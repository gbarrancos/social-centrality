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
      (is (= ([:a 0.5]
              [:b 0.33]
              [:c 0.33]) (closeness social-graph))))))
    

