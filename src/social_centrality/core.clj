(ns social-centrality.core
  (:require [social-centrality.graph :as graph]
            [social-centrality.facebook :as facebook]
            [clojure.string :as string])
)

(defn closeness [social-graph]
  "Calculates closeness for each vertex of the given graph"
  (map (fn [edge]
         (let [shortest-paths (vals (last edge))]
          [(first edge) (. Math pow (reduce + shortest-paths) -1)]))
       (graph/floyd-warshall social-graph)))

(defn format-entry [entry]
  (str (name (first entry)) " " (last entry)))

(defn -main [arg]
  (let [g 
        (if (. arg startsWith "fbtoken")
          (graph/make (facebook/edges-for (last (.split arg "fbtoken:"))))
          (graph/from-file arg))
        result (closeness g)]
    (spit (str arg "_closeness")
          (string/join "\n" (map format-entry result)))))


