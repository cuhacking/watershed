# Watershed
Watershed is a project that aims to create a general set of back end resources
for managing, planning, and running a hackathon. Originally created for cuHacking.

## Getting Started
### Prerequisites
This project is built in Kotlin, on top of JDK 15.

### Setup
To setup the configuration file, create a copy of the file `config-sample.yml` called `config.yml`, 
and input the relevant values for your database connection.

### Building and Running
Before running for the first time (and when running after a database schema change), database migrations must be generated
and then run. This can be done with:

`./gradlew migrate`

This will generate the SQL migrations from the SQLDelight migrations, and then execute them using Flyway.
Then, the application can be run with:

`./gradlew run --args="--config ../config.yml"` (assuming you config is located at the same level as `config-sample.yml`)

Some other useful Gradle commands:
* `./gradlew generateMainDatabaseMigrations` - Generates SQL migrations from SQLDelight migrations. This is automatically run when the `migrate` target is called
* `./gradlew flywayMigrate` - Runs Flyway migrations. Depends on the above command, so it's better to just run `migrate` unless you have a reason not to
* `./gradlew generateMainDatabaseInterface` - Generates the SQLDelight API. Useful for when you make changes to the SQlDelight statements but don't want to rebuild
* When in doubt and IntelliJ is complaining, run `clean build`


## Key Components and Structure
Some of the key components this project is built on:
* SQLDelight - For generating typesage APIs for database access
* Flyway - Handles migrations
* Dagger - Dependency Injection
* Ktor