(ns user.table
  (:require [table.core]))

(def ^:dynamic *display* :table)

(defn display
  "Pretty prints data or returns it depending on value of *display*. Default is to print with table."
  [data & options]
  (case *display*
    :pprint (do (clojure.pprint/pprint data) (println ""))
    :self (identity data)
    (apply table.core/table data options)))

(defn pster "Print full error stack"
  ([] (pster *e))
  ([err] (->> err .getStackTrace (map clojure.repl/stack-element-str) display)))


(defn var-meta "Prints meta of a symbol" [sym]
  (display (meta (resolve sym))))

(defn vars-meta "Prints public vars for a namespace with its meta info"
  ([] (vars-meta *ns*))
  ([nsname] (->>
     (clojure.repl/dir-fn nsname)
     (map #(ns-resolve (the-ns nsname) %))
     (remove #(not (var? %)))
     (map meta) display)))

(defn var-search "Same as apropos but returns definitions by namespace"
   [str-or-pattern]
   (let [matches? (if (instance? java.util.regex.Pattern str-or-pattern)
                    #(re-find str-or-pattern  (str %))
                    #(.contains  (str %)  (str str-or-pattern)))]
     (->>
       (reduce
         #(assoc %1 %2 (vec (filter matches? (keys (ns-publics %2)))))
         {}
         (all-ns))
       (remove #(zero? (count (second %1))))
       invert-vec-map
       display)))

(defn vars-values
  "Prints dynamic vars for a namespace mapped to their values"
   [& options]
   (let [opts (apply hash-map options)]
     (apply
       display
       (cons
         ["Var" "Value"]
         (map #(identity [% (deref %)]) (ns-dynamic-vars (get opts :ns *ns*))))
       :sort true
       options)))

(defn class-paths "Prints list of class paths" []
  (display (seq (.getURLs (java.lang.ClassLoader/getSystemClassLoader)))))

(defn properties "List properties and their values" []
  (display
    (->> (System/getProperties) .stringPropertyNames
      (reduce #(assoc %1 %2 (System/getProperty %2)) {}))))

(defn envs "List of envs and their values" []
  (->> (System/getenv) keys (reduce #(assoc %1 %2 (System/getenv %2)) {}) display))


