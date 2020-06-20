(ns picture-gallery.views.auth
  (:require [hiccup.form :as hf]
            [picture-gallery.views.layout :as layout]))

(defn registration-page
  [& [id error-msg]]
  (layout/common
   (hf/form-to [:post "/register"]
               (hf/label "user-id" "user id")
               (hf/text-field {:tabindex 1} "id" id)
               [:br]
               (hf/label "pass" "password")
               (hf/password-field {:tabindex 2} "pass")
               [:br]
               (hf/label "pass1" "retype password")
               (hf/password-field {:tabindex 3} "pass1")
               (if error-msg
                 [:p.error error-msg]
                 [:br])
               (hf/submit-button "create account"))))
