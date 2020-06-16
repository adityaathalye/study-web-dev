(defproject picture-gallery "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [hiccup "1.0.5"]
                 [ring-server "0.5.0"]
                 [org.clojure/java.jdbc "0.7.11"]
                 ;; https://mvnrepository.com/artifact/org.postgresql/postgresql
                 [org.postgresql/postgresql "42.2.14"]
                 ;; https://www.mchange.com/projects/c3p0/#basics
                 [com.mchange/c3p0 "0.9.5.5"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler picture-gallery.handler/app
         :init picture-gallery.handler/init
         :destroy picture-gallery.handler/destroy}
  :profiles
  {:uberjar {:aot :all}
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring/ring-mock "0.4.0"] [ring/ring-devel "1.7.1"]]}})
