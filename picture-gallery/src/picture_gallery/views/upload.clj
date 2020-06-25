(ns picture-gallery.views.upload
  (:require [picture-gallery.views.layout :as layout]
            [hiccup.form :as hf]
            [hiccup.element :as he]
            [picture-gallery.views.common :as vc]))

(defn upload-page
  [& [session img error]]
  (layout/common
   (vc/top-nav session)
   [:h2 "Upload an image"]
   (cond
     error
     [:p.error error]
     img
     (he/image {:height "150px"}
               (str "galleries/" img))
     :else [:p "Please select a file to upload."])
   (hf/form-to {:enctype "multipart/form-data"}
               [:post "/upload"]
               (hf/file-upload :file)
               (hf/submit-button "upload"))))
