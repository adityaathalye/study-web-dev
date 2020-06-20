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


(defn register-or-redirect
  [{{user-id :user-id} :session}]
  (if user-id
    (assoc-in (response/redirect "/" :see-other)
              [:session :user-id] user-id)
    (va/registration-page)))


(defn handle-registration
  [id pass pass1]
  (let [form-error-msg (registration-error? id pass pass1)
        user-created? (db/create-one-user {:id id :pass pass})]
    (cond
      ;; complain if non-compliant input
      form-error-msg
      (va/registration-page id
                            form-error-msg)
      ;; complain if user exists
      (not user-created?)
      (va/registration-page id
                            "Sorry, user name is already taken.")
      ;; go ahead
      :else (assoc-in (response/redirect "/" :see-other)
                      [:session :user-id] id))))


(defroutes auth-routes
  (GET "/register" request
       (register-or-redirect request))
  (POST "/register" [id pass pass1]
        (handle-registration id pass pass1)))
