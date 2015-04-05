(ns social-centrality.underwriting-graph
  (:require [social-centrality.graph :as graph]
            [social-centrality.score :as sco]))

(defprotocol UnderwritingGraph
  "Set of operations that characterizes a social underwriting graph implementation"

  (add-edges [this edges] "Adds a seq of edge pairs to the graph")
  (scores [this] "Returns a seq with edge-score pairs sorted by highest score")
  (add-fraudster [this vertice] "Marks a vertice as fraudster"))

(defrecord MemUnderwritingGraph [graph scores fraudsters]
  
  UnderwritingGraph
  (add-edges [this edges]
    (swap! (:graph this) (fn [graph]
                           (-> graph
                               (graph/add-edges edges)
                               graph/floyd-warshall))))
  (scores [this]
    @(:scores this))

  (add-fraudster [this vertice]
    (swap! (:fraudsters this) conj vertice)
    (swap! (:scores this) sco/score-graph (:graph this))))




(defn new-mem-underwriting-graph [] (MemUnderwritingGraph. (atom (graph/make [])) (atom {}) (atom #{})))


  
