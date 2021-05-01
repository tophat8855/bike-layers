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
    (let [weather                   (->Weather 40 0)
          session                   (test-session weather)
          [helmet-result]           (query session helmet?)
          [head-accessories-result] (query session head-accessories?)]
      (is (= :beanie (:?helmet helmet-result)))
      (is (= [:cycowl] (:?head-accessories head-accessories-result)))))

  (testing "when it is warmer than 50° F and colder than 55°, I should wear earmuffs and a scarf"
    (let [weather                   (->Weather 57 0)
          session                   (test-session weather)
          [helmet-result]           (query session helmet?)
          [head-accessories-result] (query session head-accessories?)
          [jacket-result]           (query session jacket?)]
      (is (= :regular (:?helmet helmet-result)))
      (is (= [:earmuffs :scarf] (:?head-accessories head-accessories-result)))
      (is (= :blue-jacket (:?jacket jacket-result)))))

  (testing "when it is warmer than 55° F and colder than 60°, I should wear earmuffs and a scarf and a blue jacket"
    (let [weather                   (->Weather 57 0)
          session                   (test-session weather)
          [helmet-result]           (query session helmet?)
          [head-accessories-result] (query session head-accessories?)
          [jacket-result]           (query session jacket?)]
      (is (= :regular (:?helmet helmet-result)))
      (is (= [:earmuffs :scarf] (:?head-accessories head-accessories-result)))
      (is (= :blue-jacket (:?jacket jacket-result)))))

  (testing "when it is warmer than 60° F and colder than 70°, I should wear earmuffs and red jacekt"
    (let [weather                   (->Weather 65 0)
          session                   (test-session weather)
          [helmet-result]           (query session helmet?)
          [head-accessories-result] (query session head-accessories?)
          [jacket-result]           (query session jacket?)]
      (is (= :regular (:?helmet helmet-result)))
      (is (= [:earmuffs] (:?head-accessories head-accessories-result)))
      (is (= :red-jacket (:?jacket jacket-result)))))

  (testing "when it is warmer than 70° F and colder than 75°, I should wear a bright shawl and straw hat"
    (let [weather         (->Weather 73 0)
          session         (test-session weather)
          [helmet-result] (query session helmet?)
          [jacket-result] (query session jacket?)]
      (is (= :straw-hat (:?helmet helmet-result)))
      (is (= :bright-shawl (:?jacket jacket-result)))))

  (testing "when it might rain, I should wear the bucket helmet and rain cape"
    (let [weather               (->Weather 65 35)
          session               (test-session weather)
          [helmet-result]       (query session helmet?)
          [torso-addons-result] (query session torso-addons?)
          [jacket-result]       (query session jacket?)]
      (is (= :bucket-hat (:?helmet helmet-result)))
      (is (= :red-jacket (:?jacket jacket-result)))
      (is (= [:rain-cape] (:?torso-addons torso-addons-result)))))

  (testing "when it is warmer than 75° F and colder than 80°, I should wear a lace poncho and straw hat"
    (let [weather         (->Weather 77 0)
          session         (test-session weather)
          [helmet-result] (query session helmet?)
          [jacket-result] (query session jacket?)]
      (is (= :straw-hat (:?helmet helmet-result)))
      (is (= :lace-poncho (:?jacket jacket-result)))))

  (testing "when it is cold and rainy, wear a jacket AND the rain cape"
    (let [weather               (->Weather 50 50)
          session               (test-session weather)
          [torso-addons-result] (query session torso-addons?)
          [jacket-result]       (query session jacket?)]
      (is (= [:rain-cape] (:?torso-addons torso-addons-result)))
      (is (= :hoodie (:?jacket jacket-result))))))
