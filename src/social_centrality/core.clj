(ns social-centrality.core
  (:require [social-centrality.graph :as graph]
            [clojure.string :as string])
)

;TODO: round closeness result
(defn closeness [social-graph]
  "Calculates closeness for each vertex of the given graph"
  (map (fn [edge]
         (let [shortest-paths (vals (last edge))]
          [(first edge) (. Math pow (reduce + shortest-paths) -1)]))
       (graph/floyd-warshall social-graph)))

(defn format-result [entry]
  (str (name (first entry)) " " (last entry)))

(defn -main [graph-file-path]
  (let [g (graph/from-file graph-file-path)
        result (closeness g)] 
    (spit (str graph-file-path "_closeness")
          (string/join "\n" (map format-result result)))))


