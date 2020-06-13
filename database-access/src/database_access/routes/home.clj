(ns database-access.routes.home
  (:require [hiccup.element :as he]
            [ring.util.response :as response]
            [compojure.core :refer [defroutes GET]]
            [database-access.reporting :as reports]
            [database-access.views.layout :as layout]))

(defn home
  []
  (layout/common
   [:h1 "Select a report: "]
   [:ul
    [:li (he/link-to "/list" "List report")]
    [:li (he/link-to "/table" "Table report")]]))


(defn write-response
  [report-bytes]
  (with-open [in (java.io.ByteArrayInputStream. report-bytes)]
    (-> (response/response in)
        (response/header "Content-Disposition" "filename=document.pdf")
        (response/header "Content-Length" (count report-bytes))
        (response/content-type "application/pdf"))))


(defn generate-report
  [report-type]
  (try
    (let [out (new java.io.ByteArrayOutputStream)]
      (condp = (keyword report-type)
        :table (reports/table-report out)
        :list (reports/list-report out))
      (write-response (.toByteArray out)))
    (catch Exception ex
      {:status 500
       :headers {"Content-Type" "text/html"}
       :body (layout/common
              [:h2 "An error occurred while generating the report"]
              [:p.error (.getMessage ex)])})))


(defroutes home-routes
  (GET "/" [] (home))
  (GET "/:report-type" [report-type] (generate-report report-type)))
