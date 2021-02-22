(ns bike-layers.layers-rules-test
  (:require [clojure.test :refer :all]
            [bike-layers.layers-rules :refer :all]
            [clara.rules :refer :all]))

(defn test-session
  [weather]
  (-> (mk-session 'bike-layers.layers-rules)
      (insert weather)
      (fire-rules)))

(deftest temperature-affects-gear-suggestions
  (testing "when it is colder than 50° F, I should wear the cycowl and beanie helmet"
    (let [weather (->Weather 40 0)
          [result]  (query (test-session weather) headgear?)]
      (is (= (:?helmet result) :beanie))
      (is (= (:?head-addons result) [:cycowl]))))

  (testing "when it is warmer than 50° F and colder than 60°, I should wear earmuffs and a scarf"
    (let [weather (->Weather 55 0)
          [result]  (query (test-session weather) headgear?)]
      (is (= (:?helmet result) :regular))
      (is (= (:?head-addons result) [:earmuffs :scarf]))))

  (testing "when it is warmer than 60° F and colder than 70°, I should wear earmuffs"
    (let [weather (->Weather 65 0)
          [result]  (query (test-session weather) headgear?)]
      (is (= (:?helmet result) :regular))
      (is (= (:?head-addons result) [:earmuffs]))))

  (testing "when it is warmer than 70° F and colder than 80°, I should wear a bright shawl and straw hat"
    (let [weather (->Weather 75 0)
          [result]  (query (test-session weather) headgear?)]
      (is (= (:?helmet result) :straw-hat))
      (is (= (:?head-addons result) [:bright-shawl]))))

  (testing "when it is warmer than 80° F, keep the sun out of your eyes"
    (let [weather (->Weather 85 0)
          [result]  (query (test-session weather) headgear?)]
      (is (= (:?helmet result) :straw-hat))
      (is (= (:?head-addons result) [])))))
