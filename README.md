# RoomMate by Team-Propra

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

## Documentation
For an overview of the project's scope, basic architecture and goals & requirements see our [documentation](./docs/RoomMate_doc.md).
## Current-branch
[![Tests](https://github.com/team-propra/main/actions/workflows/gradle.yml/badge.svg)](https://github.com/team-propra/main/actions/workflows/gradle.yml)

## Develop-branch
[![codecov](https://codecov.io/gh/team-propra/main/branch/develop/graph/badge.svg?token=K9836C4OVS)](https://codecov.io/gh/team-propra/main)

![graph](https://codecov.io/gh/team-propra/main/branch/develop/graphs/icicle.svg?token=K9836C4OVS)

## Master-branch
[![codecov](https://codecov.io/gh/team-propra/main/branch/master/graph/badge.svg?token=K9836C4OVS)](https://codecov.io/gh/team-propra/main)

![graph](https://codecov.io/gh/team-propra/main/branch/master/graphs/icicle.svg?token=K9836C4OVS)
