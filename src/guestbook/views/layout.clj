(ns guestbook.views.layout
  (:require [hiccup.core :as hc]
            [hiccup.def :as hd]
            [hiccup.element :as he]
            [hiccup.form :as hf]
            [hiccup.page :refer [html5 include-css]]))

(defn common [& body]
  (html5
    [:head
     [:title "Welcome to guestbook"]
     (include-css "/css/screen.css")]
    [:body body]))


(comment
  (hc/html
   (he/link-to {:align "left"} "https://google.com" "google"))

  (hc/html
   (hf/form-to [:post "/"]
               [:p "Name" (hf/text-field "name")]
               [:p "Message:" (hf/text-area {:rows 10 :cols 40} "message")]
               (hf/submit-button "comment")))

  (hd/defhtml page
    [& body]
    [:html
     [:head
      [:title "Welcome"]
      [:body body]]])

  (defn common
    [& body]
    (html5
     [:head
      [:title "Welcome to guestbook"]
      (include-css "/css/screen.css")]
     [:body body]))

  (common "hello")

  )
