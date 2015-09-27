(defproject train-routes "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT License"
            :url "http://www.opensource.org/licenses/mit-license.php"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clojurewerkz/neocons "3.1.0-rc1"]
                 [environ "1.0.0"]]
  :main ^:skip-aot train-routes.core
  :target-path "target/%s"
  :plugins [[lein-environ "1.0.0"]]
  :profiles {:uberjar {:aot :all}})
