(ns social-centrality.score-test
  (:require [clojure.test :refer :all]
            [social-centrality.graph :as graph]
            [social-centrality.score :refer :all]))

;
; 
; a - b - c
; |   |    
; e - f    
;     |
;     g - i
;     |
;     h
; (add-edges mug [[:a :b] [:b :c] [:a :e] [:e :f] [:f :b] [:f :g] [:g :i] [:g :h]])


(deftest closeness-test
  (testing "when vertice :a is the closest to the center of the graph it has the highest score"
    (let [social-graph (graph/make [[:a :b]
                                    [:b :a]
                                    [:a :c]
                                    [:c :a]
                                    [:a :d]
                                    [:d :a]
                                    [:a :e]
                                    [:e :a]])
          result (closeness social-graph)
          a-closeness (get result :a)
          others-closeness (vals (dissoc result :a))]
      (is (= 0.25 a-closeness))
      (is (every? #(< % 0.25) others-closeness))))

  (testing "when vertice :a is graph's outmost it should have the smallest score"
    ; a <-> b <-> c <-> d
    (let [social-graph (graph/make [[:a :b]
                                    [:b :a]
                                    [:b :c]
                                    [:c :b]
                                    [:c :d]
                                    [:d :c]])
          result (closeness social-graph)
          a-closeness (get result :a)
          others-closeness (vals (dissoc result :a))]
      (is (= 0.16666666666666666 a-closeness))
      (is (every? #(> % 0.1) others-closeness)))))

(deftest fraud-test
  (let [fraudsters #{:f1}
        social-graph (-> (graph/make [[:a :b] [:b :a]
                                      [:b :c] [:c :b]
                                      [:g :d] [:d :g]
                                      [:d :f1] [:f1 :d]])
                         graph/floyd-warshall)
        v-score-map {:a 1 :b 1 :c 1 :d 1 :g 1 :f1 1}]

; a <-> b <-> c         g <-> d <-> f1

    (testing "when a vertice is fraudulent, its score is zeroed"
      (is (= 0 (:f1 (fraud fraudsters social-graph v-score-map)))))
    (testing "when a vertice has no connection with a fraudster its score is unchanged"
      (is (= 1 (:a (fraud fraudsters social-graph v-score-map))))

      (testing "when a vertice has connection with a fraudster its score will decrease proportionally to the distance"
        (is (= 0.5 (:d (fraud fraudsters social-graph v-score-map))))
        (is (= 0.75 (:g (fraud fraudsters social-graph v-score-map))))))))
  

