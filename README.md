<p align="center" style="margin-bottom: 0; padding: 0">
<a href="https://github.com/team_propra/roommate/">
<img width="180" height="180" src="media/logo.png" alt="logo">
</a>
</p>
<h1 align="center" style="margin-top: 0; padding: 0">
  Roommate
</h1>
<p align="center">
  <a href="https://codecov.io/github/team-propra/roommate">
    <img src="https://codecov.io/github/team-propra/roommate/graph/badge.svg?token=K9836C4OVS" />
  </a>

  <a href="https://github.com/team-propra/roommate/actions/workflows/cd.yml">
    <img src="https://github.com/team-propra/roommate/actions/workflows/cd.yml/badge.svg" />
  </a>

  <a href="https://www.conventionalcommits.org/en/v1.0.0/">
    <img src="https://img.shields.io/badge/semantic--release-conventional--commits-e10079?logo=semantic-release" />
  </a>

  <a href="https://adoptium.net/temurin/releases/?version=21">
    <img src="https://img.shields.io/badge/compatibility-%E2%89%A5%20java21-c78d18?logo=java" />
  </a>
</p>
<p align="center">
  <a href="https://github.com/team_propra/roommate/">
    <img width="80%" src="media/screenshot.png" alt="screenshot">
  </a>
</p>
<p align="center">
By 
  <a href="https://github.com/nighoge">
    nighoge
  </a> |
  <a href="https://github.com/AhoiKrause">
    AhoiKrause
  </a> |
  <a href="https://github.com/F3lixLoch">
    F3lixLoch
  </a> |
  <a href="https://github.com/themassiveone/">
    themassiveone
  </a>
</p>

## Getting Started

You need Java 21 and a working Docker CLI, then run:

```shell
./gradlew bootRun
```

The application starts on http://localhost:8080. It automatically starts ephemeral Postgres and Keymaster containers and stops them when the application stops.

GitHub OAuth is optional for local browsing. Without OAuth credentials you can use Roommate as a guest, inspect rooms and workspaces, and search availability. Booking rooms and admin actions require GitHub login and a verified/admin user.

## About

Roommate is a room booking software. It was a team project created as part of a module at university.

The project came with a set of challenges:
- External dependency: Key management software (keymaster.jar), cannot be manipulated
- Authentication via GitHub OAuth
- Strict onion architecture

Required features:
- Use case: Admins manage workspaces and rooms
- Use case: Users search and book rooms

## Demo

