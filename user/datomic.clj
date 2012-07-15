(ns user.datomic
  (:use [datomic.api :only [q] :as d]))

(def uri "datomic:mem://seattle")

; Assumes you have the datomic samples in the current directory
(defn datomic-setup []
  (let [schema-tx (read-string (slurp "samples/seattle/seattle-schema.dtm"))
        data-tx (read-string (slurp "samples/seattle/seattle-data0.dtm"))]
    (d/create-database uri)
    (def conn (d/connect uri))

    ;; submit schema and seed data
    @(d/transact conn schema-tx)
    @(d/transact conn data-tx)))

(datomic-setup)

(declare build-result)

; TODO: support multiple var names in queries and merge their values
(defn query
  ([] (query '[:find ?c :where [?c :community/name]]))
  ([dq]
    (let [db (d/db conn)
          results (q dq db)
          results (map first results)
          ; assumes :find and then another symbol i.e. :in or :where
          var-names (take-while #(not (keyword? %)) (rest dq))]
      (map #(build-result % db (first var-names)) results))))

(defn build-result [result db var-name]
  ; ids seem to be 14 digits long(er), better way to detect entity ids?
  (if (and (instance? java.lang.Long result) (> result 1e13))
    (let [entity-map (d/entity db result)
          fields (->> entity-map keys vec)]
      (->> fields (reduce #(assoc %1 %2 (%2 entity-map)) {})))
    {var-name result}))

