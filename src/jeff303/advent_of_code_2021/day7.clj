(ns jeff303.advent-of-code-2021.day7
  (:require [jeff303.advent-of-code-2021.util :as util]))

(defn- cost-fn-part1 [pos1 pos2]
  (Math/abs (- pos1 pos2)))

(def cost-part2
  "This is pretty slow, so memoize it"
  (memoize (fn [difference]
             (apply + (range (inc difference))))))

(defn- cost-fn-part2 [pos1 pos2]
  (cost-part2 (Math/abs (- pos1 pos2))))

(defn- cost-for-num [target-num cost-fn nums->count]
  (reduce-kv (fn [cost n num-cnt]
               (+ cost (* (cost-fn target-num n) num-cnt)))
             0
             nums->count))

(defn- run-day-7 [input-res cost-fn]
  (let [nums-by-count     (util/input-nums->count (-> (util/read-problem-input-as-lines input-res)
                                                      first))
        [min-pos max-pos] (reduce (fn [[min-pos max-pos] pos]
                                    [(min min-pos pos) (max max-pos pos)])
                                  [Long/MAX_VALUE Long/MIN_VALUE]
                                  (keys nums-by-count))]
    (reduce (fn [min-cost target-num]
              (min min-cost (cost-for-num target-num cost-fn nums-by-count)))
     Long/MAX_VALUE
     (range min-pos (inc max-pos)))))

(defn day7-part1
  ([]
   (day7-part1 (util/get-day-input *ns*)))
  ([input-res]
   (run-day-7 input-res cost-fn-part1)))

(defn day7-part2
  ([]
   (day7-part2 (util/get-day-input *ns*)))
  ([input-res]
   (run-day-7 input-res cost-fn-part2)))

(defn day7-part1-test []
  (day7-part1 (util/get-day-test-input *ns*)))

(defn day7-part2-test []
  (day7-part2 (util/get-day-test-input *ns*)))