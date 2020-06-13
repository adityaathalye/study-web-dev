(ns database-access.views.layout
  (:require [hiccup.page :as hp]))


(defn common [& body]
  (hp/html5
   [:head
    [:title "Welcome to PDF reports"]
    (hp/include-css "/css/screen.css")]
   [:body body]))
