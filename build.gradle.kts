plugins {
    id("java")
    id("application")
}

group = "org.financiai"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Testes com JUnit
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // Conexão com banco de dados
    implementation("mysql:mysql-connector-java:8.0.33")

    // Geração de PDF
    implementation ("com.itextpdf:itextpdf:5.5.13.3")
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass.set("org.financiai.view.Program")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}