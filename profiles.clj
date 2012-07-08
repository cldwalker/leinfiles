{:user
 {:repl-options
   {:init (do
     (use '[table.core :only (table)])
     (load-file (str (System/getProperty "user.home") "/.lein/user.clj")))}
  :plugins  [[lein-clojars "0.9.0"]]
  :dependencies  {table "0.2.0"}}}
