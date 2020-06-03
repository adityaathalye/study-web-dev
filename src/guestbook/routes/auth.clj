(ns guestbook.routes.auth
  (:require [compojure.core :refer [defroutes GET POST]]
            [guestbook.views.layout :as layout]
            [hiccup.form :as hf]))


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
