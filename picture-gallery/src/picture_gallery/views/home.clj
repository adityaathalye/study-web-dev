(ns picture-gallery.views.home
  (:require [picture-gallery.views.layout :as layout]
            [picture-gallery.views.common :as vc]))


(defn home [& [session error-msg]]
  (layout/common
   (vc/top-nav session error-msg)))
