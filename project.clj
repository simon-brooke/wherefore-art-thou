(defproject wherefore-art-thou "0.1.0-SNAPSHOT"
  :description "An experiment in generating plausible-sounding names"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojars.huahaiy/symspell-clj "0.4.5"]]
   :plugins [[lein-cloverage "1.2.2"]
            [lein-codox "0.10.8"]]
  :repl-options {:init-ns wherefore-art-thou.core})
