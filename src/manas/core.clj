(ns manas.core
  (:require [clojure.java.jdbc :as sql]
            [clojure.java.jdbc.sql :as s]
            [lobos.connectivity :refer [with-connection]]
            [lobos.analyzer :refer [analyze-schema]]
            [lobos.core :as l]
            [lobos.schema :as schema :refer [integer varchar]])
  (:import [java.sql DriverManager Connection Statement SQLException]))

(defmacro run-statement [env database body catch-body & [statement]]
  `(do (Class/forName (:classname ~env))
       (let [~'conn (atom nil)]
         (try
           (reset! ~'conn (DriverManager/getConnection
                          (str "jdbc:" (:subprotocol ~env) ":"
                               (:server ~env) "/" (or ~database (:database ~env)))
                          (:user ~env)
                          (:password ~env)))
           ~@(if statement
               [(list 'let '[stmt (.createStatement @conn)]
                      (list '.executeUpdate 'stmt statement))])
           ~@body
           (catch SQLException ~'e
             ~@catch-body)
           (finally
             (if-not (nil? (deref ~'conn))
               (.close (deref ~'conn))))))))

(defn has-connection? [env & [url]]
  (let [env (if url (assoc env :server url) env)]
    (run-statement env "" (true) (false))))

(defn all-databases [env]
  (run-statement env ""
                 ((let [rs (.getCatalogs (.getMetaData @conn))]
                    (loop [rs rs acc []]
                      (if (.next rs)
                        (recur rs (conj acc (.getString rs "TABLE_CAT")))
                        acc))))
                 ()))

(defn has-database? [env & [schema]]
  (run-statement env schema (true) (false)))

(defn create-database [env & [schema]]
  (run-statement env ""
                 (true)
                 (false)
                 (str "create database " (or schema (:database env)))))

(defn drop-database [env & [schema]]
  (run-statement env ""
                 (true)
                 (false)
                 (str "drop database " (or schema (:database env)))))

(defn keywordize [x]
  (if (keyword? x) x (keyword x)))

(defn ->table-entry [[k [t & args]]]
  (concat [(symbol (str "lobos.schema/" (name t))) (keyword (name k))] args))

(defn ->table [table columns]
  (eval
   (concat ['lobos.schema/table (keyword (name table))]
           (map ->table-entry columns))))

(defn ->subname [env]
  (if (:subname env) env
      (assoc env :subname (str (:server env) "/" (:database env)))))

(defn all-tables [env]
  (with-connection (->subname env)
    (-> (analyze-schema) :tables keys)))

(defn all-columns [env table]
  (with-connection (->subname env)
    (-> (analyze-schema) :tables (get (keywordize table)) :columns keys)))

(defn has-table? [env table]
  (with-connection (->subname env)
    (-> (analyze-schema) :tables (get (keywordize table)) nil? not)))

(defn create-table [env table columns]
  (with-connection (->subname env)
    (try (l/create (->table table columns))
         true
         (catch SQLException e
           false))))

(defn drop-table [env table]
  (with-connection (->subname env)
    (try (l/drop (schema/table table))
         true
         (catch SQLException e
           false))))
