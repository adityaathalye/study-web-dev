(ns picture-gallery.routes.home
  (:require [compojure.core :refer [defroutes GET]]
            [hiccup.element :as he]
            [picture-gallery.views.layout :as layout]))


(defn home [request]
  (let [user-id (get-in request [:session :user-id])]
    (layout/common [:h1 (str "Hello World!")]
                   [:h2 (or user-id
                            (he/link-to "/register" "register"))])))


(defroutes home-routes
  (GET "/" request (home request)))
