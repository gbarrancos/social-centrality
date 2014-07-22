(ns social-centrality.core
  (:require [social-centrality.graph :as graph])
)

(defn closeness [social-graph]
  "Calculates closeness for each vertex of the given graph"
  (map (fn [edge]
         (let [shortest-paths (vals (last edge))]
          [(first edge) (. Math pow (reduce + shortest-paths) -1)]))
       (graph/floyd-warshall social-graph)))


(defn -main [graph-file-path]
  (let [g (graph/from-file graph-file-path)
        result (vec (closeness g)) ]
    (spit (str graph-file-path "_closeness") result)))


