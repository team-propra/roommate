sudo apt install yq helm -y

yq e ".version = \"0.0.0\" | .appVersion = \"0.0.0\"" -i chart/Chart.yaml
helm package ./chart
