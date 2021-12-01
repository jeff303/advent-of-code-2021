(ns jeff303.advent-of-code-2021-test
  (:require [clojure.test :refer :all]
            [jeff303.advent-of-code-2021.day1 :as day1]))

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

