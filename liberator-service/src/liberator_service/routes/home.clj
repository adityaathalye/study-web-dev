(ns liberator-service.routes.home
  (:require [compojure.core :refer :all]
            [liberator.core :refer [defresource resource request-method-in]]))

(defn home []
  (layout/common [:h1 "Hello World!"]))

(defroutes home-routes
  (ANY "/" request
       (resource
        :handle-ok "Hello World!!!"
        :etag "fixed-etag"
        :available-media-types ["text/plain"])))
