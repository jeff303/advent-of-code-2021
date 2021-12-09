(ns jeff303.advent-of-code-2021.day5
  (:require [jeff303.advent-of-code-2021.util :as util]))

(defn- input-line->line [input-line]
  (let [[_ x1 y1 x2 y2] (re-matches #"^(\d+),(\d+) *-> *(\d+),(\d+)$" input-line)]
    [[(Long. x1) (Long. y1)] [(Long. x2) (Long. y2)]]))

(defn- line->points [[[x1 y1] [x2 y2]]]
  (let [x-comp (compare x1 x2)
        y-comp (compare y1 y2)
        x-step (- x-comp)
        y-step (- y-comp)]
    (loop [acc   []
           [x y] [x1 y1]]
      (let [next-p [(+ x x-step) (+ y y-step)]]
        (if (= [x y] [x2 y2])
          (conj acc [x y])
          (recur (conj acc [x y]) next-p))))))

(defn- ensure-col-size! [count-grid col-num max-row]
  (let [col (nth count-grid col-num)]
    (assoc! count-grid col-num (reduce (fn [col _]
                                         (conj! col 0))
                                       col
                                       (range (count col) (inc max-row))))))

(defn- ensure-grid! [line count-grid]
  (let [points  (line->points line)]
    (if points
      (let [max-col (apply max (map first points))
            max-row (apply max (map second points))]
        (loop [i 0
               c count-grid]
          (cond
            (> i max-col)
            c

            (>= i (count c))
            (recur (inc i) (conj! c (transient (into [] (repeat (inc max-row) 0)))))

            true
            (recur (inc i) (ensure-col-size! c i max-row)))))
      count-grid)))

(defn- count-line-points [line count-grid]
  (let [grid   (ensure-grid! line count-grid)
        points (line->points line)]
    (loop [g            grid
           [p & more-p] points]
      (if (nil? p)
        g
        (let [[x y]      p
              col        (nth g x)
              curr-count (nth col y)]
          (recur (assoc! g x (assoc! col y (inc curr-count)))
                 more-p))))))

(defn- run-day5-counter [input-res line-filter-fn]
  (let [lines   (mapv input-line->line (util/read-problem-input-as-lines input-res))
        grid    (transient [])
        grid    (-> (reduce (fn [g line]
                              (count-line-points line g))
                     grid
                     (if line-filter-fn
                       (filterv line-filter-fn lines)
                       lines))
                 persistent!)
        grid    (mapv persistent! grid)]
    (apply + (mapv (fn [col]
                     (count (filterv #(< 1 %) col))) grid))))

(defn day5-part1
  ([]
   (day5-part1 (util/get-day-input *ns*)))
  ([input-res]
   (run-day5-counter input-res (fn [[[x1 y1] [x2 y2]]]
                                 (or (= x1 x2) (= y1 y2))))))

(defn day5-part2
  ([]
   (day5-part2 (util/get-day-input *ns*)))
  ([input-res]
   (run-day5-counter input-res nil)))

(defn day5-part1-test []
  (day5-part1 (util/get-day-test-input *ns*)))

(defn day5-part2-test []
  (day5-part2 (util/get-day-test-input *ns*)))
