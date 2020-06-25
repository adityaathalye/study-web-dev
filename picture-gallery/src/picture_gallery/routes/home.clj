(ns picture-gallery.routes.home
  (:require [compojure.core :refer [defroutes GET]]
            [picture-gallery.views.home :as vh]))

(defroutes home-routes
  (GET "/" request (vh/home (:session request))))
