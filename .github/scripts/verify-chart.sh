VERSION=$1

sudo apt install yq helm -y

yq e ".version = \"$VERSION\" | .appVersion = \"$VERSION\"" -i chart/Chart.yaml
helm package ./chart