A public demo is hosted [here](https://roommate.massivecreationlab.com). Contact us for admin access.

## Deploy Roommate

### GitHub OAuth

GitHub OAuth is required for booking and admin flows. Guests can still browse without it.

Go to https://github.com/settings/applications/new and create an OAuth application.
- `name`: whatever
- `home-page url`: `http://localhost:8080` or your hosted domain
- `authorization callback url`: `http://localhost:8080/login/oauth2/code/github` or `https://your-domain/login/oauth2/code/github`

Store the generated client ID and client secret securely.

For hosted deployments, the callback URI can also be set explicitly with `OAUTH2_GITHUB_REDIRECT_URI`. The Helm chart defaults it to `https://<ingress.host>/login/oauth2/code/github`.

### IntelliJ IDEA

No environment variables are required for guest-only local browsing. If you want to test GitHub login, add these environment variables to your Spring Boot run configuration:
- Go to RunConfigurations > Edit (top right)
- Edit the Spring Boot Configuration for this project
- ModifyOptions > EnvironmentVariables
- Enter environment variables as one string like this: `CLIENT_ID=***;CLIENT_SECRET=***`

Common local variables:
- `ADMIN_HANDLE=YOUR_GITHUB_HANDLE` (_In case you want to test the application as an admin_)
- `CLIENT_ID=YOUR_CLIENT_ID`
- `CLIENT_SECRET=YOUR_CLIENT_SECRET`
- `OAUTH2_GITHUB_REDIRECT_URI=http://localhost:8080/login/oauth2/code/github`

If you want to connect to external infrastructure instead of the automatic local containers, configure both:
- `SPRING_DATASOURCE_EXTERNAL=true`
- `SPRING_DATASOURCE_URL=jdbc:postgresql://HOST:PORT/postgres`
- `SPRING_DATASOURCE_USERNAME=YOUR_POSTGRES_USER`
- `SPRING_DATASOURCE_PASSWORD=YOUR_POSTGRES_PASSWORD`
- `ROOMMATE_KEY_MASTER_EXTERNAL=true`
- `ROOMMATE_KEY_MASTER_URL=HOST`
- `ROOMMATE_KEY_MASTER_PORT=3000`

### Docker Compose

Before using `docker compose up`, create an `.env` file according to [example.env](./example.env) and set its values accordingly. Docker Compose starts Roommate, Postgres, and Keymaster as separate services.

### Kubernetes With Helm

Below is a minimal Kubernetes deployment using the Helm chart. The chart deploys Postgres and Keymaster by default and configures Roommate to use those chart-managed services.

Create a `values.yaml` file and specify your host and secrets:

```yaml
namespace: roommate
ingress:
  host: ""
database:
  port: 5432
  containerPort: 5432
  user: ""
  password: ""
keymaster:
  port: 3000
  containerPort: 3000
roommate:
  adminHandle: ""
  clientId: ""
  clientSecret: ""
  oauthRedirectUri: ""
```

Then deploy the application:

```shell
helm install roommate-helm \
  oci://registry.massivecreationlab.com/roommate \
  -n roommate \
  --create-namespace \
  -f values.yaml
```

## Test Patterns

The test suite is split by responsibility, with small files grouped by route, service, repository behavior, or architecture rule.

### Controller Route Specifications

Controller tests live under `src/test/java/com/example/roommate/tests/controller/{controller}/{Route}Test.java`. They are Xcepto specifications using `xcepto-ssr`, not MockMvc slice tests. The Spring application runs on a random port, and each example drives the route through real HTTP.

Xcepto keeps the test shape close to a user flow: a scenario provides the running application, a browser role performs named route actions, and the assertions describe the observable response. This makes multi-step controller behavior readable in domain language instead of spreading request builders, status checks, redirects, and body assertions through the test. GET-style checks can be retried by the state machine, while POST-style actions execute once.

```java
@ControllerRouteTest
class PostBookTest extends ControllerHttpFixtureTest {
    @Test
    void verifiedBookerCanPersistAValidWorkspaceSelection() throws Exception {
        String selectedEquipment = "Monitor";
        String selectedCell = "0-1-X";
        when(bookingApplicationService.getWorkspaceDetailsModel(ROOM_ID, WORKSPACE_ID))
                .thenReturn(workspaceDetails("A-12", 4, selectedEquipment, "Dock"));
        when(bookingApplicationService.isBookingSelectionValid(any(), anyList())).thenReturn(true);
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.verifiedBooker(builder, scenario.baseUri());

            roommate.opensWorkspace(ROOM_ID, WORKSPACE_ID)
                    .assertSuccess()
                    .assertThatResponseContentString(html -> html.contains(selectedEquipment));

            roommate.submitsBookingSelection(ROOM_ID, WORKSPACE_ID, 60, selectedCell)
                    .assertThatResponseStatus(302)
                    .assertThatResponse(response -> assertRedirectsTo(response, "/"));
        }, TIMEOUT, STEP);
    }
}
```

More information about Xcepto is available at https://xcepto.org.

### Domain And Service Tests

Domain, value, validation, and application-service tests live under `tests/domain`, `tests/validation`, and `tests/services`. They exercise business rules directly with JUnit and AssertJ, without HTTP or Spring MVC route setup.

```java
@Test
void bookingDaysBecomeBookedTimeframes() {
    var bookingDays = BookingDays.from(60, List.of("0-0-X"));

    assertThat(bookingDays.toBookedTimeframes("user")).isNotEmpty();
}
```

### Repository Backends

Repository-backed behavior uses `@RepositoryBackendsTest`. The same specification runs once against ephemeral repositories and once against Postgres repositories. The Postgres variant uses shared Testcontainers infrastructure with isolated databases.

```java
@RepositoryBackendsTest
void roomCanBeAdded(RepositoryFixture fixture) {
    fixture.roomDomainService().addRoom(room);

    assertThat(fixture.rooms().findAll()).isNotEmpty();
}
```

### Architecture Tests

Architecture tests under `tests/architecture` use ArchUnit. They guard onion-layer access, annotation conventions, package/name correlations, and value/interface type rules.

```java
@ArchTest
static ArchRule classesWithDirectServiceAnnotationShouldNotExist = classes()
        .that().areNotAnnotations()
        .should().notBeAnnotatedWith(Service.class);
```


## Documentation
For an overview of the project's scope, basic architecture and goals & requirements see our [documentation](./docs/RoomMate_doc.md).
