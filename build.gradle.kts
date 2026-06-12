import net.neoforged.moddevgradle.dsl.RunModel
import org.slf4j.event.Level

plugins {
    id("idea")
    id("maven-publish")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.modDevGradle.legacyForge)
}

val modId = "deepdrilling"
val modName = "Create: Deep Drilling NeoForged"
version = "1.2.1"
group = "concerrox.$modId"
base.archivesName = "$modId-forge-${libs.versions.minecraft.get()}"

java.toolchain.languageVersion = JavaLanguageVersion.of(21)
sourceSets.main.get().resources { srcDir("src/generated/resources") }

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

legacyForge {

    version = libs.versions.minecraft.get() + '-' + libs.versions.forge.get()

    parchment {
        minecraftVersion = libs.versions.minecraft.get()
        mappingsVersion = libs.versions.parchment.get()
    }

    mods {
        create(modId) {
            sourceSet(sourceSets.main.get())
        }
    }

    runs {
        create("client", Action<RunModel> {
            client()
        })
        create("server", Action<RunModel> {
            server()
        })
        create("data", Action<RunModel> {
            data()
            programArguments.addAll(
                "--mod", modId, "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath,
            )
        })
        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            systemProperty("terminal.ansi", "true")
            logLevel = Level.DEBUG
        }
    }

}

val localRuntime by configurations.creating
configurations {
    runtimeClasspath.get().extendsFrom(localRuntime)
}

repositories {
    maven("https://thedarkcolour.github.io/KotlinForForge") // Kotlin for Forge
    maven("https://maven.createmod.net") // Create, Ponder, Flywheel
    maven("https://maven.ithundxr.dev/mirror") // Registrate
//    maven("https://maven.terraformersmc.com") // EMI
}

fun Provider<MinimalExternalModuleDependency>.get(variant: String): String = get().toString() + ":$variant"
val Provider<MinimalExternalModuleDependency>.slim get() = get("slim")
val Provider<MinimalExternalModuleDependency>.api get() = get("api")

dependencies {
    localRuntime(libs.kotlinForForge.forge)

    modImplementation(libs.create.slim) { isTransitive = false }
    modImplementation(libs.registrate)
    modImplementation(libs.ponder.forge)
    modRuntimeOnly(libs.flywheel.forge)
    modCompileOnly(libs.flywheel.forge.api)

    compileOnly(requireNotNull(annotationProcessor(libs.mixinExtras.common.get())))
    implementation(libs.mixinExtras.forge)
//    localRuntime(libs.emi.neoForge)
}

val generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    val replaceProperties = mapOf(
        "minecraft_version" to libs.versions.minecraft.get(),
        "minecraft_version_range" to "[${libs.versions.minecraft.get()},)",
        "forge_version" to "[${libs.versions.forge.get()},)",
        "loader_version_range" to "[47,)",
        "mod_id" to modId,
        "mod_name" to modName,
        "mod_license" to "MIT",
        "mod_version" to version,
        "mod_authors" to "beeisyou, ConcerroX",
        "mod_description" to "An addon for Create, adding powerful drills that extract ores from deep within the earth. ",
        "kff_version_range" to "[${libs.kotlinForForge.forge.get().version},)",
        "create_version_range" to "[6.0.6,)",
    )
    inputs.properties(replaceProperties)
    expand(replaceProperties)
    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}

sourceSets.main.get().resources.srcDir(generateModMetadata)
legacyForge.ideSyncTask(generateModMetadata)

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

configure<PublishingExtension> {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = modId
        }
    }
    repositories {
        maven {
            url = uri(File(project.projectDir, "repo"))
        }
    }
}

tasks.build {
    dependsOn(tasks.publishToMavenLocal)
}