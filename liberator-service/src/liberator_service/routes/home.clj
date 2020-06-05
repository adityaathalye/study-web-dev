(ns liberator-service.routes.home
  (:require [cheshire.core :refer [generate-string]]
            [compojure.core :refer :all]
            [liberator.core :refer [defresource request-method-in]]))

(def users (atom ["John" "Jane"]))


(defresource home
  :service-available? true
  :handle-service-not-available
  "service is currently unavailable..."

  :method-allowed? (request-method-in :get)
  :handle-method-not-allowed (fn [ctx]
                               (str (get-in ctx [:request :request-method])
                                    " is not allowed"))
  :handle-ok "Hello World!"
  :etag "fixed-etag"
  :available-media-types ["text/plain"])


(defresource get-users
  :allowed-methods [:get]
  :handle-ok (fn [_] (generate-string @users))
  :available-media-types ["application/json"])


(defresource add-user
  :allowed-methods [:post]
  :malformed? (fn [context]
                (empty?
                 (get-in context [:form-params :request "user"])))
  :handle-malformed "user name cannot be empty!"
  :post! (fn [context]
           (swap! users
                  conj
                  (get-in context [:form-params :request "user"])))
  :handle-created (fn [_] (generate-string @users))
  :available-media-types ["application/json"])


(defroutes home-routes
  (ANY "/" request home))
