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


(defn login-or-redirect
  [{{user-id :user-id error :error} :session}]
  (if user-id
    (assoc-in (response/redirect "/" :see-other)
              [:session :user-id] user-id)
    (va/login-page error)))


(defn handle-login
  [{{:keys [id pass]} :params
    session :session :as request}]
  (let [user (db/get-user id)
        session-live? (not-empty (:user-id session))]
    (cond
      ;; prevent login without logout
      session-live?
      (assoc-in (response/redirect "/" :see-other)
                [:session :user-id] id)
      ;; actual login
      (and (= (:id user) id)
           (= (:pass user) pass))
      (assoc-in (response/redirect "/" :see-other)
                [:session :user-id] id)
      ;; failed login infinite loop
      :else
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

  (GET "/login" request
       (login-or-redirect request))
  (POST "/login" request
        (handle-login request))

  (GET "/logout" []
       (handle-logout)))
