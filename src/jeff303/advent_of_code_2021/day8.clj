(ns jeff303.advent-of-code-2021.day8
  (:require [jeff303.advent-of-code-2021.util :as util]
            [clojure.string :as str]
            [clojure.set :as set]))

(def ^:private count->possible-digits {2 #{\1}
                                       3 #{\7}
                                       4 #{\4}
                                       5 #{\2 \3 \5}
                                       6 #{\0 \6 \9}
                                       7 #{\8}})

(def ^:private char-set->digit {#{\a \b \c \e \f \g} \0
                                #{\c \f} \1
                                #{\a \c \d \e \g} \2
                                #{\a \c \d \f \g} \3
                                #{\b \c \d \f} \4
                                #{\a \b \d \f \g} \5
                                #{\a \b \d \e \f \g} \6
                                #{\a \c \f} \7
                                #{\a \b \c \d \e \f \g} \8
                                #{\a \b \c \d \f \g} \9})

(defn- input-line->entry [input-line]
  (let [[signal-pattern output-value] (str/split input-line #"[|]")
        parse-fn                      (fn [part]
                                        (str/split (str/trim part) #"  *"))]
    [(parse-fn signal-pattern) (parse-fn output-value)]))

(defn- determine-segment-cipher [signal-patterns]
  (let [final-acc     (reduce (fn [{:keys [::by-count ::by-known-digit] :as acc} signal-pattern]
                                (let [signal-chars    (into #{} signal-pattern)
                                      char-count      (count signal-chars)
                                      possible-digits (get count->possible-digits char-count)]
                                  (if (= 1 (count possible-digits))
                                    (assoc-in acc [::by-known-digit (first possible-digits)] signal-chars)
                                    (update-in acc [::by-count char-count] conj signal-chars))))
                          {::by-known-digit {}
                           ::by-count       {}}
                          signal-patterns)
        chars-for     #(get-in final-acc [::by-known-digit %])
        unique-from   #(first (set/difference (chars-for %1) (chars-for %2)))
        a-char        (unique-from \7 \1)
        digit-1-chars (chars-for \1)
        b-and-d       (set/difference (chars-for \4) digit-1-chars)
        length-5-opts (get-in final-acc [::by-count 5])
        contains-all? (fn [chars* option]
                        (= (count chars*) (count (set/intersection chars* option))))
        digit-5-chars (first (filter (partial contains-all? b-and-d)
                                     length-5-opts))
        digit-7-chars (chars-for \7)
        g-char        (first (set/difference digit-5-chars b-and-d digit-7-chars))

        e-char        (first (set/difference (chars-for \8) #{g-char} b-and-d digit-7-chars))
        digit-2-chars (first (filter (partial contains-all? #{e-char g-char})
                                     length-5-opts))
        c-char        (first (set/intersection digit-2-chars digit-1-chars))
        d-char        (first (set/intersection digit-2-chars b-and-d))
        b-char        (first (disj b-and-d d-char))
        f-char        (first (disj digit-1-chars c-char))]
    {\a a-char
     \b b-char
     \c c-char
     \d d-char
     \e e-char
     \f f-char
     \g g-char}))

(defn day8-part1
  ([]
   (day8-part1 (util/get-day-input *ns*)))
  ([input-res]
   (let [input-lines    (util/read-problem-input-as-lines input-res)
         entries        (mapv input-line->entry input-lines)
         certain-counts (mapv (fn [[_ output-values]]
                                ;; very lazy/hacky approach for part 1
                                (count (filterv #(contains? #{2 3 4 7}
                                                            (count %))
                                                output-values)))
                              entries)]
     (apply + certain-counts))))

(defn day8-part2
  ([]
   (day8-part2 (util/get-day-input *ns*)))
  ([input-res]
   (let [input-lines    (util/read-problem-input-as-lines input-res)
         entries        (mapv input-line->entry input-lines)
         counts         (mapv (fn [[signal-patterns output-values]]
                                (let [cipher        (determine-segment-cipher signal-patterns)
                                      output->digit (comp char-set->digit set #(map (set/map-invert cipher) %) seq)]
                                  (Long. (apply str (map output->digit output-values)))))
                          entries)]
     (apply + counts))))

(defn day8-part1-test []
  (day8-part1 (util/get-day-test-input *ns*)))

(defn day8-part2-test []
  (day8-part2 (util/get-day-test-input *ns*)))