#!/bin/bash
################################################################################
# Copyright (c) 2012 EclipseSource Inc. and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
# 
# Contributors:
#     hstaudacher - initial API and implementation
################################################################################
# Called once the tycho build has completed.
# Takes care of a few shortcomings in the current tycho build.
set -e

CURRENT_DIR=`pwd`

#Change to the packages directory starting from the directory of this script:
cd `dirname $0`/../..
ROOT_FOLDER=`pwd`

echo "Running publish.sh from $ROOT_FOLDER"
cd $ROOT_FOLDER


BUILD_QUALIFIER="`date '+%Y%m%d%H%M%S'`"    
echo "Using build qualifier $BUILD_QUALIFIER"

#If we have many more products let's consider something more generic.

# Move one linux zip archive of each product to
# a location on download.eclipse.org where they can be downloaded.
# Move the generated p2 repository to a location on download.eclipse.org
# where they can be consumed.    

DOWNLOAD_FOLDER=/var/www/download.eclipsesource.com/technology/tabris/downloads/nightly
if [ ! -d "$DOWNLOAD_FOLDER" ]; then
#we are not on the eclipsesource build machine. for testing, let's
#deploy the build inside the builds folder of com.eclipsesource.tabris.build
   DOWNLOAD_FOLDER="$ROOT_FOLDER/com.eclipsesource.tabris.build/target/downloads/nightly"
   mkdir -p $DOWNLOAD_FOLDER
fi             
                   
BUILD_VERSION="$BUILD_QUALIFIER"
echo "Creating p2 repo dir $DOWNLOAD_FOLDER/$BUILD_VERSION" 
mkdir -p $DOWNLOAD_FOLDER/$BUILD_VERSION
       
echo "Copying repo to $DOWNLOAD_FOLDER/$BUILD_VERSION" 
cp -R com.eclipsesource.tabris.repository/target/repository/* $DOWNLOAD_FOLDER/$BUILD_VERSION

SCRIPTS_FOLDER=$ROOT_FOLDER/com.eclipsesource.tabris.build/scripts

# Create Composite Repository if needed
echo "Check if Composite Repository creation is needed"
if [ ! -f "$DOWNLOAD_FOLDER/compositeArtifacts.xml" ]; then
  echo "Create Composite Repository at $DOWNLOAD_FOLDER"
  cd $CURRENT_DIR
  sh $SCRIPTS_FOLDER/comp-repo.sh $DOWNLOAD_FOLDER create "Tabris Repository (Nighly Builds)"
fi


# Adding repository to composite repository
echo "Adding repository to composite repository"
cd $CURRENT_DIR
sh $SCRIPTS_FOLDER/comp-repo.sh $DOWNLOAD_FOLDER add $BUILD_VERSION

# Purigng old nightlies
echo "Purging the old builds."
cd $DOWNLOAD_FOLDER

files_in_dir=`ls -d */ | grep -v "sdk" | wc -l`
files_to_delete_number=`expr $files_in_dir - 10`
if [ $files_to_delete_number -gt 0 ]; then
  files_to_delete=`ls -t | grep -v "sdk" | tail -n $files_to_delete_number`
  cd $CURRENT_DIR
  for file in $files_to_delete; do
    sh $SCRIPTS_FOLDER/comp-repo.sh $DOWNLOAD_FOLDER remove $file
    rm -rf $DOWNLOAD_FOLDER/$file
  done
else
  echo "nothing to delete!"
fi
cd $CURRENT_DIR 