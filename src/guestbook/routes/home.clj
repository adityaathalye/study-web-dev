(ns guestbook.routes.home
  (:require [compojure.core :refer :all]
            [guestbook.views.layout :as layout]
            [hiccup.form :as hf]
            [guestbook.models.db :as db]))


(defn show-guests []
  (let [guest-records (db/read-guests)]
    [:ul.guests
     (map (fn [msg-record]
            [:li
             [:blockquote (:message msg-record)]
             [:p "-" [:cite (:name msg-record)]]
             [:time (:timestamp msg-record)]])
          guest-records)]))


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


(defn save-message
  [name message]
  (cond
    (empty? name) (home name message
                        "oops, they forgot to leave a name")
    (empty? message) (home name message
                           "pffft, they didn't leave a message")
    :else (do ;; save message, and then re-render server-side hiccup HTML
            (db/save-message name message)
            (home))))


(defroutes home-routes
  (GET "/" [] (home))
  (POST "/" [name message] (save-message name message)))
