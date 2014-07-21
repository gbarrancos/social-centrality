(ns social-centrality.graph-test
    (:require [clojure.test :refer :all]
              [social-centrality.graph :refer :all]))

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
