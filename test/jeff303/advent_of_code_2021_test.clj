(ns jeff303.advent-of-code-2021-test
  (:require [clojure.test :refer :all]
            [jeff303.advent-of-code-2021.day1 :as day1]
            [jeff303.advent-of-code-2021.day2 :as day2]
            [jeff303.advent-of-code-2021.day3 :as day3]
            [jeff303.advent-of-code-2021.day4 :as day4]))

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
        (is (= 23177 (day4/day4-part2)))))))

;; TODO: make those ^ a macro?
