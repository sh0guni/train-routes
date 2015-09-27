(ns train-routes.core
  (:gen-class)
  (:require [clojurewerkz.neocons.rest :as nr]
            [clojurewerkz.neocons.rest.nodes :as　nn]
            [clojurewerkz.neocons.rest.relationships :as　nrl]
            [clojurewerkz.neocons.rest.paths :as np]
            [clojurewerkz.neocons.rest.cypher :as cy]
            [environ.core :refer [env]]))

(def database-url
  (env :database-url))

(def relationships [{:type "connection" :direction "out"}
                    {:type "arrival" :direction "out"}
                    {:type "departure" :direction "out"}])

(defn create-train [conn start end trainNumber departureTime arrivalTime]
  (let [departure (nn/create conn {:trainnumber trainNumber
                                   :time departureTime})
        arrival (nn/create conn {:trainnumber trainNumber
                                   :time arrivalTime})
        depConn (nrl/create conn start departure :departure {:cost 9999})
        arrConn (nrl/create conn arrival end :arrival {:cost 9999})]
    (nrl/create conn departure arrival :connection
      {:cost (- arrivalTime departureTime)})))

(defn create-graph [conn]
  (let [station1 (nn/create conn {:name "station1"})
        station2 (nn/create conn {:name "station2"})
        station3 (nn/create conn {:name "station3"})
        route (create-train conn station1 station2 1 1000 1200)]
    (list station1 station2)))

(defn drop-graph [conn]
  (cy/tquery conn "match (n) optional match (n)-[r]-() delete n, r"))

(defn find-connection [conn]
    (try
      (let [stations (create-graph conn)]
        (np/shortest-between conn (:id (first stations)) (:id (last stations))
          :relationships relationships :max-depth 9999))
      (catch Exception e (str "caught exception: " (.getMessage e)))
      (finally (drop-graph conn))))

(defn -main
  [& args]
  (let [database-url (env :database-url)
        conn (nr/connect database-url)]
    (println (find-connection conn))))
