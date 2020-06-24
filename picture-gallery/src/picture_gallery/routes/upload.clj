(ns picture-gallery.routes.upload
  (:require [compojure.core :refer [defroutes GET POST]]
            [picture-gallery.views.upload :as vu])
  (:import [java.io File FileInputStream FileOutputStream]
           [java.awt.image AffineTransformOp BufferedImage]
           java.awt.RenderingHints
           java.awt.geom.AffineTransform
           javax.imageio.ImageIO))


(defn handle-upload
  "Process 'params' for file upload, which is of the form:
  {:file
    {:filename Clojure-icon.png,
     :content-type image/png,
     :tempfile #object[java.io.File 0x4d0cdaae /tmp/ring-multipart-2332893460722567376.tmp],
     :size 2695}}"
  [{:keys [filename] :as file}]
  (println file)
  (vu/upload-page
   (if (empty? file)
     "please select a file to upload"
     "success")))


(defroutes upload-routes
  (GET "/upload" [info]
       (vu/upload-page info))
  (POST "/upload" [file]
        (handle-upload file)))
