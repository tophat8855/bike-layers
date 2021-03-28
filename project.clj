(defproject bike-layers "0.1.0-SNAPSHOT"
  :description "an app to determine what to wear for my bike ride"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [com.cerner/clara-rules "0.21.0"]]
  :plugins [[lein-cljfmt "0.7.0"]]
  :main ^:skip-aot bike-layers.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
