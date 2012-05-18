## Description

Handy functions to have while in a Clojure repl - a beefed up clojure.repl

## Setup

Since [leiningen](https://github.com/technomancy/leiningen) is the most common way to start a
clojure repl, let's assume it's installed for setup.

Basic setup:

    $ git clone https://github.com/cldwalker/leinfiles.git
    $ cd leinfiles
    $ ln -s $PWD/user.clj ~/.lein/

For older versions of leiningen or if using a different repl, you can always explicitly load the
file from the repl:

    # Use your full home path
    >> (load-file "~/.lein/user.clj")

## Usage

Functions are defined in the u namespace and are available in any namespace:

    $ lein repl
    Loaded user.clj!
    user=> (u/load-path)
    (#<URL file:/Users/me/code/fork/>
     #<URL file:/Users/me/code/fork/>
     #<URL file:/Users/me/code/fork/>
     #<URL file:/Users/me/.lein/plugins/lein-noir-1.2.1.jar>
     #<URL file:/Users/me/code/fork/>
     #<URL file:/Users/me/code/fork/test>
     #<URL file:/Users/me/code/fork/src>
     #<URL file:/Users/me/code/fork/resources>
     #<URL file:/Users/me/.lein/self-installs/leiningen-1.7.1-standalone.jar>)

    # List all functions in u namespace
    user=> (dir u)

## TODO
* Have functions auto-load only if a clojar is present. Will require reading local project.clj and conditional loading of files.
