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
    (let [weather  (->Weather 40 0)
          [result] (query (test-session weather) headgear?)]
      (is (= (:?helmet result) :beanie))
      (is (= (:?head-addons result) [:cycowl]))))

  (testing "when it is warmer than 50° F and colder than 55°, I should wear earmuffs and a scarf"
    (let [weather        (->Weather 57 0)
          session        (test-session weather)
          [head-result]  (query session headgear?)
          [torso-result] (query session torsogear?)]
      (is (= (:?helmet head-result) :regular))
      (is (= (:?head-addons head-result) [:earmuffs :scarf]))
      (is (= (:?jacket torso-result) :blue-jacket))))

  (testing "when it is warmer than 55° F and colder than 60°, I should wear earmuffs and a scarf and a blue jacket"
    (let [weather        (->Weather 57 0)
          session        (test-session weather)
          [head-result]  (query session headgear?)
          [torso-result] (query session torsogear?)]
      (is (= (:?helmet head-result) :regular))
      (is (= (:?head-addons head-result) [:earmuffs :scarf]))
      (is (= (:?jacket torso-result) :blue-jacket))))

  (testing "when it is warmer than 60° F and colder than 70°, I should wear earmuffs and red jacekt"
    (let [weather        (->Weather 65 0)
          session        (test-session weather)
          [head-result]  (query session headgear?)
          [torso-result] (query session torsogear?)]
      (is (= (:?helmet head-result) :regular))
      (is (= (:?head-addons head-result) [:earmuffs]))
      (is (= (:?jacket torso-result) :red-jacket))))

  (testing "when it is warmer than 70° F and colder than 75°, I should wear a bright shawl and straw hat"
    (let [weather        (->Weather 73 0)
          [head-result]  (query (test-session weather) headgear?)
          [torso-result] (query (test-session weather) torsogear?)]
      (is (= (:?helmet head-result) :straw-hat))
      (is (= (:?jacket torso-result) :bright-shawl))))

  (testing "when it is warmer than 75° F and colder than 80°, I should wear a lace poncho and straw hat"
    (let [weather        (->Weather 77 0)
          session        (test-session weather)
          [head-result]  (query session headgear?)
          [torso-result] (query session torsogear?)]
      (is (= (:?helmet head-result) :straw-hat))
      (is (= (:?jacket torso-result) :lace-poncho))))

