package com.jakewharton.diffuse

import java.io.File
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.io.TempDir

abstract class BaseFunctionalTest {

  @TempDir
  lateinit var anotherTempDir: File

  protected fun runTest(
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

internal fun loadResource(name: String) = Loader::class.java.classLoader.getResource(name).let(::checkNotNull).file
