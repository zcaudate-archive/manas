(defproject im.chit/manas "0.1.0-SNAPSHOT"
  :description "Tools and analysers for the sql database"
  :url "https://www.github.com/zcaudate/manas"
  :license {:name "The MIT License"
            :url "http://http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/java.jdbc "0.3.0-beta1"]
                 [lobos "1.0.0-beta1" :exclusions [org.clojure/java.jdbc]]]
  :profiles {:dev {:dependencies [[midje "1.6.0"]
                                  [environ "0.4.0"]
                                  [mysql/mysql-connector-java "5.1.25"]]
                   :plugins [[lein-midje "3.1.3"]
                             [lein-environ "0.4.0"]]
                   :env {:username "root" :password "root"}}
             :travis {:env {:conn {:classname "com.mysql.jdbc.Driver"
                                   :subprotocol "mysql"
                                   :subname "//127.0.0.1:3306/manas_test"
                                   :server "//127.0.0.1:3306"
                                   :database "manas_test"
                                   :user "travis" :password ""}}}})
