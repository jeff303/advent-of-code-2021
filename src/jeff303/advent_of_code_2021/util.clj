(ns jeff303.advent-of-code-2021.util
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn read-problem-input-as-lines
  [resource-file]
  (with-open [rdr (-> resource-file io/resource io/reader)]
    (reduce conj [] (line-seq rdr))))

(defn read-problem-input-split-by
  [resource-file split-by]
  (with-open [rdr (-> resource-file io/resource io/reader)]
    (str/split (slurp rdr) split-by)))

(defn get-day [ns-sym]
  (-> ns-sym
      (resolve)
      (var-get)
      (ns-name)
      (name)
      (str/split #"\.")
      (last)))

(defmacro get-day-input [ns-sym]
  (let [day (get-day ns-sym)]
    (format "input/%s.txt" day)))

(defmacro get-day-test-input
  ([ns-sym]
   `(get-day-test-input ~ns-sym ""))
  ([ns-sym variant]
   (let [day (get-day ns-sym)]
     `(format "test-input/%s%s.txt" ~day ~variant))))

(defn input-nums->count
  "Expects `input-line` to be a comma separated string of numbers. Returns a map of each value to its count (number
  of occurrences in the input."
  [input-line]
  (let [nums (mapv #(Long. %) (str/split input-line #","))]
    (reduce (fn [acc n]
              (if (contains? acc n)
                (update acc n inc)
                (assoc acc n 1)))
      {}
      nums)))
