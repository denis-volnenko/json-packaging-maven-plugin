echo "REMOVE '*.iml'..."
find . -name "*.iml" -type f -delete

echo "REMOVE 'dependency-reduced-pom.xml'..."
find . -name "dependency-reduced-pom.xml" -type f -delete

echo "REMOVE '.DS_Store...' "
find . -name ".DS_Store" -type f -delete

echo "REMOVE '.mvn'..."
find . -name .mvn -type d -print0|xargs -0 rm -r --

echo "REMOVE '.idea'..."
find . -name .idea -type d -print0|xargs -0 rm -r --

echo "REMOVE '.gradle'..."
find . -name .gradle -type d -print0|xargs -0 rm -r --

echo "REMOVE 'target'..."
find . -name target -type d -print0|xargs -0 rm -r --

echo "REMOVE 'build'..."
find . -name build -type d -print0|xargs -0 rm -r --