(ns picture-gallery.routes.upload
  (:require [compojure.core :refer [defroutes GET POST]]
            [picture-gallery.views.upload :as vu]))

(defn handle-upload
  [params]
  (println params)
  (vu/upload-page "success"))


(defroutes upload-routes
  (GET "/upload" [info]
       (vu/upload-page info))
  (POST "/upload" {params :params}
        (handle-upload params))
  )
