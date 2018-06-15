# Serverside-test

Implementation of [this exercise](https://jsainsburyplc.github.io/serverside-test/).

Uses Maven for building and testing.

`mvn package` will download dependencies and compile the class files.

`mvn test` will run the unit tests.

`mvn exec:java` will run the application.

The `-q` flag can be used to mute the maven output and get the JSON output only.
