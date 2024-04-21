(defproject wherefore-art-thou "0.1.0-SNAPSHOT"
  :description "An experiment in generating plausible-sounding names"
  :url "http://example.com/FIXME"
  :license {:name "GNU General Public License,version 2.0 or (at your option) any later version"
            :url "https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojars.huahaiy/symspell-clj "0.4.5"]]
  :plugins [[lein-cloverage "1.2.2"]
            [lein-codox "0.10.8"]]
  :repl-options {:init-ns cc.journeyman.wherefore-art-thou.core})
