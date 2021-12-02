(ns jeff303.advent-of-code-2021.day2
  (:require [jeff303.advent-of-code-2021.util :as util]))

(def ^:private instruction->update-fn-part1
  {"forward" (fn [acc amount]
               (update acc ::horizontal-position + amount))
   "up"      (fn [acc amount]
               (update acc ::depth - amount))
   "down"    (fn [acc amount]
               (update acc ::depth + amount))})

(defn- execute-reducer [update-fn-map acc {:keys [::instruction ::amount]}]
  (let [update-fn (partial (update-fn-map instruction))]
    (update-fn acc amount)))

(def ^:private instruction->update-fn-part2
  {"forward" (fn [{:keys [::aim] :as acc} amount]
               (-> (update acc ::horizontal-position + amount)
                   (update ::depth + (* aim amount))))
   "up"      (fn [acc amount]
               (update acc ::aim - amount))
   "down"    (fn [acc amount]
               (update acc ::aim + amount))})

(defn- execute [init-acc update-fn-map input-res]
  (let [instructions   (->> (util/read-problem-input-as-lines input-res)
                            (map (fn [^String s]
                                   (let [[_ instruction amount] (re-matches #"([^ ]*) (\d+)" s)]
                                     {::instruction instruction, ::amount (Long. amount)}))))]
    (reduce (partial execute-reducer update-fn-map)
            init-acc
            instructions)))

(defn day2-part1
  ([]
   (day2-part1 (util/get-day-input *ns*)))
  ([input-res]
   (let [{:keys [::depth ::horizontal-position]} (execute {::depth 0, ::horizontal-position 0}
                                                          instruction->update-fn-part1
                                                          input-res)]
     (* depth horizontal-position))))

(defn day2-part1-test []
  (day2-part1 (util/get-day-test-input *ns*)))

(defn day2-part2
  ([]
   (day2-part2 (util/get-day-input *ns*)))
  ([input-res]
   (let [{:keys [::depth ::horizontal-position]} (execute {::depth 0, ::horizontal-position 0, ::aim 0}
                                                          instruction->update-fn-part2
                                                          input-res)]
     (* depth horizontal-position))))

(defn day2-part2-test []
  (day2-part2 (util/get-day-test-input *ns*)))

