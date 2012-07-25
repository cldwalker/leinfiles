{:user
 {:repl-options
   {:init (do
     (use '[table.core :only (table)])
     (use '[clojure.repl :only (dir-fn)])
     (load-file (str (System/getProperty "user.home") "/.lein/user.clj")))}
    :plugins  [[lein-clojars "0.9.0"] [lein-light "0.0.4"]]
  }
  :dependencies  {table "0.2.0" com.datomic/datomic "0.1.3164" }
}
