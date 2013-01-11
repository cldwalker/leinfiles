{
 :user {
   :plugins [
             ;[lein-clojars "0.9.0"]
             ;[lein-light "0.0.4"]
             [lein-deps-tree "0.1.2"]
             ]
   :repl-options {
     :init (do
       (use '[table.core :only (table)])
       (use 'desc.core)
       (use 'rubydoc.core)
       (use '[clojure.repl :only (dir-fn)])
       (load-file (str (System/getProperty "user.home") "/.lein/user.clj"))) }
   :dependencies [[table "0.3.2"] [desc "0.1.0"]
                  [rubydoc "0.3.0"]]
 }
}
