/*
 * SonarQube Java
 * Copyright (C) 2012-2020 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.java.resolve;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;

class ConvertTest {

  @Test
  void private_constructor() throws Exception {
    Constructor<Convert> constructor = Convert.class.getDeclaredConstructor();
    assertThat(constructor.isAccessible()).isFalse();
    constructor.setAccessible(true);
    constructor.newInstance();
  }

  @Test
  void packagePart() {
    assertThat(Convert.packagePart("org")).isEmpty();
    assertThat(Convert.packagePart("org.example")).isEqualTo("org");
  }

  @Test
  void shortName() {
    assertThat(Convert.shortName("org")).isEqualTo("org");
    assertThat(Convert.shortName("org.example")).isEqualTo("example");
    assertThat(Convert.shortName("org.example.MyClass$InnerClass")).isEqualTo("MyClass$InnerClass");
  }

  @Test
  void flatName() {
    assertThat(Convert.flatName("org/example/MyClass")).isEqualTo("org.example.MyClass");
    assertThat(Convert.flatName("org/example/MyClass$InnerClass")).isEqualTo("org.example.MyClass$InnerClass");
  }

  @Test
  void bytecodeName() {
    assertThat(Convert.bytecodeName("org.example.MyClass")).isEqualTo("org/example/MyClass");
    assertThat(Convert.bytecodeName("org.example.MyClass$InnerClass")).isEqualTo("org/example/MyClass$InnerClass");
  }

  @Test
  void enclosingClassName() throws Exception {
    assertThat(Convert.enclosingClassName("MyClass")).isEmpty();
    assertThat(Convert.enclosingClassName("MyClass$InnerClass")).isEqualTo("MyClass");
    assertThat(Convert.enclosingClassName("MyClass$$InnerClass$class")).isEqualTo("MyClass$");
    assertThat(Convert.enclosingClassName("MyClass$$InnerClass$")).isEqualTo("MyClass$");
  }

  @Test
  void innerClassName() throws Exception {
    String enclosingClassName = "MyClass";
    assertThat(Convert.innerClassName(enclosingClassName, "MyClass$InnerClass")).isEqualTo("InnerClass");
    assertThat(Convert.innerClassName(enclosingClassName, "MyClass$InnerClass$")).isEqualTo("InnerClass$");
    assertThat(Convert.innerClassName(enclosingClassName, "MyClass$InnerClass$class")).isEqualTo("InnerClass$class");
    assertThat(Convert.innerClassName(enclosingClassName, "MyClass$$InnerClass$")).isEqualTo("$InnerClass$");
    assertThat(Convert.innerClassName("Rules$ListResponse$Rule", "Rules$ListResponse$Rule$Builder")).isEqualTo("Builder");

  }

  @Test
  void fullName() throws Exception {
    assertThat(Convert.fullName(null, "MyClass")).isEqualTo("MyClass");
    assertThat(Convert.fullName("","MyClass")).isEqualTo("MyClass");
    assertThat(Convert.fullName("org.example", "MyClass")).isEqualTo("org.example.MyClass");
  }
}
