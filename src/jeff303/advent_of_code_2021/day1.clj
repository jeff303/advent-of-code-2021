(ns jeff303.advent-of-code-2021.day1
  (:require [jeff303.advent-of-code-2021.util :as util]))

(defn- count-window-sum-increases-reducer [window-size {:keys [::window ::num-increases] :as acc} n]
  (let [window-full?    (= (count window) window-size)
        next-window     (conj (if window-full? (vec (rest window)) window) n)
        safe-sum        #(apply + (filter some? %))]
    (cond-> (assoc acc ::window next-window)
      (and window-full? (< (safe-sum window) (safe-sum next-window)))
      (update ::num-increases inc))))

(defn- count-increases-reducer-old
  "Implementation for part1 before I saw part2; kept for posterity"
  [{:keys [::last-num ::num-increases] :as acc} n]
  (cond-> (assoc acc ::last-num n)
    last-num (update ::num-increases (if (< last-num n) inc identity))))

(defn- run-input-with-window-size [input-res window-size]
  (let [numbers   (->> (util/read-problem-input-as-lines input-res)
                       (map (fn [^String s]
                              (Long/parseLong s))))
        final-acc (reduce (partial count-window-sum-increases-reducer window-size)
                          {::window [], ::num-increases 0}
                          numbers)]
    (::num-increases final-acc)))

(defn day1-part1
  ([]
   (day1-part1 (util/get-day-input *ns*)))
  ([input-res]
   (run-input-with-window-size input-res 1)))

(defn day1-part2
  ([]
   (day1-part2 (util/get-day-input *ns*)))
  ([input-res]
   (run-input-with-window-size input-res 3)))

(defn day1-part1-test []
  (day1-part1 (util/get-day-test-input *ns*)))

(defn day1-part2-test []
  (day1-part2 (util/get-day-test-input *ns*)))