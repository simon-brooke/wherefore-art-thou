(ns wherefore-art-thou.core
  "Generate candidate personal names.
   
   Names are returned in all lower case, with the intention that they should 
   be capitalised at the presentation layer; this is because the culture in
   my game world creates names be compounding clan, family and personal names,
   and embedded capitals would be awkward."
  (:require [symspell-clj.core :as sp]))

(def ^:dynamic *fragments*
  "Characters and sequences of characters which can be appended to a candidate
   name. You may want to add to these if your language includes diacriticals.
   The members of this set should be strings.
   
   Specified as dynamic so that you can rebind this to suit your needs."
  (concat
   (map str
        (apply concat
               (map #(repeat %2 %1)
                    (seq "abcdefghijklmnopqrstuvwxyz")
                        ;; relative frequencies of respective characters in
                        ;; English (integer)
                    [8 2 3 4 12 2 2 6 7 1 1 4 2 6 7 2 0 6 6 9 3 1 2 1 2 1])))
   ["ch" "dh" "gh" "sh" "th" "zh"]
   ["qu"]))

(def ^:dynamic *vowels*
  "Characters considered to be vowels. You may want to add to these if your
   language includes diacriticals on vowels. The members of this set should 
   be strings.
   
   Specified as dynamic so that you can rebind this to suit your needs."
  #{"a" "e" "i" "o" "u"})

(defn vowel?
  "True if `x` is considered to be a vowel."
  [x]
  (*vowels* (str x)))

(def ^:dynamic *consonants*
  "Characters considered to be consonants. You may want to add to these if your
   language includes diacriticals on consonants. The members of this set should 
   be strings.
   
   Specified as dynamic so that you can rebind this to suit your needs."
  (set (remove vowel? *fragments*)))

(def ^:dynamic *disallowed-sequences*
  "This is very much a matter of taste; currently a list of character 
   sequences I consider unpronouncible.
   
   Specified as dynamic so that you can rebind this to suit your needs."
  #{"cb" "kq" "vp" "xw" "zb"})

(def disallowed?
  (memoize (fn [n]
             (first (filter #(.contains n %) *disallowed-sequences*)))))

(defn consonant?
  "True if `x` is considered to be a consonant."
  [x]
  (*consonants* (str x)))

(def ^:dynamic *max-name-length*
  "The maximum length of a name (excluding gender suffix if any) we will 
   generate.
   
   Specified as dynamic so that you can rebind this to suit your needs."
  9)

(def ^:dynamic *max-consecutive-consonants*
  "The maximum number of consecutive consonants in a name we will generate.
   
   Specified as dynamic so that you can rebind this to suit your needs."
  3)

(def ^:dynamic *max-consecutive-vowels*
  "The maximum number of consecutive vowels in a name we will generate.
   
   Specified as dynamic so that you can rebind this to suit your needs."
  3)
 
(defn- max-consecutive
  [name type]
  (last
   (sort
    (loop [i 0 cands '() r (seq name)]
      ;; (println (str "i: " i ", c: " c ", r: " r))
      (let [c (str (first r))]
        (cond (empty? r) (cons i cands)
              (case type
                :consonant (consonant? c)
                :vowel (vowel? c)) (recur (inc i) cands (rest r))
              :else (recur 0 (cons i cands) (rest r))))))))

(defn- gen
  "Generate a candidate name; may return nil."
  []
  (loop [name ""]
    (let [s (seq name)
          c (count name)]
      (cond (> c *max-name-length*) nil
            (> (max-consecutive name :consonant)
               *max-consecutive-consonants*) nil
            (> (max-consecutive name :vowel)
               *max-consecutive-vowels*) nil
            (disallowed? name) nil
            (and (> c 1)
                 (zero? (rand-int 3)) ;; so that we can get more than one syllable
                 (seq (filter vowel? s))
                 (consonant? (last s))) name
            :else (recur (str name (str (rand-nth *fragments*))))))))

(def sc (sp/new-spellchecker))

(defn- good-name?
  "Anything which is a recognisable word is probably not a good name (although
   there may be exceptions to this rule, it's hard to automate them)."
  [n]
  (when n
    (let [m (first (sp/lookup sc n))]
      (cond (and m (zero? (.getEditDistance m))) false
            :else true))))

(def ^:dynamic *genders*
  "Genders of characters in your game world, with name suffixes appropriate to 
   those genders.
         
   Specified as dynamic so that you can rebind this to suit your needs. If the
   cultures of your world do not use gender-specific suffixes, the `:suffixes`
   values should be `nil`. Repeated suffixes will be used more frequently."
  {:female {:suffixes ["" "a" "a" "a" "i" "iy" "iy" "y"]}
   :male {:suffixes ["" "an" "an" "en" "en" "en" "on"]}})

(defn gender-suffix
  "Return a suitable gender suffix for this `base-name` given this `gender`.
   `gender` may be a keyword, or a map which has a keyword value to the
   keyword `:gender`. Keywords which are not keys in the current binding of
   `*genders*` will be ignored. Default is no suffix."
  [base-name gender]
  (let [last-consonant? (consonant? (last (seq base-name)))
        g' (if (keyword? gender) gender (:gender gender))]
    (or
     (when last-consonant?
       (when (keyword? g')
         (rand-nth (-> *genders* g' :suffixes))))
     "")))

(defn generate
  "Generate an acceptable candidate name."
  ([]
   (first
    (filter good-name? (map (fn [_] (gen)) (range)))))
  ([gender]
   (let [base-name (generate)]
     (str base-name (gender-suffix base-name gender)))))
