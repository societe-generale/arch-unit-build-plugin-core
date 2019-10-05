#!/bin/bash

echo "scm.url=scm:git:git@github.com:societe-generale/arch-unit-build-plugin-core.git" >> release.properties
echo "scm.tag=$TRAVIS_TAG" >> release.properties

echo "******release.properties content*******"
cat release.properties

echo "******directory content*******"
pwd
ls -al
