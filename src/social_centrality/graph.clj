(ns social-centrality.graph
  (:require [clojure.string :as string]))

(defn- set-distance [graph edge distance]
  "Sets the distance between two vertices in the given graph"
  (let [[from to] edge]
    (if (get graph from)
      (assoc-in graph [from to] distance)
      (conj graph {from {from 0 to distance}}))))

(defn- paths [graph vertice]
  (get graph vertice))

(defn edges [graph vertice]
  (paths graph vertice))

(defn- vertices [graph]
  (keys graph))

(defn- has-path? [graph src dst]
  "Checks if vertex 'src' has a path to vertex 'dst'"
  (contains? (paths graph src) dst))

(defn distance [graph v1 v2]
  "Retrieves distance between two vertices. If there are no paths between v1 and v2 Integer/MAX_VALUE is returned"
  (get (paths graph v1) v2 (Integer/MAX_VALUE)))

(defn add-edges [edges graph]
  (reduce (fn [res edge] (set-distance res edge 1)) graph edges))

(defn make [edges]
  "Build a graph from a seq of edges pairs [source destination]"
  (reduce (fn [res edge] (set-distance res edge 1)) {} edges))

(defn k-merge [graph v k]
 "Builds a new map with shortest paths calculated for vertice 'v' based on intermediate vertice 'k'"
  (let [edges (paths graph v)
       v-to-k-dist (get edges k)]
    (reduce (fn [result k-edge]
              (let [v-dist (get edges (first k-edge) (Integer/MAX_VALUE))
                    new-dist (+ (last k-edge) v-to-k-dist) ]
                (if (< new-dist v-dist)
                  (assoc result (first k-edge) new-dist)
                  result))) (paths graph v) (paths graph k))))

(defn- k-step [graph k]
  "Calculates the shortest paths for the graph, using k as intermediate vertice"
  (reduce (fn [graph v]
            (if (has-path? graph v k)
              (conj graph {v (k-merge graph v k)})
              graph)) graph (vertices graph)))

(defn floyd-warshall [graph]
  "Returns a graph with all-pairs shortest paths calculated"
  (reduce k-step graph (vertices graph)))

(defn from-file [path]
  "Reads a graph from file."
  (make 
   (map (fn [str-pair]
          (map keyword (string/split str-pair #"(\s+)")))
        (string/split-lines (slurp path)))))
