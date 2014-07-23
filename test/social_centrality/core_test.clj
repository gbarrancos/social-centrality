(ns social-centrality.core-test
  (:require [clojure.test :refer :all]
            [social-centrality.core :refer :all]
            [social-centrality.graph :as graph]))

(deftest closeness-test
  (testing "when vertice :a is the closest to the center of the graph"
    (let [social-graph (graph/make [[:a :b]
                                    [:b :a]
                                    [:a :c]
                                    [:c :a]
                                    [:a :d]
                                    [:d :a]
                                    [:a :e]
                                    [:e :a]])
          result (closeness social-graph)
          a-closeness (last (last (filter (fn [r] (= :a (first r))) result))) ]
      (is (= 0.25 a-closeness ))
      (is (not-any? (fn [r] (> (last r) a-closeness)) result))))

  (testing "when vertice :a is graph's outmost"
    ; a <-> b <-> c <-> d <-> e
    (let [social-graph (graph/make [[:a :b]
                                    [:b :a]
                                    [:b :c]
                                    [:c :b]
                                    [:c :d]
                                    [:d :c]
                                    [:d :e]
                                    [:e :d]])
          result (closeness social-graph)
          a-closeness (last (last (filter (fn [r] (= :a (first r))) result)))]
      (is (= 0.1 a-closeness))
      (is (not-any? (fn [r] (< (last r) a-closeness)) result))))
      
      
)
    

