(ns picture-gallery.routes.auth
  (:require [hiccup.form :as hf]
            [compojure.core :refer [GET POST defroutes]]
            [picture-gallery.routes.home :as home]
            [picture-gallery.views.layout :as layout]
            [ring.middleware.session :as session]
            [ring.util.response :as response]))


(defn registration-page
  [& [id]]
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
               [:br]
               (hf/submit-button "create account"))))


(defn handle-registration
  [id pass pass1 request]
  (assoc-in (response/redirect "/" :see-other)
            [:session :user-id] id))


(defroutes auth-routes
  (GET "/register" []
       (registration-page))
  (POST "/register" [id pass pass1 & request]
        (handle-registration id pass pass1 request))
  )
