(ns social-centrality.score
  (:require [social-centrality.graph :as g]))


(defn- distance-fraud-k [v-to-fraudster-dist]
  (cond 
   (= v-to-fraudster-dist 0) 0 ; fraud guy: zero his score
   (= (Integer/MAX_VALUE) v-to-fraudster-dist ) 1 ; no connection = no-op
   :else (- 1 (. Math pow 0.5 v-to-fraudster-dist)))); referred= (1 - .5 ^ dist)

; coefficient tends to 0 depending on amount of direct connections to fraudster
(defn- fraud-coefficient [edges fraudsters]
  (reduce (fn [res fraudster]
            (* res (distance-fraud-k (get edges fraudster (Integer/MAX_VALUE)))))
            1 fraudsters))

(defn closeness [social-graph]
  "Calculates closeness for each vertex of the given graph and returns a map with vertice label as key and its calculated closeness score as value"
  (reduce (fn [res [vertice edges]]
            (let [shortest-paths (vals edges)
                  score (. Math pow (reduce + shortest-paths) -1)]
              (assoc res vertice score)))
            {} (g/floyd-warshall social-graph)))


(defn fraud [fraudsters social-graph v-score-map]
  "Multiply each vertex's score by its fraud coefficient based on the distance between vertice and fraudsters"
  (reduce (fn [res [vertice score]]
               (let [edges (get social-graph vertice)
                     k (fraud-coefficient edges fraudsters)]
                 (assoc res vertice (* score k)))) {} v-score-map))
                        
(defn score-graph [graph fraudsters]
  (->> graph
      closeness
      (fraud fraudsters graph)))

