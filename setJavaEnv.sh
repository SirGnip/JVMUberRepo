# This script is used by launchJavaTerminalBash.bat and can be run manually
echo Setting up Java environment...
echo
export JAVA_HOME="/c/Program Files/Java/jdk1.8.0_45"
echo "Set JAVA_HOME to $JAVA_HOME"
export PATH="$JAVA_HOME/bin:/c/Program Files (x86)/Meld:$PATH"
echo Prepended JAVA_HOME/bin and Meld to PATH
