## Description

Handy functions to have while in a Clojure repl - a beefed up clojure.repl

## Setup

Since [leiningen 2](https://github.com/technomancy/leiningen) is the most common way to start a
clojure repl, let's assume it's installed for setup.

Basic setup:

    $ git clone https://github.com/cldwalker/leinfiles.git
    $ cd leinfiles
    $ ln -s $PWD/user.clj ~/.lein/
    $ ln -s $PWD/profiles.clj ~/.lein/

For older versions of leiningen or if using a different repl, you can always explicitly load the
file from the repl:

    # Use your full home path
    >> (load-file "~/.lein/user.clj")

## Usage

Functions are defined in the user namespace:

```sh
$ lein repl
Loaded user.clj!
# List available fns
user=> (vars-meta)
+------+---------------------+---------+------+--------------------------+------------------------+-----------------------------------------------------------------------------------------------------+-------+--+
| ns   | name                | dynamic | line | file                     | arglists               |
doc
| macro |  |
+------+---------------------+---------+------+--------------------------+------------------------+-----------------------------------------------------------------------------------------------------+-------+--+
| user | *display*           | true    | 41   | /Users/me/.lein/user.clj |                        |
|       |  |
| user | cdoc                |         | 1    | NO_SOURCE_PATH           | ([v] [ns-str var-str]) |
Lazily checks if the clojuredocs client is available, and uses it to\n  retrieve examples if it is.
| true  |  |
| user | class-paths         |         | 84   | /Users/me/.lein/user.clj | ([])                   |
Prints list of class paths
|       |  |
| user | clojuredocs         |         | 1    | NO_SOURCE_PATH           | ([v] [ns-str var-str]) |
Lazily checks if the clojuredocs client is available, and uses it to\n  retrieve examples if it is.
| true  |  |
| user | display             |         | 43   | /Users/me/.lein/user.clj | ([data & options])     |
Pretty prints data or returns it depending on value of *display*. Default is to print with table.
|       |  |
| user | doc-dir             |         | 32   | /Users/me/.lein/user.clj | ([nsname])             |
Prints docs for a given namespace
|       |  |
| user | envs                |         | 92   | /Users/me/.lein/user.clj | ([])                   |
List of envs and their values
|       |  |
| user | help                |         | 1    | NO_SOURCE_PATH           | ([])                   |
Prints a list of helpful commands.
|       |  |
| user | java-methods        |         | 58   | /Users/me/.lein/user.clj | ([klass])              |
List of methods for a java class
|       |  |
| user | java-methods-for    |         | 61   | /Users/me/.lein/user.clj | ([obj])                |
List of methods for java object
|       |  |
| user | jdoc                |         | 38   | /Users/me/.lein/user.clj | ([obj])                |
javadoc an object
|       |  |
| user | ns-dynamic-vars     |         | 18   | /Users/me/.lein/user.clj | ([] [nsname])          |
dynamic vars for a namespace as determined by *var* convention
|       |  |
| user | properties          |         | 87   | /Users/me/.lein/user.clj | ([])                   |
List properties and their values
|       |  |
| user | pster               |         | 51   | /Users/me/.lein/user.clj | ([] [err])             |
Print full error stack
|       |  |
| user | set-signal-handler! |         | 1    | NO_SOURCE_PATH           | ([signal f])           |
| true  |  |
| user | sourcery            |         | 1    | NO_SOURCE_PATH           | ([name])               |
| true  |  |
| user | split               |         | 13   | /Users/me/.lein/user.clj | ([f coll])             |
|       |  |
| user | spy                 |         | 29   | /Users/me/.lein/user.clj | ([arg])                |
Simple print debugging
|       |  |
| user | sym-to-var          |         | 9    | /Users/me/.lein/user.clj | ([sym])                |
|       |  |
|      |                     |         |      |                          |                        |
|       |  |
| user | var-meta            |         | 64   | /Users/me/.lein/user.clj | ([sym])                |
Prints meta of a symbol
|       |  |
| user | vars-meta           |         | 67   | /Users/me/.lein/user.clj | ([] [nsname])          |
Prints public vars for a namespace with its meta info
|       |  |
| user | vars-values         |         | 72   | /Users/me/.lein/user.clj | ([& options])          |
Prints dynamic vars for a namespace mapped to their values
|       |  |
+------+---------------------+---------+------+--------------------------+------------------------+-----------------------------------------------------------------------------------------------------+-------+--+
```

## TODO
* Have functions auto-load only if a clojar is present. Will require reading local project.clj and conditional loading of files.
