(ns jeff303.advent-of-code-2021.day4
  (:require [jeff303.advent-of-code-2021.util :as util]
            [clojure.string :as str]
            [clojure.set :as set]))

(defn- lines->board-acc [lines]
  (let [board-lines (str/split-lines lines)
        board       (mapv (comp (fn [line]
                                  (let [cells (str/split line #"  *")]
                                    (mapv #(Long. %) cells)))
                              str/trim)
                        board-lines)
        num->pos    (apply merge (map-indexed (fn [row-num row]
                                                (apply merge (map-indexed (fn [col-num cell]
                                                                            {cell [row-num col-num]}) row))) board))]
    {::num->pos num->pos, ::covered-pos #{}, ::board board,
     ::num-rows (count board), ::num-cols (count (first board))}))

(defn- board-acc->uncovered-pos-sum [{:keys [::num->pos ::num-rows ::board ::num-cols ::covered-pos] :as board-acc}]
  (apply + (keep-indexed (fn [row-num row]
                           (apply + (keep-indexed (fn [col-num v]
                                                    (when-not (contains? covered-pos [row-num col-num])
                                                      v)) row)))
             board)))

(defn- spot->row [[r _] num-cols]
  (mapv (fn [col-num]
          [r col-num]) (range 0 num-cols)))

(defn- spot->col [[_ c] num-rows]
  (mapv (fn [row-num]
          [row-num c]) (range 0 num-rows)))

(defn- apply-num-to-board-acc [[idx n] {:keys [::num->pos ::num-rows ::num-cols ::bingo? ::covered-pos] :as board-acc}]
  (if (contains? num->pos n)
    (let [hit-pos       (get num->pos n)
          row-positions (spot->row hit-pos num-cols)
          col-positions (spot->col hit-pos num-rows)
          now-covered   (conj covered-pos hit-pos)]
      (cond-> board-acc
        (and (nil? bingo?) (or (every? (partial contains? now-covered) col-positions)
                               (every? (partial contains? now-covered) row-positions)))
        (assoc ::bingo? true, ::bingo-round idx)

        true
        (assoc ::covered-pos now-covered, ::last-num n)))

    board-acc))

(defn- find-winner-part1 [all-board-accs _]
  (first (filter ::bingo? all-board-accs)))

(defn- find-winner-part2 [all-board-accs idx]
  (when (every? ::bingo? all-board-accs)
    (first (filter #(= idx (::bingo-round %)) all-board-accs))))

(defn- winning-board-reducer [find-winner-fn {:keys [::all-board-accs] :as acc} [idx next-num]]
  (let [new-boards (map (partial apply-num-to-board-acc [idx next-num]) all-board-accs)
        winning-board (find-winner-fn new-boards idx)]
    (if winning-board
      (reduced {::all-board-accs [winning-board]})
      (assoc acc ::all-board-accs new-boards))))

(defn- run-reducer [filter-fn input-res]
  (let [[nums & boards] (util/read-problem-input-split-by input-res #"\n\n")
        board-accs      (mapv lines->board-acc boards)
        nums            (map #(Long. %) (str/split nums #","))
        winning-board   (-> (reduce (partial winning-board-reducer filter-fn)
                                    {::all-board-accs board-accs}
                                    (map vector (range (count nums)) nums))
                            ::all-board-accs
                            first)
        uncovered-sum   (board-acc->uncovered-pos-sum winning-board)
        last-num-called (::last-num winning-board)]
    (* uncovered-sum last-num-called)))

(defn day4-part1
  ([]
   (day4-part1 (util/get-day-input *ns*)))
  ([input-res]
   (run-reducer find-winner-part1 input-res)))

(defn day4-part2
  ([]
   (day4-part2 (util/get-day-input *ns*)))
  ([input-res]
   (run-reducer find-winner-part2 input-res)))

(defn day4-part1-test []
  (day4-part1 (util/get-day-test-input *ns*)))

(defn day4-part2-test []
  (day4-part2 (util/get-day-test-input *ns*)))