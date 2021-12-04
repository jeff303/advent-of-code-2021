(ns jeff303.advent-of-code-2021.day3
  (:require [jeff303.advent-of-code-2021.util :as util]))

(defn- filter-at-position [pos bit-char char-vectors]
  (filterv (fn [char-vec]
             (= bit-char (nth char-vec pos)))
           char-vectors))

(defn- count-at-position [pos bit-char char-vectors]
  (count (filter-at-position pos bit-char char-vectors)))

(defn- char-vec->int [char-vec]
  (Integer/parseInt (apply str char-vec) 2))

(defn- run-reducer [update-acc-fn init-acc char-vectors-fn char-vec-len]
  (reduce (fn [acc i]
            (let [count-0s (count-at-position i \0 (char-vectors-fn acc))
                  count-1s (count-at-position i \1 (char-vectors-fn acc))]
              (update-acc-fn acc i count-0s count-1s)))
    init-acc
    (range char-vec-len)))

(defn- update-acc-part1 [acc _ count-0s count-1s]
  (let [more-1s? (> count-1s count-0s)]
    (-> (update acc ::gamma-bits conj (if more-1s? \1 \0))
        (update ::epsilon-bits conj (if more-1s? \0 \1)))))

(defn- part2-reducer [keep-char-fn]
  (fn [{:keys [::numbers] :as acc} i count-0s count-1s]
    (if (= 1 (count numbers))
        (reduced acc)
        (let [filtered (filter-at-position i (keep-char-fn count-0s count-1s) numbers)]
          (assoc acc ::numbers filtered)))))

(def ^:private find-oxygen-generator-rating-reducer (part2-reducer (fn [count-0s count-1s]
                                                                     (cond (< count-0s count-1s)
                                                                           \1

                                                                           (= count-0s count-1s)
                                                                           \1

                                                                           (> count-0s count-1s)
                                                                           \0))))

(def ^:private find-co2-scrubber-rating-reducer (part2-reducer (fn [count-0s count-1s]
                                                                 (cond (< count-0s count-1s)
                                                                       \0

                                                                       (= count-0s count-1s)
                                                                       \0

                                                                       (> count-0s count-1s)
                                                                       \1))))

(defn day3-part1
  ([]
   (day3-part1 (util/get-day-input *ns*)))
  ([input-res]
   (let [char-vectors (map seq (util/read-problem-input-as-lines input-res))
         char-vec-len (count (first char-vectors))
         final-acc    (run-reducer update-acc-part1
                                   {::epsilon-bits [] ::gamma-bits [] ::numbers char-vectors}
                                   (constantly char-vectors) ; part 1 always uses the same numbers
                                   char-vec-len)
         gamma-rate   (char-vec->int (::gamma-bits final-acc))
         epsilon-rate (char-vec->int (::epsilon-bits final-acc))]
     (* gamma-rate epsilon-rate))))

(defn day3-part2
  ([]
   (day3-part2 (util/get-day-input *ns*)))
  ([input-res]
   (let [char-vectors  (map seq (util/read-problem-input-as-lines input-res))
         char-vec-len  (count (first char-vectors))
         oxy-gen-rtg   (-> (run-reducer find-oxygen-generator-rating-reducer
                                        {::numbers char-vectors}
                                        ::numbers
                                        char-vec-len)
                           ::numbers
                           first)
         co2-scrb-rtg  (-> (run-reducer find-co2-scrubber-rating-reducer
                                        {::numbers char-vectors}
                                        ::numbers
                                        char-vec-len)
                           ::numbers
                           first)]
     (* (char-vec->int oxy-gen-rtg) (char-vec->int co2-scrb-rtg)))))

(defn day3-part1-test []
  (day3-part1 (util/get-day-test-input *ns*)))

(defn day3-part2-test []
  (day3-part2 (util/get-day-test-input *ns*)))

