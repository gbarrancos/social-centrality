(ns social-centrality.core
  (:require [social-centrality.graph :as graph]
            [social-centrality.facebook :as facebook]
            [social-centrality.score :as score]
            [clojure.string :as string]))

(defn format-entry [entry]
  (str (name (first entry)) " " (last entry)))

(defn -main [arg]
  (let [g 
        (if (. arg startsWith "fbtoken")
          (graph/make (facebook/edges-for (last (.split arg "fbtoken:"))))
          (graph/from-file arg))
        result (score/closeness g)]
    (spit (str arg "_closeness")
          (string/join "\n" (map format-entry result)))))


