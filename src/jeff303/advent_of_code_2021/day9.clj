(ns jeff303.advent-of-code-2021.day9
  (:require [jeff303.advent-of-code-2021.util :as util]
            [clojure.string :as str]
            [clojure.set :as set]))

(defn- input-line->row [input-line]
  (mapv (comp #(Long/parseLong %) str) (seq input-line)))

(defn- input-lines->grid [input-lines]
  (mapv input-line->row input-lines))

(defn- grid->transient [grid]
  (transient (mapv transient grid)))

(defn- transient-grid->persisted [transient-grid]
  (mapv persistent! (persistent! transient-grid)))

(defn- find-neighbors [num-rows num-cols row-idx col-idx]
  (cond-> #{}
    (< row-idx (dec num-rows)) ; down
    (conj [(inc row-idx) col-idx])

    (> row-idx 0) ; up
    (conj [(dec row-idx) col-idx])

    (< col-idx (dec num-cols)) ; right
    (conj [row-idx (inc col-idx)])

    (> col-idx 0) ; left
    (conj [row-idx (dec col-idx)])))

(defn- find-row-low-points [grid num-rows num-cols row-num row]
  (->> (map-indexed (fn [col-num cell]
                      (let [neighbors (find-neighbors num-rows num-cols row-num col-num)]
                        (when (every? true? (map (fn [[r c]]
                                                   (let [neighbor-val (nth (nth grid r) c)]
                                                     (> neighbor-val cell)))
                                                 neighbors))
                          [row-num col-num (inc cell)])))
                    row)
       (filter some?)))

(defn- find-low-points [grid]
  (let [num-rows    (count grid)
        num-cols    (count (first grid))
        low-points  (apply concat (map-indexed (partial find-row-low-points grid num-rows num-cols) grid))]
    (apply + (map last low-points))))

(defn- covered? [search-grid row-idx col-idx]
  (true? (nth (nth search-grid row-idx) col-idx)))

(defn- find-basin-neighbors [search-grid num-rows num-cols [row-idx col-idx]]
  (let [all-neighbors (find-neighbors num-rows num-cols row-idx col-idx)]
    (filter (fn [[r c]]
              (false? (nth (nth search-grid r) c)))
            all-neighbors)))

(defn- cover [search-grid row-idx col-idx]
  (let [updated-row (assoc! (nth search-grid row-idx) col-idx true)]
    (assoc! search-grid row-idx updated-row)))

(defn- cover-all [search-grid points]
  (loop [search-grid*   search-grid
         [[r c] & more] points]
    (if (nil? r)
      search-grid*
      (recur (cover search-grid* r c)
        more))))

(defn- search-grid->str [search-grid]
  (let [row-count (count search-grid)
        col-count (count (nth search-grid 0))]
    (str/join "\n"
              (map (fn [row-idx]
                     (str/join ", "
                               (map (fn [col-idx]
                                      (nth (nth search-grid row-idx) col-idx))
                                    (range col-count))))
                   (range row-count)))))

(defn- find-basins [grid]
  (let [num-rows    (count grid)
        num-cols    (count (first grid))
        search-grid (-> (mapv (fn [row]
                                (mapv (fn [cell]
                                        (= 9 cell)) row))
                              grid)
                        grid->transient)
        final-acc   (reduce (fn [{:keys [::curr-search-grid ::all-basin-sizes] :as acc} [r c]]
                              (if (covered? curr-search-grid r c)
                                acc
                                (loop [next-set     #{[r c]}
                                       search-grid* (cover search-grid r c)
                                       basin-size   1]
                                  (if (empty? next-set)
                                    (-> (update acc ::all-basin-sizes conj basin-size)
                                        (assoc ::curr-search-grid search-grid*))
                                    (let [next-search-grid (cover-all search-grid* next-set)]
                                      (let [neighbors (->> (map (partial find-basin-neighbors
                                                                         next-search-grid
                                                                         num-rows
                                                                         num-cols)
                                                                next-set)
                                                           (apply concat)
                                                           set)]
                                        (recur neighbors
                                               next-search-grid
                                               (+ basin-size (count neighbors)))))))))
                            {::curr-search-grid search-grid
                             ::all-basin-sizes  []}
                            (for [r (range num-rows)
                                  c (range num-cols)]
                              [r c]))]
    (::all-basin-sizes final-acc)))

(defn day9-part1
  ([]
   (day9-part1 (util/get-day-input *ns*)))
  ([input-res]
   (let [input-lines (util/read-problem-input-as-lines input-res)
         grid        (input-lines->grid input-lines)]
     (find-low-points grid))))

(defn day9-part1-test []
  (day9-part1 (util/get-day-test-input *ns*)))

(defn day9-part2
  ([]
   (day9-part2 (util/get-day-input *ns*)))
  ([input-res]
   (let [input-lines (util/read-problem-input-as-lines input-res)
         grid        (input-lines->grid input-lines)
         all-basins  (find-basins grid)]
     (->> (sort all-basins)
          reverse
          (take 3)
          (apply *)))))

(defn day9-part2-test []
  (day9-part2 (util/get-day-test-input *ns*)))
