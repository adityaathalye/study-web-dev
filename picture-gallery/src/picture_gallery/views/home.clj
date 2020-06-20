(ns picture-gallery.views.home
  (:require [hiccup.element :as he]
            [picture-gallery.views.layout :as layout]))

(defn home [user-id]
  (layout/common [:h1 (str "Hello World!")]
                 [:h2 (or user-id
                          (he/link-to "/register" "register"))]))
