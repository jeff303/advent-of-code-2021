(ns jeff303.advent-of-code-2021-test
  (:require [clojure.test :refer :all]
            [jeff303.advent-of-code-2021.day1 :as day1]
            [jeff303.advent-of-code-2021.day2 :as day2]
            [jeff303.advent-of-code-2021.day3 :as day3]
            [jeff303.advent-of-code-2021.day4 :as day4]
            [jeff303.advent-of-code-2021.day5 :as day5]
            [jeff303.advent-of-code-2021.day6 :as day6]
            [jeff303.advent-of-code-2021.day7 :as day7]))

(deftest day1-test
  (testing "day1"
    (testing ", part 1 works as expected"
      (testing " with sample test input"
        (is (= 7 (day1/day1-part1-test))))
      (testing " with my input"
        (is (= 1548 (day1/day1-part1)))))
    (testing ", part 2 works as expected"
      (testing " with sample test input"
        (is (= 5 (day1/day1-part2-test))))
      (testing " with my input"
        (is (= 1589 (day1/day1-part2)))))))

(deftest day2-test
  (testing "day2"
    (testing ", part 1 works as expected"
      (testing " with sample test input"
        (is (= 150 (day2/day2-part1-test))))
      (testing " with my input"
        (is (= 1499229 (day2/day2-part1)))))
    (testing ", part 2 works as expected"
      (testing " with sample test input"
        (is (= 900 (day2/day2-part2-test))))
      (testing " with my input"
        (is (= 1340836560 (day2/day2-part2)))))))

(deftest day3-test
  (testing "day3"
    (testing ", part 1 works as expected"
      (testing " with sample test input"
        (is (= 198 (day3/day3-part1-test))))
      (testing " with my input"
        (is (= 2972336 (day3/day3-part1)))))
    (testing ", part 2 works as expected"
      (testing " with sample test input"
        (is (= 230 (day3/day3-part2-test))))
      (testing " with my input"
        (is (= 3368358 (day3/day3-part2)))))))

(deftest day4-test
  (testing "day4"
    (testing ", part 1 works as expected"
      (testing " with sample test input"
        (is (= 4512 (day4/day4-part1-test))))
      (testing " with my input"
        (is (= 23177 (day4/day4-part1)))))
    (testing ", part 2 works as expected"
      (testing " with sample test input"
        (is (= 1924 (day4/day4-part2-test))))
      (testing " with my input"
        (is (= 6804 (day4/day4-part2)))))))

(deftest day5-test
  (testing "day5"
    (testing ", part 1 works as expected"
      (testing " with sample test input"
        (is (= 5 (day5/day5-part1-test))))
      (testing " with my input"
        (is (= 5167 (day5/day5-part1)))))
    (testing " updated line->points works correctly"
      (doseq [[expected line-str] [[[[5 5] [6 4] [7 3] [8 2]]
                                    "5,5 -> 8,2"]
                                   [[[9 7] [8 7] [7 7]]
                                    "9,7 -> 7,7"]]]
        (is (= expected (#'day5/line->points (#'day5/input-line->line line-str))))))
    (testing ", part 2 works as expected"
      (testing " with sample test input"
        (is (= 12 (day5/day5-part2-test))))
      (testing " with my input"
        (is (= 17604 (day5/day5-part2)))))))

(deftest day6-test
  (testing "day6"
    (testing ", part 1 works as expected"
      (testing " with sample test input"
        (testing " after 18 rounds"
          (is (= 26 (day6/day6-test 18))))
        (testing " after 80 rounds"
          (is (= 5934 (day6/day6-test 80)))))
      (testing " with my input"
        (is (= 358214 (day6/day6)))))
    (testing ", part 2 works as expected"
      (testing " with sample test input"
        (is (= 26984457539 (day6/day6-test 256))))
      (testing " with my input"
        (is (= 1622533344325 (day6/day6 256)))))))

(deftest day7-test
  (testing "day7"
    (testing ", part 1 works as expected"
      (testing " with sample test input"
        (is (= 37 (day7/day7-part1-test))))
      (testing " with my input"
        (is (= 343441 (day7/day7-part1)))))
    (testing " cost function for part 2 works as expected"
      (doseq [[expected-cost pos1 pos2] [[10 1 5]
                                         [45 14 5]
                                         [66 16 5]
                                         [0 15 15]]]
        (is (= expected-cost (#'day7/cost-fn-part2 pos1 pos2)))))
    (testing ", part 2 works as expected"
      (testing " with sample test input"
        (is (= 168 (day7/day7-part2-test))))
      (testing " with my input"
        (is (= 98925151 (day7/day7-part2)))))))

;; TODO: make those ^ a macro?
