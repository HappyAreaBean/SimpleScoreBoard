plugins {
    java
    id("com.gradleup.shadow") version "9.6.1"
    id("io.freefair.lombok") version "9.8.0"
    id("xyz.jpenilla.run-paper") version "3.1.0"
}

group = "cc.happyareabean"
version = "1.1.0"

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        url = uri("https://repo.extendedclip.com/releases/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

    compileOnly("me.clip:placeholderapi:2.11.6")

    implementation("fr.mrmicky:fastboard:2.2.2")
    implementation("dev.dejvokep:boosted-yaml:1.3.7")

    implementation("io.github.revxrsal:lamp.common:4.0.0-rc.19")
    implementation("io.github.revxrsal:lamp.bukkit:4.0.0-rc.19")
    implementation("io.github.revxrsal:lamp.brigadier:4.0.0-rc.19")

    implementation("org.bstats:bstats-bukkit:3.0.2")
}

tasks.shadowJar {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    val packageName = "cc.happyareabean.simplescoreboard.libs"

    if (System.getenv("DEV") == null) {
        relocate("fr.mrmicky.fastboard", "$packageName.fastboard")
        relocate("revxrsal.commands", "$packageName.lamp")
        relocate("dev.dejvokep.boostedyaml", "$packageName.boostedyaml")
    }

    relocate("org.bstats", "$packageName.bstats")
}

java {
    val targetJavaVersion = 21
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"

    if (JavaVersion.current().isJava10Compatible) {
        options.release.set(21)
    }
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks.runServer {
    minecraftVersion("1.21.8")

    downloadPlugins {
        hangar("PlaceholderAPI", "2.11.6")
    }
}

tasks.withType<xyz.jpenilla.runtask.task.AbstractRun>().configureEach {
    javaLauncher = javaToolchains.launcherFor {
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(21)
    }
    jvmArgs("-XX:+AllowEnhancedClassRedefinition")
}