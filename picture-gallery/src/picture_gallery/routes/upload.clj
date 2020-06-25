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


(defn- file-path
  [& path-slugs]
  (->> path-slugs
       (interpose File/separator)
       (apply str)))


(def galleries
  (file-path "resources"
             "galleries"))


(defn image-upload-path
  ([user-id]
   (file-path galleries
              user-id))
  ([user-id file-name]
   (file-path galleries
              user-id
              file-name)))


(defn upload-or-redirect
  [{session :session}]
  (if (:user-id session)
    (vu/upload-page session)
    (response/redirect "/" :see-other)))


(defn handle-upload
  "Process 'params' for file upload, which is of the form:
  {:file
    {:filename Clojure-icon.png,
     :content-type image/png,
     :tempfile #object[java.io.File 0x4d0cdaae /tmp/ring-multipart-2332893460722567376.tmp],
     :size 2695}}"
  [{{:keys [file]} :params
    {:keys [user-id] :as session} :session}]
  (cond
    (empty? user-id)
    (response/redirect "/" :see-other)

    (empty? file)
    (vu/upload-page session
                    nil
                    "Please select a file to upload.")

    :else ;; upload to server, and render back to user
    (let [upload-to-path (image-upload-path user-id (:filename file))
          views-slug (str user-id "/"
                          (hut/url-encode (:filename file)))]
      (io/copy (:tempfile file)
               (io/file upload-to-path))
      (vu/upload-page session
                      views-slug))))


(defn serve-file
  [user-id file-name]
  (response/file-response
   (image-upload-path user-id file-name)))


(defroutes upload-routes
  (GET "/upload" request
       (upload-or-redirect request))
  (POST "/upload" request
        (handle-upload request))
  (GET "/img/:user-id/:file-name" [user-id file-name]
       (serve-file user-id file-name)))
