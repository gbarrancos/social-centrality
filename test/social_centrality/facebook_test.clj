(ns social-centrality.facebook-test
  (:require [clojure.test :refer :all]
            [social-centrality.facebook :refer :all])
  (:use clj-http.fake))

(def my-friends-1 "{\"data\":[{\"name\":\"Ishida Uryuu\",\"id\":\"100\"},{\"name\":\"Urahara Kisuke\",\"id\":\"500\"}],\"paging\":{\"next\":\"https://graph.facebook.com/generatedlink\"}}")

(def my-friends-2 "{ \"data\": [{\"name\":\"Inoue Orihime\",\"id\":\"900\"}], \"paging\": {\"previous\": \"https://graph.facebook.com/v2.0/274375839421076/friends?limit=5000&offset=0&access_token=CAAHeLWh2UZBUBAG7ZA1oBBY4ZBBEDn74xZBAflDkf5v6lmCmfdyjscNE0XVGMi6u0bZAHvwXPXTQOp6pNIxIviO94vDCRFBhb6aAFzaTYw6IUtl5DbI72KZBxZAIXIgJqzsONc1H5KdZCc2BcXfrZCXG0s2qfzZAXfEQc8BKc5mtKd3qVWjMnjZACaZB5ri6u9IpZAVmul8ss73qCD2L64ZCgDiZAzT\"}}")


(deftest my-friends-test
  (testing "Retrieving friends list from Facebook Graph API"
    (with-fake-routes 
      { "https://graph.facebook.com/me/friends?access_token=atoken"
        (fn [req] {:status 200 :headers {} :body my-friends-1})
        "https://graph.facebook.com/generatedlink"
        (fn [req] {:status 200 :headers {} :body my-friends-2})}

      (is (= '(("Ishida Uryuu" "100")
               ("Urahara Kisuke" "500")
               ("Inoue Orihime" "900"))
             (map vals (my-friends "atoken")))))))
