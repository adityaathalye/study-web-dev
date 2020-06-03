(ns guestbook.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [ring.adapter.jetty :as jetty]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [guestbook.routes.home :as grh :refer [home-routes]]
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
  ;; (-> (routes home-routes app-routes)
  ;;     (handler/site)
  ;;     (wrap-base-url))
  grh/home-routes-handler-coded-manually)


(defn -main
  [& [port]]
  (let [port (Integer. (or port
                           (System/getenv "PORT")
                           3000))]
    (init)
    (jetty/run-jetty #'app
                     {:port port
                      :join? false})))


(comment

  ;; defroutes is a macro
  (macroexpand
   '(defroutes foobar
      (compojure.core/GET "/:id" [id] (str "<p>the id is : " id "</p>"))
      (compojure.core/GET "/:other-id" [other-id] (str "<p>the other id is : " other-id "</p>"))))

  ;; defroutes creates a def with a composite 'route' that wraps over the
  ;; sequence of routes defined using the HTTP macros GET/POST etc.
  (def
    foobar
    (compojure.core/routes
     (compojure.core/GET "/:id" [id] (str "<p>the id is : " id "</p>"))
     (compojure.core/GET "/:other-id" [other-id] (str "<p>the other id is : " other-id "</p>"))))


  ;; GET is a macro, so for example...
  (macroexpand
   '(compojure.core/GET "/:id" [id] (str "<p>the id is : " id "</p>")))

  (compojure.core/make-route
   :get
   {:source "/:id", :re #"/([^/,;?]+)", :keys [:id], :absolute? false}
   (clojure.core/fn
     [request__2318__auto__]
     (compojure.core/let-request
      [[id] request__2318__auto__]
      (str "<p>the id is : " id "</p>"))))

  ;; let-request is also a macro
  (macroexpand
   '(compojure.core/let-request
     [[id] request__2318__auto__]
     (str "<p>the id is : " id "</p>")))

  (let*
      [id
       (clojure.core/get-in
        request__2318__auto__
        [:params :id]
        (clojure.core/get-in request__2318__auto__ [:params "id"]))]
    (do (str "<p>the id is : " id "</p>")))

  ;; the whole thing expands to:

  (def
    foobar
    (compojure.core/routes
     (compojure.core/make-route
      :get
      {:source "/:id", :re #"/([^/,;?]+)", :keys [:id], :absolute? false}
      (clojure.core/fn
        [request__2318__auto__]
        (let*
            [id
             (clojure.core/get-in
              request__2318__auto__
              [:params :id]
              (clojure.core/get-in request__2318__auto__ [:params "id"]))]
          (do (str "<p>the id is : " id "</p>")))))
     (compojure.core/make-route
      :get
      {:source "/:other-id", :re #"/([^/,;?]+)", :keys [:other-id], :absolute? false}
      (clojure.core/fn
        [request__2318__auto__]
        (let*
            [other-id
             (clojure.core/get-in
              request__2318__auto__
              [:params :other-id]
              (clojure.core/get-in request__2318__auto__ [:params "other-id"]))]
          (do (str "<p>the other id is : " other-id "</p>")))))))


  ;; context macro
  (macroexpand
   '(compojure.core/context
     "an/arbit/path/:company"
     (compojure.core/GET "/:id" [id] (str "<p>the id is : " id "</p>"))
     (compojure.core/GET "/:other-id" [other-id] (str "<p>the other id is : " other-id "</p>"))))

  (compojure.core/make-context
   {:source "an/arbit/path/:company:__path-info",
    :re #"an/arbit/path/([^/,;?]+)(|/.*)",
    :keys [:company :__path-info],
    :absolute? false}
   (clojure.core/fn
     [request__2400__auto__]
     (compojure.core/let-request
      [(compojure.core/GET
        "/:id"
        [id]
        (str "<p>the id is : " id "</p>"))
       request__2400__auto__]
      (compojure.core/routes
       (compojure.core/GET
        "/:other-id"
        [other-id]
        (str "<p>the other id is : " other-id "</p>"))))))
  )
