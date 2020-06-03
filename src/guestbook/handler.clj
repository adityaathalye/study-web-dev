(ns guestbook.handler
  (:gen-class)
  (:require [compojure.core :refer [defroutes routes]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [guestbook.models.db :as db]
            [guestbook.routes.auth :as auth]
            [guestbook.routes.home :refer [home-routes]]
            [hiccup.middleware :refer [wrap-base-url]]
            [ring.adapter.jetty :as jetty]))

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
  (-> (routes auth/auth-routes
              home-routes
              ;; app-routes must always be last as it
              ;; handles "not-found" routes
              app-routes)
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
