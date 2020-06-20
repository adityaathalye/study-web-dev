(ns picture-gallery.routes.auth
  (:require [compojure.core :refer [defroutes GET POST]]
            [picture-gallery.models.db :as db]
            [picture-gallery.views.auth :as va]
            [ring.util.response :as response]))

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
    (va/registration-page id
                          error-msg)
    (do (db/create-user {:id id :pass pass})
        (assoc-in (response/redirect "/" :see-other)
                  [:session :user-id] id))))


(defroutes auth-routes
  (GET "/register" []
       (va/registration-page))
  (POST "/register" [id pass pass1]
        (handle-registration id pass pass1)))
