(ns social-centrality.facebook
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]))

(def friends-uri "https://graph.facebook.com/me/friends")
(def mutual-friends-uri "")



(defn auth-request [uri user-token]
  (json/read-str
   (:body (http/get uri {:query-params {"access_token" user-token}}))))


(defn my-friends [user-token]
  (loop [resp (auth-request friends-uri user-token)
         result []]
    (let [friends (concat result (get resp "data"))
          next-page (get-in ["paging" "next"] resp)]
      (if-not next-page
        friends
        (recur (json/read-str (:body (http/get next-page)))
               friends)))))

  
  
  
  



(defn mutual-friends-with [userid user-token])

  
