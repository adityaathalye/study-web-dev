(ns picture-gallery.handler
  (:require [compojure.core :refer [defroutes routes]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [hiccup.middleware :refer [wrap-base-url]]
            [picture-gallery.routes.auth :refer [auth-routes]]
            [picture-gallery.routes.home :refer [home-routes]]
            [picture-gallery.routes.upload :refer [upload-routes]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [ring.middleware.resource :refer [wrap-resource]]))

(defn init []
  (println "picture-gallery is starting"))

(defn destroy []
  (println "picture-gallery is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes auth-routes
              home-routes
              upload-routes
              app-routes)
      (handler/site)
      (wrap-base-url)))
