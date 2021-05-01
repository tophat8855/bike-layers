(ns bike-layers.core
  (:require [bike-layers.open-weather :as weather]))

(defn -main
  [& args]
  (prn weather/current-weather))
