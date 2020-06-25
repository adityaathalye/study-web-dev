(ns picture-gallery.routes.upload
  (:require [compojure.core :refer [defroutes GET POST]]
            [hiccup.util :as hut]
            [picture-gallery.views.upload :as vu]
            [ring.util.response :as response]
            [clojure.java.io :as io])
  (:import [java.io File FileInputStream FileOutputStream]
           [java.awt.image AffineTransformOp BufferedImage]
           java.awt.RenderingHints
           java.awt.geom.AffineTransform
           javax.imageio.ImageIO))


(defn upload-or-redirect
  [{session :session}]
  (if (:user-id session)
    (vu/upload-page)
    (response/redirect "/" :see-other)))

(defn handle-upload
  "Process 'params' for file upload, which is of the form:
  {:file
    {:filename Clojure-icon.png,
     :content-type image/png,
     :tempfile #object[java.io.File 0x4d0cdaae /tmp/ring-multipart-2332893460722567376.tmp],
     :size 2695}}"
  [{:keys [filename tempfile] :as file}]
  (println file)
  (if (empty? file)
    (vu/upload-page)
    (do (io/copy tempfile (io/file "resources" "galleries" filename))
      (vu/upload-page (hut/url-encode filename)))))


(defn serve-file
  [file-name]
  (response/file-response
   (str "resources"
        File/separator
        "galleries"
        File/separator
        file-name)))


(defroutes upload-routes
  (GET "/upload" request
       (upload-or-redirect request))
  (POST "/upload" [file]
        (handle-upload file))
  (GET "img/:file-name" [file-name]
       (serve-file file-name)))
