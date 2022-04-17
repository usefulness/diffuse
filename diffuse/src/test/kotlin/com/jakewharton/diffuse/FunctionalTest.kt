package com.jakewharton.diffuse

import java.io.File
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class FunctionalTest {

  @TempDir
  lateinit var anotherTempDir: File

  @Test
  internal fun `diffuse diff on stripe`() = runTest(
    mode = "apk",
    root = "stripe",
    artifactA = "paymentsheet-example-release-master.apk",
    artifactB = "paymentsheet-example-release-pr.apk",
  )

  @Test
  internal fun `diffuse diff on obfuscated artifact`() {
    val output = anotherTempDir.resolve("diffuse-out")
    val apkA = loadResource("github-browser/apkA.apk")
    val apkB = loadResource("github-browser/apkB.apk")
    val mappingsA = loadResource("github-browser/mappingA.txt")
    val mappingsB = loadResource("github-browser/mappingB.txt")

    main(
      "diff",
      "--apk", apkA, apkB,
      "--old-mapping", mappingsA,
      "--new-mapping", mappingsB,
      "--text", output.path,
    )

    val lines = output.readLines()
    assertThat(lines).contains("  - androidx.recyclerview.widget.RecyclerView markItemDecorInsetsDirty():0: RecyclerView\$ItemAnimator")
    assertThat(lines[7]).isEqualTo("      dex │     1 MiB │   1.2 MiB │ +164.8 KiB │   2.2 MiB │   2.6 MiB │ +380.9 KiB ")
  }

  @Test
  internal fun `diffuse diff on aar`() = runTest(
    mode = "aar",
    root = "lazythreeten",
    artifactA = "lazythreetenbp-release.aar",
  )

  @Test
  internal fun `diffuse diff on signed artifact`() = runTest(
    mode = "apk",
    root = "otwarty-wykop-mobilny",
    artifactA = "app-release.apk",
    mappingA = "mapping.txt",
  )

  @Test
  internal fun `diffuse info returns proper size`() = runTest(
    mode = "jar",
    root = "diffuse",
    artifactA = "diffuse-unspecified-r8.jar",
  )

  private fun runTest(
    mode: String,
    root: String,
    artifactA: String,
    artifactB: String = artifactA,
    mappingA: String? = null,
    mappingB: String? = mappingA,
  ) {
    val diffOutput = anotherTempDir.resolve("diffuse-diff")
    val infoOutput = anotherTempDir.resolve("diffuse-info")
    val artifactAResource = loadResource("$root/$artifactA")
    val artifactBResource = loadResource("$root/$artifactB")
    val mappingAResource = mappingA?.let { loadResource("$root/$it") }
    val mappingBResource = mappingB?.let { loadResource("$root/$it") }
    val expectedDiff = loadResource("$root/diff.txt").let(::File)
    val expectedInfo = loadResource("$root/info.txt").let(::File)

    if (mappingAResource == null || mappingBResource == null) {
      main(
        "diff",
        "--$mode", artifactAResource, artifactBResource,
        "--text", diffOutput.path,
      )
    } else {
      main(
        "diff",
        "--$mode", artifactAResource, artifactBResource,
        "--old-mapping", mappingAResource,
        "--new-mapping", mappingBResource,
        "--text", diffOutput.path,
      )
    }
    main(
      "info",
      "--$mode", artifactBResource,
      "--text", infoOutput.path,
    )

    assertThat(diffOutput).hasSameTextualContentAs(expectedDiff)
    assertThat(infoOutput).hasSameTextualContentAs(expectedInfo)
  }
}

private object Loader

fun loadResource(name: String) = Loader::class.java.classLoader.getResource(name).let(::checkNotNull).file
