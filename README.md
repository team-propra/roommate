# RoomMate: Room booking solution

[![codecov](https://codecov.io/github/team-propra/roommate/graph/badge.svg?token=K9836C4OVS)](https://codecov.io/github/team-propra/roommate)
[![ReleasePipeline](https://github.com/team-propra/roommate/actions/workflows/cd.yml/badge.svg)](https://github.com/team-propra/roommate/actions/workflows/cd.yml)

## Getting Started

### OAuth
In order to run the Spring Boot application locally, you will need to provide a GitHub oauth application token.
First go to https://github.com/settings/applications/new and create an oauth application. 
- `name`: whatever
- `home-page url`: http://localhost:8080
- `auth callback url`: http://localhost:8080

Now you'll see `ClientID`. You also require a `Client Secret`, so feel free to generate one.
Afterwards in order to run the application you will need to set the following environment variables: `CLIENT_ID` and `CLIENT_SECRET`

In case you're using IntelliJ, you may follow these steps:
- Go to RunConfigurations > Edit (top right)
- Edit the Spring Boot Configuration for this project
- ModifyOptions > EnvironmentVariables
- Enter the following string (replacing *** with your values) `CLIENT_ID=***;CLIENT_SECRET=***`

Now you will be able to run the application properly with OAuth login :)

### Deployment

#### Using IntelliJ IDEA
If you are using IntelliJ to start up RoomMate, remember to add these following environment variables to your run configuration:

`ADMIN_HANDLE=YOUR_GITHUB_HANDLE` (_In case you want to test the application as an admin_)

`CLIENT_ID=YOUR_CLIENT_ID` (from OAuth step above)

`CLIENT_SECRET=YOUR_CLIENT_SECRET` (from OAuth step above)

`DATABASE_URL=localhost`

`KEYMASTER_URL=localhost`

#### Using Docker compose
Before you can use `docker compose up` you will need to create an .env file according to our [example.env](./example.env) and set it's values accordingly.

#### Using Kubernetes (helm)

Below is a minimal Kubernetes deployment utilizing the helm package manager.

First, you will need to create the `values.yaml` file and specify your secrets:

```yaml
namespace: roommate
ingress:
  host: ""
database:
  user: ""
  password: ""
roommate:
  adminHandle: ""
  clientId: ""
  clientSecret: ""
```

Then deploy the application using helm in your cluster:

```shell
helm install roommate-helm \
  oci://registry.massivecreationlab.com/roommate \
  --version 1.0.0 \
  -n roommate \
  --create-namespace \
  -f values.yaml
```

## Documentation
For an overview of the project's scope, basic architecture and goals & requirements see our [documentation](./docs/RoomMate_doc.md).
