(ns manas.test-core
  (:require [midje.sweet :refer :all]
           [manas.core :refer :all]
           [environ.core :refer [env]]))

(fact "->table-entry"
  (->table-entry ["HELLO"  [:varchar 15]])
  => '(lobos.schema/varchar :HELLO 15))

(fact "->table"
  (->table "HELLO"
           [["EMAIL" [:varchar 50]]
            ["NAME"  [:varchar 50]]])

  => (lobos.schema/table :HELLO
                         (lobos.schema/varchar :EMAIL 50)
                         (lobos.schema/varchar :NAME 50)))

(fact "has-connection?"
  (has-connection? (env :conn)) => true)