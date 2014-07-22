(ns social-centrality.graph-test
    (:require [clojure.test :refer :all]
              [social-centrality.graph :refer :all]))


(deftest make-test
  (testing "Creation of a new graph"
    (let [edges [[:a :b] [:a :c] [:b :c] [:c :d]] 
          expected-graph {:a {:a 0 :b 1 :c 1}
                          :b {:b 0 :c 1}
                          :c {:c 0 :d 1}}]
      (is (= expected-graph (make edges))))))

(deftest k-merge-test
  (testing "Merging"
    (testing "when intermediate vertice"
      (testing "has shorter distance paths"
        (is (= {:2 {:1 4 :3 2}}
               (k-merge {:1 {:3 -2}} {:2 {:1 4, :3 2}}))))
      (testing "does not contain shorter distance paths"
        (is (= {:2 {:1 4 :3 2}} (k-merge {:1 {:3 100}} {:2 {:1 4 :3 2}}))))
      (testing "has a new path"
        (is (= {:2 {:1 5 :3 2 :20 10}}
               (k-merge {:1 {:3 -2 :20 5}} {:2 {:1 5 :3 2}}))))
    
)))

(deftest floyd-warshall-test
  (let [graph (make [[:a :b]
                     [:a :c]
                     [:b :c]
                     [:c :d]
                     [:d :a]])]
    (testing "All Pairs Shortest Paths Calculation"
      (let [expected {:a {:a 0 :b 1 :c 1 :d 2}
                      :b {:b 0 :c 1 :d 2 :a 3}
                      :c {:c 0 :a 2 :b 3 :d 1}
                      :d {:d 0 :a 1 :b 2 :c 2}}]

        (is (= expected (floyd-warshall graph)))))))


    
