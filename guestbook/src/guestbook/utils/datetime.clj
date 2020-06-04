(ns guestbook.utils.datetime)

(defn format-time
  [timestamp]
  (.format (new java.text.SimpleDateFormat "dd/MM/yyyy")
           timestamp))
