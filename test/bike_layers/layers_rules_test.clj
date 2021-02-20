(ns bike-layers.layers-rules-test
  (:require [clojure.test :refer :all]
            [bike-layers.layers-rules :refer :all]
            [clara.rules :refer :all]))

(defn test-session
  [weather test-query]
  (-> (mk-session 'bike-layers.layers-rules)
      (insert weather)
      (fire-rules)
      (query test-query)))

(deftest temperature-affects-helmet-and-neck-gear-suggestions
  (testing "when it is colder than 50° F, I should wear the cycowl and beanie helmet"
    (let [weather (->Weather 40 0)
          [result]  (test-session weather headgear?)]
      (is (= (:?helmet result) :beanie))
      (is (= (:?head-addons result) [:cycowl])))))
