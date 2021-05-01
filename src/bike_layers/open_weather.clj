(ns bike-layers.open-weather
  (:require [bike-layers.config :as config]
            [clj-http.client :as http]
            [clojure.data.json :as json]))

(def current-weather
  (let [{:strs [current hourly]} (-> (http/get "https://api.openweathermap.org/data/2.5/onecall"
                                               {:query-params {:units   "imperial"
                                                               :lat     "45.8492"
                                                               :lon     "-84.6189"
                                                               :exclude "minutely,daily,alerts"
                                                               :appid   config/openweather-app-id}})
                                     :body
                                     json/read-str)]
    {:precipitation (get (first hourly) "pop")
     :temperature   (get current "temp")}))
