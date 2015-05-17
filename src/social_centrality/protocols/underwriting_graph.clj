(ns social-centrality.protocols.underwriting-graph)

(defprotocol UnderwritingGraph
  "Set of operations that characterizes a social underwriting graph implementation"
  
  (add-edges! [this edges] "Adds a seq of edge pairs to the graph")
  (score! [this] "Triggers graph score calculation")
  (add-fraudster! [this vertice] "Marks a vertice as fraudster")
  (scores [this] "Returns a seq with edge-score pairs sorted by highest score"))
