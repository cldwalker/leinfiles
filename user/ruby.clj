(ns user.ruby)

(defn split [f coll]
  "Like ruby's partition"
  [(filter f coll) (remove f coll)])

(defn has-fn?
  "Indicates if a namespace has a fn"
  ([fun] (has-fn? fun *ns*))
  ([fun nsp] (#(if (var? %) (fn? (deref %)) false) (ns-resolve nsp fun))))

(defn glob
  "Simpler version of Dir.glob" [dir regex]
  (->> (clojure.java.io/file dir) (file-seq) (map str) (filter #(re-find regex %))))

