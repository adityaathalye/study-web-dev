(ns liberator-service.routes.home
  (:require [cheshire.core :refer [generate-string]]
            [compojure.core :refer :all]
            [liberator.core :refer [defresource request-method-in]]
            [noir.io :as io]
            [clojure.java.io :as jio]))

(def users (atom ["John" "Jane"]))


(defresource home
  :available-media-types ["text/html"]

  :exists? (fn [context]
             [(io/get-resource "/home.html")
              {::file (jio/file (str (io/resource-path) "home.html"))}])

  :handle-ok (fn [{{{resource :resource} :route-params} :request}]
               (jio/input-stream (io/get-resource "/home.html")))

  :last-modified (fn [{{{resource :resource} :route-params} :request}]
                   (.lastModified (jio/file (str (io/resource-path) "home.html")))))


(defresource get-users
  :allowed-methods [:get]
  :handle-ok (fn [_] (generate-string @users))
  :available-media-types ["application/json"])


(defresource add-user
  :allowed-methods [:post]
  :malformed? (fn [context]
                (empty?
                 (get-in context [:request :form-params "user"])))
  :handle-malformed "user name cannot be empty!"
  :post! (fn [context]
           (swap! users
                  conj
                  (get-in context [:request :form-params "user"])))
  :handle-created (fn [_] (generate-string @users))
  :available-media-types ["application/json"])


(defroutes home-routes
  (ANY "/" request home)
  (ANY "/add-user" request add-user)
  (ANY "/users" request get-users))
