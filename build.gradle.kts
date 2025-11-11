plugins {
    id("java")
}

group = "jacc.samples"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation("com.beust:jcommander:1.78")
    implementation("com.google.code.gson:gson:2.8.9")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}