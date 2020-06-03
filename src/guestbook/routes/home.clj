(ns guestbook.routes.home
  (:require [compojure.core :refer :all]
            [guestbook.views.layout :as layout]
            [hiccup.form :as hf]
            [guestbook.models.db :as db]
            [guestbook.utils.datetime :as dateutil]
            [noir.session :as session]))


(defn show-guests []
  (let [guest-records (db/read-guests)]
    [:ul.guests
     (map (fn [msg-record]
            [:li
             [:blockquote (:message msg-record)]
             [:p "-" [:cite (:name msg-record)]]
             [:time (dateutil/format-time
                     (:timestamp msg-record))]])
          guest-records)]))


(defn home [& [name message error]]
  (layout/common
   [:h1 "Guestbook " (session/get :user)]
   [:p "Welcome to my guestbook app"]
   (hf/form-to [:post "/"]
               [:p "Name:"]
               (hf/text-field "name" name)
               [:p "Message:"]
               (hf/text-area {:rows 10 :cols 40} "message" message)
               [:br]
               (hf/submit-button "comment"))
   [:p.error [:strong error]]
   [:br]
   [:hr]
   [:br]
   (show-guests))) ; punch in list of existing comments


(defn save-message
  [name message]
  (cond
    (empty? name) (home name message
                        "Yikes, you forgot to add your name")
    (empty? message) (home name message
                           "Oops, you forgot to leave a message")
    :else (do ;; save message, and then re-render server-side hiccup HTML
            (db/save-message name message)
            (home))))


(defroutes home-routes
  (GET "/" [] (home))
  (POST "/" [name message] (save-message name message)))
