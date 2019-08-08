package com.jakewharton.dex

import com.google.common.io.Resources
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.io.File

class DexFieldsTest {
  @Test fun types() {
    val types = File(Resources.getResource("types.dex").file)
    val methods = DexParser.fromFile(types)
        .listFields()
        .map { it.toString() }
    assertThat(methods).containsExactly(
        "Types valueBoolean: boolean",
        "Types valueByte: byte",
        "Types valueChar: char",
        "Types valueDouble: double",
        "Types valueFloat: float",
        "Types valueInt: int",
        "Types valueLong: long",
        "Types valueShort: short",
        "Types valueString: String",
        "Types valueStringArray: String[]")
  }

  @Test fun visibilities() {
    val visibilities = File(Resources.getResource("visibilities.dex").file)
    val methods = DexParser.fromFile(visibilities)
        .listFields()
        .map { it.toString() }
    assertThat(methods).containsExactly(
        "Visibilities test1: String",
        "Visibilities test2: String",
        "Visibilities test3: String",
        "Visibilities test4: String")
  }

  @Test fun multipleDexFiles() {
    val types = File(Resources.getResource("types.dex").file)
    val visibilities = File(Resources.getResource("visibilities.dex").file)
    val methods = DexParser.fromFiles(types, visibilities)
        .listFields()
        .map { it.toString() }
    assertThat(methods).containsExactly(
        "Types valueBoolean: boolean",
        "Types valueByte: byte",
        "Types valueChar: char",
        "Types valueDouble: double",
        "Types valueFloat: float",
        "Types valueInt: int",
        "Types valueLong: long",
        "Types valueShort: short",
        "Types valueString: String",
        "Types valueStringArray: String[]",
        "Visibilities test1: String",
        "Visibilities test2: String",
        "Visibilities test3: String",
        "Visibilities test4: String")
  }

  @Test fun apk() {
    val one = File(Resources.getResource("one.apk").file)
    val methods = DexParser.fromFiles(one)
        .listFields()
        .map { it.toString() }
    assertThat(methods).containsExactly(
        "Types valueBoolean: boolean",
        "Types valueByte: byte",
        "Types valueChar: char",
        "Types valueDouble: double",
        "Types valueFloat: float",
        "Types valueInt: int",
        "Types valueLong: long",
        "Types valueShort: short",
        "Types valueString: String",
        "Types valueStringArray: String[]")
  }

  @Test fun apkMultipleDex() {
    val three = File(Resources.getResource("three.apk").file)
    val methods = DexParser.fromFiles(three)
        .listFields()
        .map { it.toString() }
    assertThat(methods).containsExactly(
        "Types valueBoolean: boolean",
        "Types valueByte: byte",
        "Types valueChar: char",
        "Types valueDouble: double",
        "Types valueFloat: float",
        "Types valueInt: int",
        "Types valueLong: long",
        "Types valueShort: short",
        "Types valueString: String",
        "Types valueStringArray: String[]",
        "Visibilities test1: String",
        "Visibilities test2: String",
        "Visibilities test3: String",
        "Visibilities test4: String")
  }

  @Test fun classFile() {
    val params = File(Resources.getResource("Visibilities.class").file)
    val methods = DexParser.fromFiles(params)
        .listFields()
        .map { it.toString() }
    assertThat(methods).containsExactly(
        "Visibilities test1: String",
        "Visibilities test2: String",
        "Visibilities test3: String",
        "Visibilities test4: String")
  }

  @Test fun multipleClassFiles() {
    val params = File(Resources.getResource("Types.class").file)
    val visibilities = File(Resources.getResource("Visibilities.class").file)
    val methods = DexParser.fromFiles(params, visibilities)
        .listFields()
        .map { it.toString() }
    assertThat(methods).containsExactly(
        "Types valueBoolean: boolean",
        "Types valueByte: byte",
        "Types valueChar: char",
        "Types valueDouble: double",
        "Types valueFloat: float",
        "Types valueInt: int",
        "Types valueLong: long",
        "Types valueShort: short",
        "Types valueString: String",
        "Types valueStringArray: String[]",
        "Visibilities test1: String",
        "Visibilities test2: String",
        "Visibilities test3: String",
        "Visibilities test4: String")
  }

  @Test fun jarFile() {
    val params = File(Resources.getResource("types.jar").file)
    val methods = DexParser.fromFiles(params)
        .listFields()
        .map { it.toString() }
    assertThat(methods).containsExactly(
        "Types valueBoolean: boolean",
        "Types valueByte: byte",
        "Types valueChar: char",
        "Types valueDouble: double",
        "Types valueFloat: float",
        "Types valueInt: int",
        "Types valueLong: long",
        "Types valueShort: short",
        "Types valueString: String",
        "Types valueStringArray: String[]")
  }

