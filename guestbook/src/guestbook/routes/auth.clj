(ns guestbook.routes.auth
  (:require [compojure.core :refer [defroutes GET POST]]
            [guestbook.views.layout :as layout]
            [hiccup.form :as hf]
            [noir.response :as nr]
            [noir.session :as session]
            [noir.util.crypt :as crypt]
            [noir.validation :refer [errors? has-value? on-error rule]]
            [guestbook.models.db :as db]))

(defn format-error
  [[error]]
  [:div [:p.error [:strong error]]])


(defn control
  [field fname ftext]
  (list (on-error fname format-error)
        (hf/label fname ftext)
        (field fname)
        [:br]))

(comment
  ;; adding on-error to control introduces error state, and we now must
  ;; provide the binding, to test any call with 'control' in it's call chain
  (binding [noir.validation/*errors* (atom {})]
    (control hf/text-field "id" "screen name"))
  )


(defn registration-page
  []
  (layout/common
   (hf/form-to [:post "/register"]
               (control hf/text-field "id" "screen name")
               (control hf/password-field "pass" "password")
               (control hf/password-field "pass1" "retype password")
               (hf/submit-button "create account"))))

(comment
  (binding [noir.validation/*errors* (atom {})]
    (registration-page))
  )


(defn login-page
  []
  (layout/common
   (hf/form-to [:post "/login"]
               (control hf/text-field "id" "Screen Name")
               (control hf/password-field "pass" "Password")
               (hf/submit-button "login"))))


(defn handle-login
  [id pass]
  (let [user (db/get-user id)]
    (rule (has-value? id)
          ["id" "screen name is required"])
    (rule (has-value? pass)
          ["pass" "password is required"])
    (rule (and user (crypt/compare pass (:pass user)))
          ["pass" "invalid screen name or password"])

    (if (errors? "id" "pass")
      (login-page)
      (do (session/put! :user id)
          (nr/redirect "/")))))


(defn handle-registration
  [id pass pass1]
  (rule (= pass pass1)
        ["pass" "password was not retyped correctly"])
  (if (errors? "pass")
    (registration-page)
    (do (db/add-user-record {:id id :pass (crypt/encrypt pass)})
        (nr/redirect "/login"))))


(defroutes auth-routes
  (GET "/register" [_] (registration-page))
  (POST "/register" [id pass pass1]
        (handle-registration id pass pass1))

  (GET "/login" [] (login-page))
  (POST "/login" [id pass]
        (handle-login id pass))

  (GET "/logout" []
       (layout/common
        (hf/form-to [:post "/logout"]
                    (hf/submit-button "logout"))))
  (POST "/logout" []
        (session/clear!)
        (nr/redirect "/")))
