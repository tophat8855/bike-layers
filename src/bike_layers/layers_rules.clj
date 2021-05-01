(ns bike-layers.layers-rules
  (:require [clara.rules :refer :all]
            [clara.rules.accumulators :as acc]))

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
;; red reflective jacket: 60s
;; bright shawl: 70-75
;; orange shawl: 75-80

;; if it's 10% rain or above: rain helmet
;; if it's dark out: blue jacket

;; Things to check: precipitation, temperature, daylight (later- get this going first)

;; I'd really like the rain helmet to override every other helmet in a clear way
;; right now, it's just explicit about precification

(defrecord Weather [temperature precipitation])

(defrecord Helmet [helmet])

(defrecord HeadAccessories [head-accessories])

(defrecord Jacket [jacket])

(defrecord TorsoAddons [torso-addons])

;; TODO: Make bucket helmet win over other helmets without it needing to be
;; above all the other helmet rules. In this case, order is important for it
;; win out over any other helmet, but we should be able to accomplish this in code,
;; not with the ordering of the functions in the namespace
(defrule precipitation-more-than-20%-chance
  "keep your head dry"
  [Weather (>= precipitation 20)]
  =>
  (insert! (->Helmet :bucket-hat)))

(defrule precipitation-more-than-20%-chance
  "keep your head dry"
  [Weather (>= precipitation 20)]
  =>
  (insert! (->Helmet :bucket-hat)))
(defrule colder-than-50-wear-warmest-helmet
  "If it's cold out, wear a warm helmet, that's what it's for."
  [Weather (< temperature 50)]
  =>
  (insert-all! [(->Helmet :beanie)
                (->HeadAccessories [:cycowl])]))

(defrule between-50-and-60-wear-earmuffs-and-scarf
  "If it's chilly out, keep your neck and ears warm"
  [Weather (and (>= temperature 50)
                (< temperature 60))]
  =>
  (insert-all! [(->Helmet :regular)
                (->HeadAccessories [:earmuffs :scarf])]))

(defrule between-60-and-70-wear-earmuffs-and-red-jacket
  "If it's cool out, your ears may be nippy"
  [Weather (and (>= temperature 60)
                (< temperature 70))]
  =>
  (insert-all! [(->Helmet :regular)
                (->HeadAccessories [:earmuffs])
                (->Jacket :red-jacket)]))

(defrule between-70-and-80-wear-a-fancy-hat
  "If it's warm, you might still get a little breeze, so wear a nice shawl"
  [Weather (>= temperature 70)]
  =>
  (insert! (->Helmet :straw-hat)))

(defrule under-55-wear-a-hoodie-jacket
  "this jacket has a couple of layers and will keep you wam"
  [Weather (and (< temperature 55))]
  =>
  (insert! (->Jacket :hoodie)))

(defrule between-55-and-60-wear-blue-jacket
  "this jacket has a couple of layers and will keep you wam"
  [Weather (and (>= temperature 55)
                (< temperature 60))]
  =>
  (insert! (->Jacket :blue-jacket)))

(defrule between-70-and-75-wear-bright-shawl
  "Bright Shawl will keep you warm if there is a cool breeze"
  [Weather (and (>= temperature 70)
                (< temperature 75))]
  =>
  (insert! (->Jacket :bright-shawl)))

(defrule between-75-and-80-wear-bright-shawl
  "cool and breezy and keeps the sun off"
  [Weather (and (>= temperature 75)
                (< temperature 80))]
  =>
  (insert! (->Jacket :lace-poncho)))

(defrule precipitation-more-than-30%-chance
  "time for the rain cape"
  [Weather (>= precipitation 30)]
  =>
  (insert! (->TorsoAddons [:rain-cape])))

(defquery helmet?
  "Returns what helmet to wear"
  []
  [Helmet (= ?helmet helmet)])

(defquery head-accessories?
  "Returns what to wear on my head and neck"
  []
  [HeadAccessories (= ?head-accessories head-accessories)])

(defquery jacket?
  "Returns what to wear on my torso"
  []
  [Jacket (= ?jacket jacket)])

(defquery torso-addons?
  "Returns what to wear on my torso"
  []
  [TorsoAddons (= ?torso-addons torso-addons)])
