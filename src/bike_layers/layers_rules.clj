(ns bike-layers.layers-rules
  (:require [clara.rules :refer :all]))

;; Clothing items:
;; Head Gear: Beanie Helmet, Straw Hat helmet, bucket hat helmet, regular helmet
;; Heac Gear addons: Ear muffs, Cycowl, yellow/gray scarf

;; Torso: red reflective jacket, blue reflective jacket, non-bike heavier coat, bright shawl
;; Torse addons: rain-poncho

;; helmet/neck rules:
;; if it's < 50° F, cycowl and beanie
;; if it's 50 <= X < 60, ear muffs, scarf
;; if it's 60 <= X < 70,  ear muffs, jacket
;; if it's 70 <= X < 80,  bright shawl, straw hat
;; if it's 70 <= X < 80, straw hat
;; if it's 80 <= X, straw hat

;; if it's 30% chance of rain or above: poncho
;; blue reflective jacket: high 50s
;; if it's 10% rain or above: rain helmet
;; if it's dark out: blue jacket

;; only 1 helmet at a time: beanie, rain, straw, plain
;; only 1 ear thing at a time: cycowl, earmuff
;; only 1 torso warm thing at a time: non-bike heavier coat, blue jacket, red jacket, bright shawl

;;; Things to check: precipitation, temperature, daylight (later- get this going first)
;; should I add the new orange poncho to this list?

(defrecord Weather [temperature precipitation])

(defrecord HeadGear [helmet head-addons])

(defrecord TorsoGear [jacket torso-addons])

(defrule colder-than-50-wear-warmest-helmet
  "If it's cold out, wear a warm helmet, that's what it's for."
  [Weather (< temperature 50)]
  =>
  (insert! (->HeadGear :beanie [:cycowl])))

(defrule between-50-and-60-wear-earmuffs-and-scarf
  "If it's chilly out, keep your neck and ears warm"
  [Weather (and (>= temperature 50) (< temperature 60))]
  =>
  (insert! (->HeadGear :regular [:earmuffs :scarf])))

(defrule between-60-and-70-wear-earmuffs
  "If it's cool out, your ears may be nippy"
  [Weather (and (>= temperature 60) (< temperature 70))]
  =>
  (insert! (->HeadGear :regular [:earmuffs])))

(defrule between-70-and-80-wear-a-fancy-hat
  "If it's warm, you might still get a little breeze, so wear a nice shawl"
  [Weather (and (>= temperature 70) (< temperature 80))]
  =>
  (insert! (->HeadGear :straw-hat [:bright-shawl])))

(defrule over-80-keep-the-sun-out-of-your-eyes
  "The sun is bright and right in your eyes; wear a straw hat"
  [Weather (and (>= temperature 80))]
  =>
  (insert! (->HeadGear :straw-hat [])))

(defquery headgear?
  "Returns what to wear on my head"
  []
  [HeadGear (= ?helmet helmet) (= ?head-addons head-addons)])

(comment
  (println (-> (mk-session)
               (insert (->Weather 45 0))
               (fire-rules)
               (query headgear))))
