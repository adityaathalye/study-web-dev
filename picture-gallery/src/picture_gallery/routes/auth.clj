(ns picture-gallery.routes.auth
  (:require [compojure.core :refer [defroutes GET POST]]
            [picture-gallery.models.db :as db]
            [picture-gallery.views.auth :as va]
            [ring.util.response :as response]
            [picture-gallery.views.home :as vh]))


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


(defn handle-login
  [id pass]
  (let [user (db/get-user id)]
    (if (and (= (:id user) id)
             (= (:pass user) pass))
      (assoc-in (response/redirect "/" :see-other)
                [:session :user-id] id)
      (-> (response/redirect "/login" :see-other)
          (assoc-in [:session :error]
                    "Bad username or password. Please retry.")
          (assoc-in [:session :user-id]
                    nil)))))


(defn handle-logout
  []
  (assoc-in
   (response/redirect "/" :see-other)
   [:session :user-id] nil))


(defroutes auth-routes
  (GET "/register" request
       (register-or-redirect request))
  (POST "/register" [id pass pass1]
        (handle-registration id pass pass1))

  (GET "/login" {{error :error} :session}
       (va/login-page error))
  (POST "/login" [id pass]
        (handle-login id pass))

  (GET "/logout" []
       (handle-logout)))
