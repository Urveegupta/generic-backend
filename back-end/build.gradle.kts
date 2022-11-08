plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:31.0.1-jre")

    implementation("io.javalin:javalin:4.6.4")

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    implementation("org.slf4j:slf4j-simple:2.0.1")

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
    implementation("org.slf4j:slf4j-api:2.0.1")

    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4")

    // https://mavenlibs.com/maven/dependency/com.healthmarketscience.sqlbuilder/sqlbuilder
    implementation("com.healthmarketscience.sqlbuilder:sqlbuilder:3.0.2")

    // https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple
    implementation("com.googlecode.json-simple:json-simple:1.1.1")

    // cayenne dependencies
    implementation("org.apache.cayenne:cayenne-java8:4.0.2")
    implementation("org.apache.cayenne:cayenne-server:4.1.1")

    // https://mavenlibs.com/maven/dependency/mysql/mysql-connector-java
    implementation("mysql:mysql-connector-java:8.0.30")

    // unit tests
    implementation("io.mockk:mockk:1.12.5")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}