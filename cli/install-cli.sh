#!/bin/bash
set -e

INSTALL_BASE="/opt/dims"

# If there was a path specified, use that instead of the default
if [ ! "$1" == "" ]; then
   INSTALL_BASE="$1"
fi

echo "Installing to $INSTALL_BASE"

JAR_PATH="$INSTALL_BASE/jars/stix"

if [ -d "$JAR_PATH" ]; then
    echo "Existing jar path found, cleaning"
    rm -f "$JAR_PATH/*"
fi

# Copy all the jars and properties into the jars folder
mkdir -p "$JAR_PATH"

cp target/*.jar "$JAR_PATH"
# Copy any properties, ignoring errors
cp target/*.properties "$JAR_PATH" 2> /dev/null || :

cp *.properties "$JAR_PATH" 2> /dev/null || :

# Copy the scripts into the bin folder
mkdir -p "$INSTALL_BASE/bin"
cp stix.* "$INSTALL_BASE/bin"

# Set permissions
chmod -R 0775 "$JAR_PATH"

# Make sure the directories have +x
chmod a+x "$INSTALL_BASE/bin"
chmod a+x "$JAR_PATH"
# Make sure the scripts are executable
for f in `ls $INSTALL_BASE/bin/stix.*`; do
    chmod a+x "$f"
done

echo "Installed to $INSTALL_BASE"
echo "Add '$INSTALL_BASE/bin' to your path"
