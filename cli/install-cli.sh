#!/bin/bash
set -e

INSTALL_BASE="/opt/dims"

# If there was a path specified, use that instead of the default
if [ ! "$1" == "" ]; then
   INSTALL_BASE="$1"
fi

echo "Installing to $INSTALL_BASE"

# Copy all the jars and properties into the jars folder
mkdir -p "$INSTALL_BASE/jars"

cp target/*.jar "$INSTALL_BASE/jars"
# Copy any properties, ignoring errors
cp target/*.properties "$INSTALL_BASE/jars" 2> /dev/null || :

cp *.properties "$INSTALL_BASE/jars" 2> /dev/null || :

# Copy the scripts into the bin folder
mkdir -p "$INSTALL_BASE/bin"
cp stix.* "$INSTALL_BASE/bin"

# Set permissions
chmod -R a+r "$INSTALL_BASE/jars"
chmod -R a-w "$INSTALL_BASE/jars"

# Make sure the directories have +x
chmod a+x "$INSTALL_BASE/bin"
chmod a+x "$INSTALL_BASE/jars"
# Make sure the scripts are executable
for f in `ls $INSTALL_BASE/bin/stix.*`; do
    chmod a+x "$f"
done

echo "Installed to $INSTALL_BASE"
echo "Add '$INSTALL_BASE/bin' to your path"
