(ns picture-gallery.routes.auth
  (:require [hiccup.form :as hf]
            [compojure.core :refer [GET POST defroutes]]
            [picture-gallery.routes.home :as home]
            [picture-gallery.views.layout :as layout]
            [ring.middleware.session :as session]
            [ring.util.response :as response]))


(defn registration-page
  [& [id error-msg]]
  (layout/common
   (hf/form-to [:post "/register"]
               (hf/label "user-id" "user id")
               (hf/text-field "id" id)
               [:br]
               (hf/label "pass" "password")
               (hf/password-field "pass")
               [:br]
               (hf/label "pass1" "retype password")
               (hf/password-field "pass1")
               (if error-msg
                 [:p.error error-msg]
                 [:br])
               (hf/submit-button "create account"))))


(defn registration-error?
  [id pass pass1]
  (cond (empty? id)
        "User ID is required."
        (>= 5 (count pass))
        "Password must be greater than 5 characters."
        (not= pass pass1)
        "Passwords do not match."
        :else false))


(defn handle-registration
  [id pass pass1]
  (if-let [error-msg (registration-error? id pass pass1)]
    (registration-page id
                       error-msg)
    (assoc-in (response/redirect "/" :see-other)
              [:session :user-id] id)))


(defroutes auth-routes
  (GET "/register" []
       (registration-page))
  (POST "/register" [id pass pass1]
        (handle-registration id pass pass1)))
