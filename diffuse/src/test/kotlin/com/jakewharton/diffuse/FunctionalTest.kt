package com.jakewharton.diffuse

import com.google.common.truth.Truth.assertThat
import java.io.File
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class FunctionalTest {

  @TempDir
  lateinit var anotherTempDir: File

  @Test
  internal fun `diffuse diff on stripe`() {
    val output = anotherTempDir.resolve("diffuse-out")
    val apkA = loadResource("stripe/paymentsheet-example-release-master.apk")
    val apkB = loadResource("stripe/paymentsheet-example-release-pr.apk")

    main(
      "diff",
      "--apk", apkA, apkB,
      "--text", output.path,
    )

    assertThat(output.readLines()).contains("  - com.stripe.android.model.PaymentMethod component17\$payments_core_release() → PaymentMethod\$USBankAccount")
  }

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
  internal fun `diffuse diff on aar`() {
    val output = anotherTempDir.resolve("diffuse-out")
    val aarA = loadResource("lazythreeten/lazythreetenbp-release.aar")
    val aarB = loadResource("lazythreeten/lazythreetenbp-release.aar")

    main(
      "diff",
      "--aar", aarA, aarB,
      "--text", output.path,
    )

    assertThat(output.readLines()).contains("    total │ 160.7 KiB │ 160.7 KiB │  0 B ")
  }
}

private object Loader

fun loadResource(name: String) = Loader::class.java.classLoader.getResource(name).let(::checkNotNull).file
