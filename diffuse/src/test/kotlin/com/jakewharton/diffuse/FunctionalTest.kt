package com.jakewharton.diffuse

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class FunctionalTest : BaseFunctionalTest() {

  @Test
  fun `diffuse diff on stripe`() = runTest(
    mode = "apk",
    root = "stripe",
    artifactA = "paymentsheet-example-release-master.apk",
    artifactB = "paymentsheet-example-release-pr.apk",
  )

  @Test
  fun `diffuse diff on obfuscated artifact`() {
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
  fun `diffuse diff on aar`() = runTest(
    mode = "aar",
    root = "lazythreeten",
    artifactA = "lazythreetenbp-release.aar",
  )

  @Test
  fun `diffuse diff on signed artifact`() = runTest(
    mode = "apk",
    root = "otwarty-wykop-mobilny",
    artifactA = "app-release.apk",
    mappingA = "mapping.txt",
  )

  @Test
  fun `diffuse info returns proper size`() = runTest(
    mode = "jar",
    root = "diffuse",
    artifactA = "diffuse-unspecified-r8.jar",
  )
}
