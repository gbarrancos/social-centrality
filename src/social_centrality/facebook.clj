(ns social-centrality.facebook
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]))

(def friends-uri "https://graph.facebook.com/me/friends")

(defn user-uri [userid]
  (str "https://graph.facebook.com/" userid))


(defn auth-get [uri user-token params]
  (let [query-pars (assoc params "access_token" user-token)]
    (json/read-str
     (:body (http/get uri {:query-params query-pars })))))


(defn my-friends [user-token]
  "Obtains the Friends List for the user"
  (loop [resp (auth-get friends-uri user-token {})
         result []]
    (let [friends (concat result (get resp "data"))
          next-page (get-in resp ["paging" "next"])]
      (if-not next-page
        friends
        (recur (json/read-str (:body (http/get next-page)))
               friends)))))


(defn paging-for [cursors-map]
  (let [before (get cursors-map "before")
        after (get cursors-map "after")]
    (if (= before after) nil after)))


(defn mutual-friends-with [userid user-token]
  "Retrieves a Mutual Friends List for an userid over the authenticated user perspective"
  (loop [pars {"fields" "context.fields(mutual_friends)"}
         result []]
    (let [resp (get-in (auth-get (user-uri userid) user-token pars)
                       ["context" "mutual_friends"])
          mut-friends (concat result (get resp "data"))
          next-cursor (paging-for (get-in resp ["paging" "cursors"]))]
      (if-not next-cursor
        mut-friends
        (recur (assoc pars "after" next-cursor)
               mut-friends)))))
  
(defn friend-to-edges[user-id friend]
  (let [uid (keyword user-id)
        fid (keyword (get friend "id"))]
    [[uid fid] [fid uid]]))

(defn friends-to-edges[user-id friends]
  (reduce (fn[res friend] (concat res (friend-to-edges user-id friend)))
          [] friends))

(defn edges-for [user-token]
  "Returns friends edges for the given Facebook User"
  (let [friends (my-friends user-token)
        friends-edges (friends-to-edges :me friends)]
    (reduce (fn [net-edges friend]
              (let [fid (get friend "id")
                    mut-friends (mutual-friends-with fid user-token)]
                (concat net-edges (friends-to-edges fid mut-friends))))
            friends-edges friends)))

        
                  

