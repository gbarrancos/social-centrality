(defproject social-centrality "0.1.0-SNAPSHOT"
  :description "Centrality Metrics Utility"
  :url "http://www.github.com/gbarrancos/social-centrality"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.5"]
                 [clj-http "0.9.2"]
                 [clj-http-fake "0.7.8" :scope "test"]]
  :main social-centrality.core
)
