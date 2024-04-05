# wherefore-art-thou

*"A rose by any other name would smell as sweet"*

[The Great Game](https://github.com/simon-brooke/the-great-game) needs *a lot* of plausible-sounding names. This is an experiment in generating plausible-sounding names.

## Usage

**WARNING**: this is not yet stable and the API may change!

The structure of the names you use is going to depend a lot on your setting; the character set will also vary with setting (English uses very few diacritics). To allow for this, `wherefore-art-thou` is designed to be configured by rebinding dynamic variables. This means it will work 'out of the box' with English letter frequencies and my preferred gender suffixes, but if these don't suit you you can simply rebind the variables to override my preferences.

### Dynamic (rebindable) variables

The dynamic variables are:

### \*consonants\*

Characters considered to be consonants. You may want to add to these if your
language includes diacriticals on consonants. The members of this set should 
be strings.

### \*disallowed-sequences\*

This is very much a matter of taste; currently a list of character 
sequences I consider unpronouncible.

### \*female-suffixes\*

Suffixes which may be appended to female names. Note that suffixes will 
only be appended if the base name ends in a consonant. If your culture does
not use gender-specific name suffixes, bind this to `nil`.

### \*fragments\*

Characters and sequences of characters which can be appended to a candidate
name. You may want to add to these if your language includes diacriticals.
The members of this set should be strings.

### \*male-suffixes\*

Suffixes which may be appended to male names. Note that suffixes will 
only be appended if the base name ends in a consonant. If your culture does
not use gender-specific name suffixes, bind this to `nil`.

### \*max-consecutive-consonants\*

The maximum number of consecutive consonants in a name we will generate.

### \*max-consecutive-vowels\*

The maximum number of consecutive vowels in a name we will generate.

### \*max-name-length\*

The maximum length of a name (excluding gender suffix if any) we will 
generate.

### \*vowels\*

Characters considered to be vowels. You may want to add to these if your
language includes diacriticals on vowels. The members of this set should 
be strings.


Names are returned in all lower case, with the intention that they should 
be capitalised at the presentation layer; this is because the culture in
my game world creates names be compounding clan, family and personal names,
and embedded capitals would be awkward.

### Functions

#### generate
```clojure
(generate)
(generate gender)
```

Generate an acceptable candidate name. Gender is, at this stage, expected to 
be either the keyword `:male`, the keyword `:female`, or a map containing a
value for the keyword `:gender`. The library ought to work for fictional worlds
with more than two genders, but at present it does not.


## License

Copyright © 2024 Simon Brooke

This program and the accompanying materials are made available under the
terms of the GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
