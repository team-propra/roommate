VERSION=$1

sudo apt install yq helm -y

yq e ".version = \"$VERSION\" | .appVersion = \"$VERSION\"" -i chart/Chart.yaml
helm package ./chart
helm registry login registry.massivecreationlab.com -u $REGISTRY_USERNAME -p $REGISTRY_PASSWORD
helm push roommate-helm-$VERSION.tgz oci://registry.massivecreationlab.com/roommate