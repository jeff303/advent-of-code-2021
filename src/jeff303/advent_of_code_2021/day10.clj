(ns jeff303.advent-of-code-2021.day10
  (:require [jeff303.advent-of-code-2021.util :as util]))

(defn- input-line->char-seq [input-line]
  (seq input-line))

(defn- reduce-until-first-illegal-char [char-seq]
  (reduce (fn [{:keys [::stack] :as acc} c]
            (let [expect-top (case c
                               \) \(
                               \> \<
                               \} \{
                               \] \[
                               nil)]
              (if (some? expect-top)
                (if-not (= expect-top (peek stack))
                  (reduced (assoc acc ::illegal-char c))
                  (update acc ::stack pop))
                (update acc ::stack conj c))))
          {::stack []}
          char-seq))

(defn- illegal-char->score [c]
  (case c
    nil 0
    \)  3
    \]  57
    \}  1197
    \>  25137))

(defn- incomplete-stack->score [stack]
  (reduce (fn [score c]
            (-> (* 5 score)
                (+ (case c
                     \( 1
                     \[ 2
                     \{ 3
                     \< 4))))
          0
          (reverse stack)))

(defn day10-part1
  ([]
   (day10-part1 (util/get-day-input *ns*)))
  ([input-res]
   (let [input-lines (util/read-problem-input-as-lines input-res)
         char-seqs   (map input-line->char-seq input-lines)]
     (apply + (map (comp illegal-char->score ::illegal-char reduce-until-first-illegal-char) char-seqs)))))

(defn day10-part1-test []
  (day10-part1 (util/get-day-test-input *ns*)))

(defn day10-part2
  ([]
   (day10-part2 (util/get-day-input *ns*)))
  ([input-res]
   (let [input-lines  (util/read-problem-input-as-lines input-res)
         char-seqs    (map input-line->char-seq input-lines)
         final-accs   (filter #(nil? (::illegal-char %))
                              (map reduce-until-first-illegal-char char-seqs))
         final-scores (map (comp incomplete-stack->score ::stack) final-accs)]
     (-> (sort final-scores)
         vec
         (nth (/ (count final-scores) 2))))))

(defn day10-part2-test []
  (day10-part2 (util/get-day-test-input *ns*)))
