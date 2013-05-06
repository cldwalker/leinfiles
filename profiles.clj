{
 :user {
   :plugins [
             ;[lein-clojars "0.9.0"]
             ;[lein-light "0.0.4"]
             [lein-difftest "2.0.0"]
             [lein-grep "0.1.0"]
             [lein-open "0.1.0"]
             [lein-outdated "1.0.0"]
             [lein-exec "0.3.0"]
             ;[lein-pprint "1.1.1"]
             ]
   :repl-options {
     :init (do
       (use '[table.core :only (table)])
       (use 'desc.core)
       (use 'rubydoc.core)
       (use '[clojure.repl :only (dir-fn)])
       (load-file (str (System/getProperty "user.home") "/.lein/user.clj"))) }
   :dependencies [[table "0.4.0"]
                  [desc "0.1.0"]
                  [rubydoc "0.4.0"]]
 }
}
