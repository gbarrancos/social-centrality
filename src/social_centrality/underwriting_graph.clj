(ns social-centrality.underwriting-graph
  (:require [social-centrality.graph :as graph]
            [social-centrality.score :as sco]))

(defprotocol UnderwritingGraph
  "Set of operations that characterizes a social underwriting graph implementation"

  (add-edges! [this edges] "Adds a seq of edge pairs to the graph")
  (score! [this] "Triggers graph score calculation")
  (add-fraudster! [this vertice] "Marks a vertice as fraudster")
  (scores [this] "Returns a seq with edge-score pairs sorted by highest score"))

(defrecord MemUnderwritingGraph [graph scores fraudsters]
  
  UnderwritingGraph
  (add-edges! [this edges]
    (swap! (:graph this) (fn [graph]
                           (->> graph
                               (graph/add-edges edges)
                               graph/floyd-warshall))))
  (score! [this]
    (swap! (:scores this) (fn [_] (sco/score-graph @(:graph this)
                                                   @(:fraudsters this)))))

  (add-fraudster! [this vertice]
    (swap! (:fraudsters this) conj vertice)
    (score! this))
  
  (scores [this]
    @(:scores this)))




(defn new-mem-underwriting-graph [] (MemUnderwritingGraph. (atom (graph/make [])) (atom {}) (atom #{})))
