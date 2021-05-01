(ns bike-layers.config
  (:require [environ.core :refer [env]]))

(def openweather-app-id
  (env :openweather-app-id))
