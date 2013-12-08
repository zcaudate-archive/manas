(defproject im.chit/manas "0.1.0-SNAPSHOT"
  :description "Tools and analysers for the sql database"
  :url "https://www.github.com/zcaudate/manas"
  :license {:name "The MIT License"
            :url "http://http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/java.jdbc "0.3.0-beta1"]
                 [mysql/mysql-connector-java "5.1.25"]
                 [lobos "1.0.0-beta1" :exclusions [org.clojure/java.jdbc]]]
  :profiles {:dev {:dependencies [[midje "1.6.0"]]}})
