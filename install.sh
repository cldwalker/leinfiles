#!/usr/bin/env bash
set -e

mkdir -p ~/.lein
ln -s $PWD/user.clj ~/.lein/
ln -s $PWD/user ~/.lein/
ln -s $PWD/profiles.clj ~/.lein/
