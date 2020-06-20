(ns picture-gallery.views.home
  (:require [hiccup.element :as he]
            [picture-gallery.views.layout :as layout]))

(defn home [& [user-id error-msg]]
  (layout/common [:div
                  (if user-id
                    [:div
                     [:div {:style "float: right"}
                      [:strong (va/logout-link user-id)]]
                     [:div {:style "float: none"}
                      [:h1 "Hello, "  user-id]]]
                    [:div
                     [:span {:style "float: left"} [:h1 (str "Hello World!")]]
                     [:span {:style "float: right"}
                      (va/login-form user-id error-msg)
                      [:span {:style "float: right"}
                       [:strong " or " (he/link-to "/register" "register")]]]])]))
