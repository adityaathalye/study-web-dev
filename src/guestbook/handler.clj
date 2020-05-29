(ns guestbook.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [ring.adapter.jetty :as jetty]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [guestbook.routes.home :refer [home-routes]]
            [guestbook.models.db :as db])
  (:gen-class))


(defn init []
  (println "guestbook is starting")
  (when-not (.exists (java.io.File. "./db.sq3"))
    (db/create-guestbook-table)))


(defn destroy []
  (println "guestbook is shutting down"))


(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))


(def app
  (-> (routes home-routes app-routes)
      (handler/site)
      (wrap-base-url)))


(defn -main
  [& [port]]
  (let [port (Integer. (or port
                           (System/getenv "PORT")
                           3000))]
    (init)
    (jetty/run-jetty #'app
                     {:port port
                      :join? false})))
