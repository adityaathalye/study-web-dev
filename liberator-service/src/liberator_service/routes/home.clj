(ns liberator-service.routes.home
  (:require [cheshire.core :refer [generate-string]]
            [compojure.core :refer :all]
            [liberator.core :refer [defresource request-method-in resource]]))

(def users (atom ["Firstame" "Lastname"]))


(defn home []
  (layout/common [:h1 "Hello World!"]))


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


(defroutes home-routes
  (ANY "/" request home))
