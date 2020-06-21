(ns picture-gallery.views.upload
  (:require [picture-gallery.views.layout :as layout]
            [hiccup.form :as hf]))

(defn upload-page
  [info]
  (layout/common
   [:h2 "Upload an image"]
   [:p info]
   (hf/form-to {:enctype "multipart/form-data"}
               [:post "/upload"]
               (hf/file-upload :file)
               (hf/submit-button "upload"))))
