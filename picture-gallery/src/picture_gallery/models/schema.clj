(ns picture-gallery.models.schema
  (:require [clojure.java.jdbc :as sql]
            [picture-gallery.models.db :as db]))

(defn create-users-table
  []
  (sql/with-db-connection [db db/db]
    (sql/db-do-commands db
     (sql/create-table-ddl :users
                           [[:id "varchar(32) PRIMARY KEY"]
                            [:pass "varchar(100)"]]))))

(comment
  (create-users-table)
  )
