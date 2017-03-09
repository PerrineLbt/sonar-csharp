/*
 * SonarSource :: .NET :: Shared library
 * Copyright (C) 2016-2017 SonarSource SA
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
package org.sonarsource.dotnet.shared.plugins.protobuf;

import com.google.protobuf.Parser;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class RawProtobufImporter<T> {

  private final Parser<T> parser;

  RawProtobufImporter(Parser<T> parser) {
    this.parser = parser;
  }

  public void accept(Path protobuf) {
    try (InputStream inputStream = Files.newInputStream(protobuf)) {
      while (true) {
        T message = parser.parseDelimitedFrom(inputStream);
        if (message == null) {
          break;
        }
        consume(message);
      }
    } catch (IOException e) {
      throw new IllegalStateException("unexpected error while parsing protobuf file: " + protobuf, e);
    }
  }

  abstract void consume(T message);

}