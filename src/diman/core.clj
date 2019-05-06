(ns diman.core
  (:require [diman.dimensions [base-dimensions standard-formula]]
            )
  )

(defn- get-base-dim [dim base]
  (if (= dim (:name base))
    (:notation base)
    nil)
  )

(defn- parse-base-dim
  ([name] (parse-base-dim name base-dimensions nil))
  ([name bdim dim]
    (if (or (empty? bdim) (some? dim))
      dim
      (recur name (drop-last bdim) (get-base-dim name (last bdim)))
      )
    )
  )

(defn- get-std-form [dim std]
  (if (= dim (:quantity std))
    (:formula std)
    nil)
  )

(defn- parse-std-form
  ([name] (parse-std-form name standard-formula nil))
  ([name sform form]
    (if (or (empty? sform) (some? form))
      form
      (recur name (drop-last sform) (get-std-form name (last sform)))
      )
    )
  )

(defn get-dim
  "Creates

  Example:
     (def varpar '((v, "velocity")(t, "time")))
     (get-dim varpar)
  "
  ([var-par] (get-dim var-par []))
  ([var-par ans]
   (if (empty? var-par)
     ans
     (if (some? (parse-base-dim (last (last var-par))))
       (recur (drop-last var-par)
              (cons {:symbol (first (last var-par))
                     :quantity (last (last var-par))
                     :dimension (parse-base-dim (last (last var-par)))} ans))
       (recur (drop-last var-par)
              (cons {:symbol (first (last var-par))
                     :quantity (last (last var-par))
                     :dimension (parse-std-form (last (last var-par)))} ans))
       )
     )
    )
  )

(defn- comparable-subcomponent [ref_component next_component]
  "Returns the sub-component in formula component which is in common with the formula with
  the most notations; returns nil if none of the notations have in common or its same."
  (let [[common]
        [(clojure.string/join
           ["[" (re-find (re-pattern
                           (clojure.string/replace ref_component #"[\]]" "]+"))
                         next_component) "]"])]]
    (if (or (= common ref_component) (= (str (second common)) "^"))
      nil
      common)
    ))

(defn foo
  ([eqn_form] (foo eqn_form
                   (clojure.string/split eqn_form #"[\+]")
                   (formula-with-most-notations (clojure.string/split eqn_form #"[\+]"))
                   ()))
  ([eqn_form lst_all_terms_form ref_form ans]
   (if (empty? lst_all_terms_form)
     ans
     )
    )
  )
