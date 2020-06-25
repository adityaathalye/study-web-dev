(ns picture-gallery.views.common
  (:require [hiccup.element :as he]
            [picture-gallery.views.auth :as va]))

(defn top-nav
  [& [{user-id :user-id} error-msg]]
  [:div
   (if user-id
     [:div
      [:div {:style "float: right"}
       [:span.top-menu-item [:strong (he/link-to "/upload" "upload")]]
       [:span.top-menu-item [:strong (va/logout-link user-id)]]]
      [:div {:style "float: none"}
       [:h1 "Hello, "  user-id]]]
     [:div
      [:span {:style "float: left"} [:h1 (str "Hello World!")]]
      [:span {:style "float: right"}
       (va/login-form user-id error-msg)
       [:span {:style "float: right"}
        [:strong " or " (he/link-to "/register" "register")]]]])])
