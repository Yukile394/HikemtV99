plugins {
    java
}

group = "ai.idealistic.spartan"
version = "1.0.0"

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(21)) }
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://libraries.minecraft.net")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.viaversion.com")
    maven("https://repo.opencollab.dev/main/")
}

val paperVersion: String = providers.gradleProperty("paperVersion").getOrElse("1.21.4-R0.1-SNAPSHOT")

// Harici eklenti API'leri icin (ItemsAdder, CrackShot, ProjectKorra ...) minimal stub'lar.
// Jar'a DAHIL EDILMEZ, sadece derleme icindir.
sourceSets {
    create("stubs") { java.srcDir("src/stubs/java") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:$paperVersion")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.3.0")
    compileOnly("com.viaversion:viaversion-api:5.0.0")
    compileOnly("org.geysermc.floodgate:api:2.2.3-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    "stubsCompileOnly"("io.papermc.paper:paper-api:$paperVersion")
    compileOnly(sourceSets["stubs"].output)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(21)
    options.compilerArgs.addAll(listOf("-Xmaxerrs", "10000", "-Xlint:-deprecation", "-Xlint:-removal"))
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("plugin.yml") { expand("version" to project.version) }
}

tasks.jar {
    archiveFileName.set("Spartan.jar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.build { dependsOn(tasks.jar) }
