plugins {
    kotlin("jvm") version "2.0.10"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
    id("com.gradleup.shadow") version "8.3.2"
}

repositories {
    mavenCentral()
    maven("https://maven.miles.sh/pineapple")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT") { isChanging = true }
    implementation("sh.miles:pineapple-bundle:1.0.0-SNAPSHOT") { isChanging = true }
    bukkitLibrary(kotlin("stdlib"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    this.archiveClassifier = ""
    this.archiveVersion = ""
    archiveFileName = "LegacyTextify-${project.version}.jar"

    val packageName = "${project.group}.${project.name.lowercase()}"
    this.relocate("sh.miles.pineapple", "$packageName.libs.pineapple")
}

tasks.jar {
    enabled = false
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

kotlin {
    jvmToolchain(17)
}

bukkit {
    name = project.name
    version = project.version.toString()
    main = "sh.miles.${project.name.lowercase()}.${project.name}Plugin"
    apiVersion = "1.20" // LATEST
}
