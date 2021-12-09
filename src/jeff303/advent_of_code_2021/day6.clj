(ns jeff303.advent-of-code-2021.day6
  (:require [jeff303.advent-of-code-2021.util :as util]
            [clojure.string :as str]))

(defn- safe-add [x y]
  (cond
    (nil? x)
    y

    (nil? y)
    x

    true
    (+ x y)))

(defn- round-reducer [acc fish-age num-fishies]
  (if (= fish-age 0)
    (-> (update acc 6 safe-add num-fishies)
        (assoc 8 num-fishies))
    (update acc (dec fish-age) safe-add num-fishies)))

(defn day6
  ([]
   (day6 80 (util/get-day-input *ns*)))
  ([rounds]
   (day6 rounds (util/get-day-input *ns*)))
  ([rounds input-res]
   (let [start-acc (util/input-nums->count (-> (util/read-problem-input-as-lines input-res)
                                               first))
         final-acc (loop [round 0
                          acc   start-acc]
                     (if (= round rounds)
                       acc
                       (recur (inc round) (reduce-kv round-reducer {} acc))))]
     (apply + (vals final-acc)))))

(defn day6-test
  ([]
   (day6 (util/get-day-test-input *ns*)))
  ([rounds]
   (day6 rounds (util/get-day-test-input *ns*))))
