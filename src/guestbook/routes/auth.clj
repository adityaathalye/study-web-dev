(ns guestbook.routes.auth
  (:require [compojure.core :refer [defroutes GET POST]]
            [guestbook.views.layout :as layout]
            [hiccup.form :as hf]))


(defn registration-page
  []
  (layout/common
   (hf/form-to [:post "/register"]
               (hf/label "id" "screen name")
               (hf/text-field "id")
               [:br]
               (hf/label "pass" "password")
               (hf/password-field "pass")
               [:br]
               (hf/label "pass1" "retype password")
               (hf/password-field "pass1")
               [:br]
               (hf/submit-button "create account"))))

(comment
  (registration-page)
  )
