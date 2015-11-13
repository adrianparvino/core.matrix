(ns clojure.core.matrix.macros-clj
  "Namespace for core.matrix macros. Keeping them separate allows us to do conditional
  macros that can handle the differences between Clojure and Clojurescript."
  (:require [clojure.core.matrix.macros :refer [c-for]])
  (:import [java.util Arrays]))

(defmacro abutnth [i xs]
  `(let [n# (alength ~xs)
         length# (int (dec n#))
         new-xs# (Arrays/copyOf ~xs length#)]
     (c-for [j# (int ~i) (< j# (dec n#)) (inc j#)]
       (aset new-xs# (int j#) (aget ~xs (int (inc j#)))))
     new-xs#))

(defmacro areverse [xs]
  `(let [n# (alength ~xs)
         new-xs# (Arrays/copyOf ~xs (int n#))]
     (c-for [i# (int 0) (< i# (quot n# 2)) (inc i#)]
       (let [j# (- (- n# 1) i#)
             t# (aget new-xs# j#)]
         (aset new-xs# j# (aget new-xs# i#))
         (aset new-xs# i# t#)))
     new-xs#))

(def ^:dynamic *trying-current-implementation* nil)

(defmacro try-current-implementation
  [sym form]
  `(if *trying-current-implementation*
     (TODO (str "Not yet implemented: " ~(str form) " for " (class ~sym)))
     (binding [*trying-current-implementation* true]
       (let [imp# (imp/get-canonical-object)
             ~sym (mp/coerce-param imp# ~sym)]
         ~form))))

(defmacro eps== [a b eps]
  `(<= (Math/abs (- (double ~a) (double ~b))) (double ~eps) ))


