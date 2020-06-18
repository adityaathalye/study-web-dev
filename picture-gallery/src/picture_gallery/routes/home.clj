(ns picture-gallery.routes.home
  (:require [compojure.core :refer [GET defroutes]]
            [picture-gallery.views.layout :as layout]))

(defn home [request]
  (layout/common [:h1 (str "Hello World! "
                           (get-in request [:session :user-id]
                                   "ANONYMOUS"))]))

(defroutes home-routes
  (GET "/" request (home request)))
