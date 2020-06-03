(ns guestbook.routes.auth
  (:require [compojure.core :refer [defroutes GET POST]]
            [guestbook.views.layout :as layout]
            [hiccup.form :as hf]
            [noir.response :as nr]
            [noir.session :as session]))


(defn control
  [field fname ftext]
  (list (hf/label fname ftext)
        (field fname)
        [:br]))

(comment
  (control hf/text-field "id" "screen name")
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
  (registration-page)
  )


(defn login-page
  [& [error]]
  (layout/common
   (when error
     [:div [:p.error [:strong "Login error: " error]]])
   (hf/form-to [:post "/login"]
               (control hf/text-field "id" "Screen Name")
               (control hf/password-field "pass" "Password")
               (hf/submit-button "login"))))


(defn handle-login
  [id pass]
  (cond
    (empty? id) (login-page
                 "screen name is required")
    (empty? pass) (login-page
                   "password is required")
    (and (= "foo" id)
         (= "bar" pass)) (do (session/put! :user id)
                             (nr/redirect "/"))
    :else (login-page
           "authentication failed")))


(defroutes auth-routes
  (GET "/register" [_] (registration-page))
  (POST "/register" [id pass pass1]
        (if (= pass pass1)
          (nr/redirect "/")
          (registration-page)))

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
