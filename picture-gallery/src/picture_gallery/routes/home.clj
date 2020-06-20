(ns picture-gallery.routes.home
  (:require [compojure.core :refer [defroutes GET]]
            [picture-gallery.views.home :as vh]))

(defroutes home-routes
  (GET "/" {{user-id :user-id} :session} (vh/home user-id)))
