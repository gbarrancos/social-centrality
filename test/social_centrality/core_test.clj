(ns social-centrality.core-test
  (:require [clojure.test :refer :all]
            [social-centrality.core :refer :all]
            [social-centrality.graph :as graph]))

(deftest closeness-test
  (testing "Calculating closeness of all graph's vertices"
    (let [social-graph (graph/make [[:a :b]
                                    [:a :c]
                                    [:b :c]
                                    [:c :a]])]
      (is (= [[:c 0.3333333333333333]
              [:b 0.3333333333333333]
              [:a 0.5]] (closeness social-graph))))))
    

