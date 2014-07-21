(ns social-centrality.graph
  (:require [clojure.string :as string])
  )

(defn set-distance [graph edge distance]
  "Sets the distance between two vertices in the given graph"
  (let [from (first edge)
        to (last edge)]
    (if (map? (get graph from))
      (assoc-in graph [from to] distance)
      (conj graph {from {from 0 to distance}}))))

(defn paths [graph vertice]
  (get graph vertice))

(defn has-path? [graph src dst]
  "Checks if vertex 'src' has a path to vertex 'dst'"
  (contains? (paths graph src) dst))

(defn make [edges]
  "Build a graph from a seq of edges pairs [source destination]"
  (reduce (fn [graph edge]
            (set-distance graph edge 1)) {} edges))

(defn k-merge [k-paths paths]
  "Builds a new map with shortest paths routes using 'k' as intermediate vertice and  merging them into 'paths'"
  (let [k-vertice (first (keys k-paths))
        vertice (first (keys paths))
        edges (get paths vertice)
        v-to-k-dist (get edges k-vertice)]
    (reduce (fn [result k-edge] 
              (let [v-dist (get edges (first k-edge) (Integer/MAX_VALUE))
                    new-dist (+ (last k-edge) v-to-k-dist) ]
                (if (< new-dist v-dist)
                  (assoc-in result [vertice (first k-edge)] new-dist)
                  result 
                  ))
              ) paths (get k-paths k-vertice))))

(defn k-step [graph k]
  "Calculates the shortest paths for the graph, using k as intermediate vertice"
  (reduce (fn [graph v]
            (if (has-path? graph v k)
              (conj graph (k-merge {k (paths graph k)} {v (paths graph v)}))
              graph))
          graph (keys graph)))

(defn floyd-warshall [graph]
  "Returns a graph with all-pairs shortest paths calculated"
  (reduce (fn [result-graph k-vertice]
            (k-step result-graph k-vertice))
          graph (keys graph)))

(defn from-file [path]
  "Reads a graph from file."
  (make 
   (map (fn [str-pair]
          (map keyword (string/split str-pair #"(\s+)")))
        (string/split-lines (slurp path)))))
