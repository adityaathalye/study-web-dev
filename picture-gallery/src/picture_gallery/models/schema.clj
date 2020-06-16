(ns picture-gallery.models.schema
  (:require [clojure.java.jdbc :as sql]
            [picture-gallery.models.db :as db]))

(defn create-users-table
  [db-conn]
  (sql/db-do-commands db-conn
                      (sql/create-table-ddl :users
                                            [[:id "varchar(32) PRIMARY KEY"]
                                             [:pass "varchar(100)"]])))


(comment
  (create-users-table db/db-conn)
  (sql/get-connection db/db-conn)
  )
