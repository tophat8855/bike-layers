(ns bike-layers.core
  (:require [bike-layers.open-weather :as weather]
            [bike-layers.layers-rules :as rules]
            [clara.rules :refer :all]))

(defn -main
  [& args]
  (let [{:keys [temperature precipitation]} weather/current-weather
        session                             (-> (mk-session 'bike-layers.layers-rules)
                                                (insert (rules/->Weather temperature precipitation))
                                                (fire-rules))
        helmet                              (query session rules/helmet?)
        head-accessories                    (query session rules/head-accessories?)
        jacket                              (query session rules/jacket?)
        torso-addons                        (query session rules/torso-addons?)]

    (prn "Wear a " helmet ", " head-accessories ", " jacket ", and" torso-addons)))
