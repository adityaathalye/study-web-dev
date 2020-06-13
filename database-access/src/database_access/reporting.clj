(ns database-access.reporting
  (:require [clj-pdf.core :as pdf]
            [clojure.java.jdbc :as sql]
            [database-access.core :as dc]))

(defn create-employee-table
  []
  (sql/create-table
   :employee
   [:name "varchar(50)"]
   [:occupation "varchar(50)"]
   [:place "varchar(50)"]
   [:country "varchar(50)"]))

(comment
 (sql/with-connection
   dc/db
   (create-employee-table)))

(comment
  (sql/with-connection
    dc/db
    (sql/insert-rows
     :employee
     ["Albert Einstein", "Engineer", "Ulm", "Germany"]
     ["Alfred Hitchcock", "Movie Director", "London", "UK"]
     ["Wernher Von Braun", "Rocket Scientist", "Wyrzysk", "Poland"]
     ["Sigmund Freud", "Neurologist", "Pribor", "Czech Republic"]
     ["Mahatma Gandhi", "Lawyer", "Gujarat", "India"]
     ["Sachin Tendulkar", "Cricket Player", "Mumbai", "India"]
     ["Michael Schumacher", "F1 Racer", "Cologne", "Germany"])))


(defn read-employees
  []
  (sql/with-connection
    dc/db
    (sql/with-query-results
      rs
      ["select * from employee"]
      (doall rs))))

(comment
  (read-employees)
  )

;; Try out PDF reporting
(comment

  (pdf/pdf
   [{:header "Wow that was easy"}
    [:list
     [:chunk {:style :bold} "a bold item"]
     "another item"
     "yet another item"]
    [:paragraph "I'm a paragraph"]]
   "doc.pdf"))


(def employee-template
  (pdf/template [$name $occupation $place $country]))

(comment
  (employee-template
   (read-employees))

  (pdf/pdf
   [{:header "Employee List"}
    (into [:table
           {:border true
            :cell-border true
            :header [{:background-color [0 204 0]}
                      "Name" "Occupation" "Place" "Country"]}]
          (employee-template
           (read-employees)))]
   "report.pdf")
  )


(def employee-template-paragraph
  (pdf/template
   [:paragraph
    [:heading {:style {:size 15}} $name]
    [:chunk {:style :bold} "occupation: "] $occupation "\n"
    [:chunk {:style :bold} "place: "] $place "\n"
    [:chunk {:style :bold} "country: "] $country
    [:spacer]]))


(comment
  (pdf/pdf
   [{}
    [:heading {:size 10} "Employees"]
    [:line]
    [:spacer]
    (employee-template-paragraph
     (read-employees))]
   "report.pdf")
  )


(defn table-report
  [out]
  (pdf/pdf
   [{:header "Employee List"}
    (into [:table
           {:border false
            :cell-border false
            :header [{:background-color [0 150 150]}
                     "Name" "Occupation" "Place" "Country"]}]
          (employee-template (read-employees)))]
   out))


(defn list-report
  [out]
  (pdf/pdf
   [{}
    [:heading {:size 10} "Employees"]
    [:line]
    [:spacer]
    (employee-template-paragraph
     (read-employees))]
   out))
