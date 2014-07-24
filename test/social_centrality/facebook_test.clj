(ns social-centrality.facebook-test
  (:require [clojure.test :refer :all]
            [social-centrality.facebook :refer :all])
  (:use clj-http.fake))

;TODO: extract these JSON strings to individual files

(def my-friends-1 "{\"data\":[{\"name\":\"Ishida Uryuu\",\"id\":\"100\"},{\"name\":\"Urahara Kisuke\",\"id\":\"500\"}],\"paging\":{\"next\":\"https://graph.facebook.com/generatedlink\"}}")

(def my-friends-2 "{ \"data\": [{\"name\":\"Inoue Orihime\",\"id\":\"900\"}], \"paging\": {\"previous\": \"https://graph.facebook.com/v2.0/274375839421076/friends?limit=5000&offset=0&access_token=CAAHeLWh2UZBUBAG7ZA1oBBY4ZBBEDn74xZBAflDkf5v6lmCmfdyjscNE0XVGMi6u0bZAHvwXPXTQOp6pNIxIviO94vDCRFBhb6aAFzaTYw6IUtl5DbI72KZBxZAIXIgJqzsONc1H5KdZCc2BcXfrZCXG0s2qfzZAXfEQc8BKc5mtKd3qVWjMnjZACaZB5ri6u9IpZAVmul8ss73qCD2L64ZCgDiZAzT\"}}")


(deftest my-friends-test
  (testing "Retrieving friends list from Facebook Graph API"
    (with-fake-routes 
      { "https://graph.facebook.com/me/friends?access_token=atoken"
        (fn [req] {:status 200 :headers {} :body my-friends-1})
        "https://graph.facebook.com/generatedlink"
        (fn [req] {:status 200 :headers {} :body my-friends-2})}

      (is (= [{"name" "Ishida Uryuu", "id" "100"}
              {"name" "Urahara Kisuke", "id" "500"}
              {"name" "Inoue Orihime", "id" "900"}] (vec(my-friends "atoken")))))))



(def mut-friends-1 "
{\"context\":{\"mutual_friends\":{\"data\":[{\"id\": \"255\", \"name\": \"Ikkaku Madarame\"} {\"id\":\"69\", \"name\": \"Kensei Muguruma\"}], \"paging\": {\"cursors\": {\"before\": \"b4\", \"after\": \"2\"}}, \"summary\": {\"total_count\": 3 }}}, \"id\": \"500\"}")

(def mut-friends-2 "
{\"context\":{\"mutual_friends\":{\"data\":[{\"id\": \"444\", \"name\": \"Shuhei Hisagi\"}], \"paging\": {\"cursors\": {\"before\": \"last\", \"after\": \"last\"}}, \"summary\": {\"total_count\": 3 }}}, \"id\": \"1389619557960841\"}")



(deftest mutual-friends-with-user-test
  (testing "Retrieving mutual friends list from Facebook Graph API"
    (with-fake-routes
      { "https://graph.facebook.com/500?fields=context.fields%28mutual_friends%29&access_token=atoken"
        (fn [req] {:status 200 :headers {} :body mut-friends-1})
        "https://graph.facebook.com/500?fields=context.fields%28mutual_friends%29&access_token=atoken&after=2"
        (fn [req] {:status 200 :headers {} :body mut-friends-2})
       }

      (is (= [{"id" "255", "name" "Ikkaku Madarame"}
              {"id" "69", "name" "Kensei Muguruma"}
              {"id" "444", "name" "Shuhei Hisagi"}] (vec (mutual-friends-with "500" "atoken")))))))
  


(deftest friend-to-edges-test
  (testing "Converting a friend connection map into a vector of edge pairs"
    (is (= [[:18 :666]
            [:666 :18]] (friend-to-edges :18 {"id" "666" "name" "Kerry King"})))))


(deftest friends-to-edges-test
  (testing "Converting a list of friend connection maps into a vector of edge pairs"
    (is (= [[:1 :2]
            [:2 :1]
            [:1 :3]
            [:3 :1]] (friends-to-edges :1 [{"id" "2" "name" "Marty Friedman"}
                                           {"id" "3" "name" "Dimebag Darrell"}])))))
                                      
