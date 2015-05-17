(ns social-centrality.mem-underwriting-graph
  (:require [social-centrality.protocols.underwriting-graph :as ug-pro]
            [social-centrality.graph :as graph]
            [social-centrality.score :as sco]))

(defrecord MemUnderwritingGraph [graph scores fraudsters]

  ug-pro/UnderwritingGraph
  (add-edges! [this edges]
    (swap! (:graph this) (fn [graph]
                           (->> graph
                               (graph/add-edges edges)
                               graph/floyd-warshall))))
  (score! [this]
    (swap! (:scores this) (fn [_] (sco/score-graph @(:graph this)
                                                   @(:fraudsters this)))))

  (add-fraudster! [this vertice]
    (swap! (:fraudsters this) conj vertice))
  
  (scores [this]
    @(:scores this)))



(defn new [] (MemUnderwritingGraph. (atom (graph/make [])) (atom {}) (atom #{})))
