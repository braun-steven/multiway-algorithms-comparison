#!/usr/bin/env bash

REPO_DIR_MW_ALGS="multiway-algorithms"

if [ ! -d "$REPO_DIR_MW_ALGS" ]; then
    echo -e "Local multiway-algorithm repository not found. Cloning now."
    git clone https://github.com/waikato-datamining/multiway-algorithms -b develop
else
    echo -e "Updating local multiway-algorithm repository"
    cd ${REPO_DIR_MW_ALGS}
    git pull
    cd ..
fi

cd matlab
REPO_DIR_NWAY="nway"
if [ ! -d "$REPO_DIR_NWAY" ]; then
    echo -e "Local nway-toolbox repository not found. Cloning now."
    git clone https://github.com/andrewssobral/nway.git
fi
cd ..

cd ${REPO_DIR_MW_ALGS}
echo -e "Installing multiway-algorithm maven package locally."
mvn install -Dmaven.test.skip=true
cd ..