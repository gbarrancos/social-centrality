(ns social-centrality.graph)

(defn set-distance [graph edge distance]
  "Sets the distance between two vertices in the given graph"
  (let [from (first edge)
        to (last edge)]
    (if (map? (get graph from))
      (conj graph {from (conj (get graph from) {to distance})})
      (conj graph {from {to distance}}))))

(defn paths [graph vertice]
  (get graph vertice))

(defn has-path? [graph src dst]
  "Checks if vertex 'src' has a path to vertex 'dst'"
  (contains? (paths graph src) dst))

(defn make [edges]
  "Build a graph from a seq of edges pairs [source destination]"
  (reduce (fn [graph edge]
            (set-distance graph edge 1)) {} edges))

(defn k-merge [k-paths v-paths]
  (let [k-vertice (first (keys k-paths))
        v-vertice (first (keys v-paths))
        k-edges (get k-paths k-vertice)
        v-edges (get v-paths v-vertice)
        v-to-k-dist (get v-edges k-vertice)]
    (reduce (fn [result k-edge] 
              (let [v-dist (get v-edges (first k-edge) (Integer/MAX_VALUE))
                    new-dist (+ (last k-edge) v-to-k-dist) ]
                (if (< new-dist v-dist)
                  (assoc-in result [v-vertice (first k-edge)] new-dist)
                  result 
                  ))
              ) v-paths k-edges)))

(defn k-step [graph k]
  "Calculates the shortest paths for the graph, using k as intermediate vertice"
  (reduce (fn [graph v]
            (if (has-path? graph v k)
              (conj graph (k-merge (paths k) (paths v))) ;wont work!
              graph))
          graph (keys graph)))

(defn floyd-warshall [graph]
  "Returns a graph with all-pairs shortest paths calculated"
  (reduce (fn [result-graph k-vertice]
            (k-step result-graph k-vertice))
          graph (keys graph)))

