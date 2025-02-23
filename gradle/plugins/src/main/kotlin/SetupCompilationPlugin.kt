import io.github.usefulness.KtlintGradleExtension
import io.github.usefulness.KtlintGradlePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import org.gradle.language.jvm.tasks.ProcessResources
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

class SetupCompilationPlugin : Plugin<Project> {

  override fun apply(project: Project) = with(project) {
    pluginManager.apply("org.jetbrains.kotlin.jvm")

    val javaTarget = getVersionCatalogVersion("java-target").toInt()

    kotlinExtension.apply {
      jvmToolchain(23)
    }

    tasks.withType<KotlinJvmCompile>().configureEach {
      compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(javaTarget.toString()))
        freeCompilerArgs.add("-opt-in=kotlin.contracts.ExperimentalContracts")
      }
    }

    pluginManager.withPlugin("java") {
      tasks.withType<JavaCompile>().configureEach {
        options.release.set(javaTarget)
      }
      tasks.named<ProcessResources>("processResources") {
        from(isolated.rootProject.projectDirectory.file("LICENSE"))
      }
    }


    pluginManager.apply(KtlintGradlePlugin::class.java)
    extensions.configure<KtlintGradleExtension>("ktlint") {
      ktlintVersion.set(getVersionCatalogVersion("ktlint"))
    }

    tasks.withType<Test>().configureEach {
      useJUnitPlatform()
    }

    configurations.configureEach {
      resolutionStrategy.eachDependency {
        if (requested.group == "com.android.tools.build" && requested.name == "aapt2-proto") {
          useVersion(getVersionCatalogVersion("aapt2Proto").toString())
          because("we need to keep dependencies in sync with bundletool")
        }
        if (requested.group == "com.google.protobuf" && requested.name == "protobuf-java") {
          useVersion(getVersionCatalogVersion("protobufJava").toString())
          because("we need to keep dependencies in sync with bundletool")
        }
        if (requested.group == "com.google.guava" && requested.name == "guava") {
          useVersion(getVersionCatalogVersion("guava").toString())
          because("we need to keep dependencies in sync with bundletool")
        }
      }
    }
  }
}

internal val Project.libs
  get() = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

internal fun Project.getVersionCatalogVersion(name: String) =
  libs.findVersion(name).get().requiredVersion
