(ns guestbook.routes.home
  (:require [compojure.core :refer :all]
            [guestbook.views.layout :as layout]
            [hiccup.form :as hf]))

(defn show-guests []
  (let [dummy-messages
        [{:message "howdy" :name "bob" :timestamp nil}
         {:message "hello" :name "alice" :timestamp nil}]]
    [:ul.guests
     (map (fn [msg-record]
            [:li
             [:blockquote (:message msg-record)]
             [:p "-" [:cite (:name msg-record)]]
             [:time (:timestamp msg-record)]])
          dummy-messages)]))


(defn home [& [name message error]]
  (layout/common
   [:h1 "Guestbook"]
   [:p "Welcome to my guestbook app"]
   [:p error]
   (show-guests) ; punch in list of existing comments
   [:hr]
   (hf/form-to [:post "/"]
               [:p "Name:"]
               (hf/text-field "name" name)
               [:p "Message:"]
               (hf/text-area {:rows 10 :cols 40} "message" message)
               [:br]
               (hf/submit-button "comment"))))


(defroutes home-routes
  (GET "/" [] (home)))