  @Test fun multipleJarFiles() {
    val types = File(Resources.getResource("types.jar").file)
    val visibilities = File(Resources.getResource("visibilities.jar").file)
    val methods = DexParser.fromFiles(types, visibilities)
        .listFields()
        .map { it.toString() }
    assertThat(methods).containsExactly(
        "Types valueBoolean: boolean",
        "Types valueByte: byte",
        "Types valueChar: char",
        "Types valueDouble: double",
        "Types valueFloat: float",
        "Types valueInt: int",
        "Types valueLong: long",
        "Types valueShort: short",
        "Types valueString: String",
        "Types valueStringArray: String[]",
        "Visibilities test1: String",
        "Visibilities test2: String",
        "Visibilities test3: String",
        "Visibilities test4: String")
  }

  @Test fun jarMultipleClasses() {
    val three = File(Resources.getResource("three.jar").file)
    val methods = DexParser.fromFiles(three)
        .listFields()
        .map { it.toString() }
    assertThat(methods).containsExactly(
        "Types valueBoolean: boolean",
        "Types valueByte: byte",
        "Types valueChar: char",
        "Types valueDouble: double",
        "Types valueFloat: float",
        "Types valueInt: int",
        "Types valueLong: long",
        "Types valueShort: short",
        "Types valueString: String",
        "Types valueStringArray: String[]",
        "Visibilities test1: String",
        "Visibilities test2: String",
        "Visibilities test3: String",
        "Visibilities test4: String")
  }

  @Test fun dexBytes() {
    val bytes = Resources.toByteArray(Resources.getResource("types.dex"))
    val methods = DexParser.fromBytes(bytes)
        .listFields()
        .map { it.toString() }
    assertThat(methods).containsExactly(
        "Types valueBoolean: boolean",
        "Types valueByte: byte",
        "Types valueChar: char",
        "Types valueDouble: double",
        "Types valueFloat: float",
        "Types valueInt: int",
        "Types valueLong: long",
        "Types valueShort: short",
        "Types valueString: String",
        "Types valueStringArray: String[]")
  }

  @Test fun apkBytes() {
    val bytes = Resources.toByteArray(Resources.getResource("one.apk"))
    val methods = DexParser.fromBytes(bytes)
        .listFields()
        .map { it.toString() }
    assertThat(methods).containsExactly(
        "Types valueBoolean: boolean",
        "Types valueByte: byte",
        "Types valueChar: char",
        "Types valueDouble: double",
        "Types valueFloat: float",
        "Types valueInt: int",
        "Types valueLong: long",
        "Types valueShort: short",
        "Types valueString: String",
        "Types valueStringArray: String[]")
  }

  @Test fun classBytes() {
    val bytes = Resources.toByteArray(Resources.getResource("Types.class"))
    val methods = DexParser.fromBytes(bytes)
        .listFields()
        .map { it.toString() }
    assertThat(methods).containsExactly(
        "Types valueBoolean: boolean",
        "Types valueByte: byte",
        "Types valueChar: char",
        "Types valueDouble: double",
        "Types valueFloat: float",
        "Types valueInt: int",
        "Types valueLong: long",
        "Types valueShort: short",
        "Types valueString: String",
        "Types valueStringArray: String[]")
  }

  @Test fun jarBytes() {
    val bytes = Resources.toByteArray(Resources.getResource("types.jar"))
    val methods = DexParser.fromBytes(bytes)
        .listFields()
        .map { it.toString() }
    assertThat(methods).containsExactly(
        "Types valueBoolean: boolean",
        "Types valueByte: byte",
        "Types valueChar: char",
        "Types valueDouble: double",
        "Types valueFloat: float",
        "Types valueInt: int",
        "Types valueLong: long",
        "Types valueShort: short",
        "Types valueString: String",
        "Types valueStringArray: String[]")
  }

  @Test fun aarExtractsJars() {
    val three = File(Resources.getResource("three.aar").file)
    val methods = DexParser.fromFiles(three)
        .listFields()
        .map { it.toString() }
    assertThat(methods).containsExactly(
        "Types valueBoolean: boolean",
        "Types valueByte: byte",
        "Types valueChar: char",
        "Types valueDouble: double",
        "Types valueFloat: float",
        "Types valueInt: int",
        "Types valueLong: long",
        "Types valueShort: short",
        "Types valueString: String",
        "Types valueStringArray: String[]",
        "Visibilities test1: String",
        "Visibilities test2: String",
        "Visibilities test3: String",
        "Visibilities test4: String")
  }
}