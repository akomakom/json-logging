/*
 * Copyright (c) 2022-2023 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.terracottatech.cloud.logging.json;

import ch.qos.logback.contrib.jackson.JacksonJsonFormatter;
import ch.qos.logback.contrib.json.classic.JsonLayout;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import junit.framework.TestCase;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Mathieu Carbou
 */
public class TerracottaJsonFormatterTest extends TestCase {

  private static final String STR = "\"/\\\b\f\n\r\t <>&azAZ09лицтя😀\uD83D\u003F\u0000\u001F\u0020\u007F\u009F\u0100\u2000\u20FF";

  private final ObjectMapper jackson = new ObjectMapper().configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
  private final JacksonJsonFormatter logbackJacksonFormatter = new JacksonJsonFormatter();

  public TerracottaJsonFormatterTest() {
    logbackJacksonFormatter.getObjectMapper().configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public void testToJsonStringMap() throws IOException {
    Map data = new HashMap();
    Key.SUPPORTED.forEach(k -> data.put(k, k));
    data.put(Key.ID, Collections.singletonMap(Key.PRODUCT, "Terracotta DB"));
    data.put(JsonLayout.MDC_ATTR_NAME, Collections.singletonMap("foo", "bar"));
    data.put("other", null);
    data.put(JsonLayout.EXCEPTION_ATTR_NAME, "my\nstack\ntrace");
    data.put(Key.ACCID, STR);

    String json = new TerracottaJsonFormatter().toJsonString(data);

    String comparisonWithJackson = jackson.writeValueAsString(data);
    String comparisonWithLogbackJsonFormatter = logbackJacksonFormatter.toJsonString(data);

    assertThat(json, is(equalTo(comparisonWithJackson)));
    assertThat(json, is(equalTo(comparisonWithLogbackJsonFormatter)));
  }

  public void testToJsonStringStr() {
    Stream.of(
        "",
        STR,
        "\u0000",
        "azerty\u0000",
        "\u0000azerty",
        "т",
        "\n",
        "a\nb"
    ).forEach(s -> {
      try {
        assertThat(
            s,
            new TerracottaJsonFormatter().toJsonString(s).toString(),
            is(equalTo(jackson.writeValueAsString(s))));
        assertThat(
            s,
            new TerracottaJsonFormatter().toJsonString(Collections.singletonMap("foo", s)).toString(),
            is(equalTo(logbackJacksonFormatter.toJsonString(Collections.singletonMap("foo", s)))));
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    });
  }
}