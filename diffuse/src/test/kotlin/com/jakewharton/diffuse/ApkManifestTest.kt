package com.jakewharton.diffuse

import com.jakewharton.diffuse.format.Apk.Companion.toApk
import com.jakewharton.diffuse.io.Input.Companion.asInput
import java.io.File
import kotlin.io.path.Path
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ApkManifestTest {

  @Test
  fun `binary manifest of stripe master`() = assertManifest("stripe/paymentsheet-example-release-master.apk", "stripe/manifest-master.txt")

  @Test
  fun `binary manifest of stripe pr`() = assertManifest("stripe/paymentsheet-example-release-pr.apk", "stripe/manifest-pr.txt")

  @Test
  fun `binary manifest of signed artifact`() = assertManifest("otwarty-wykop-mobilny/app-release.apk", "otwarty-wykop-mobilny/manifest.txt")

  @Test
  fun `binary manifest of obfuscated artifact A`() = assertManifest("github-browser/apkA.apk", "github-browser/manifest-a.txt")

  @Test
  fun `binary manifest of obfuscated artifact B`() = assertManifest("github-browser/apkB.apk", "github-browser/manifest-b.txt")

  private fun assertManifest(apk: String, expected: String) {
    val manifest = Path(loadResource(apk)).asInput().toApk().manifest

    val actual = buildString {
      appendLine("package: ${manifest.packageName}")
      appendLine("versionName: ${manifest.versionName}")
      appendLine("versionCode: ${manifest.versionCode}")
      appendLine()
      append(manifest.xml)
    }

    assertThat(actual).isEqualTo(File(loadResource(expected)).readText())
  }
}
