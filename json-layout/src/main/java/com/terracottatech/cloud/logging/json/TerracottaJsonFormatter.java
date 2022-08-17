/*
 * Copyright (c) 2022-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */
package com.terracottatech.cloud.logging.json;

import ch.qos.logback.contrib.json.JsonFormatter;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Mathieu Carbou
 */
class TerracottaJsonFormatter implements JsonFormatter {

  private static final String[] ESCAPES = new String[93];

  static {
    // https://www.ietf.org/rfc/rfc4627.txt
    for (int c = 0; c <= 0x1f; c++) {
      ESCAPES[c] = String.format("\\u%04X", c);
    }
    ESCAPES['\b'] = "\\b"; // #8
    ESCAPES['\t'] = "\\t"; // #9
    ESCAPES['\n'] = "\\n"; // #10
    ESCAPES['\f'] = "\\f"; // #12
    ESCAPES['\r'] = "\\r"; // #13
    ESCAPES['"'] = "\\\""; // #34
    ESCAPES['\\'] = "\\\\"; // #92
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public String toJsonString(Map m) {
    return "{" + ((Map<String, ?>) m).entrySet().stream()
        .sorted(Map.Entry.comparingByKey()) // json map entries are usually sorted by key to help readability amongst other things
        .map(e -> toJsonString(e.getKey()) + ":" + toJsonString(e.getValue()))
        .collect(Collectors.joining(",")) + "}";
  }

  @SuppressWarnings("rawtypes")
  private CharSequence toJsonString(Object value) {
    if (value == null) {
      return "null";
    } else if (value instanceof CharSequence) {
      return toJsonString((CharSequence) value);
    } else if (value instanceof Map) {
      return toJsonString((Map) value);
    } else {
      // Case where the incoming map would contain an unsupported value or type.
      // This should never be the case but if it happens we shouldn't blindly discard the data.
      return toJsonString(String.valueOf(value));
    }
  }

  CharSequence toJsonString(CharSequence s) {
    final int length = s.length();
    if (length == 0) {
      return "\"\"";
    } else {
      // The code below will iterate over all characters of s.
      // If no replacement is needed, s is returned.
      // If at least one is needed, s is reconstructed with the replacement(s)
      StringBuilder replacement = null;
      String escaped;
      int last = 0;
      for (int i = 0; i < length; i++) {
        final char c = s.charAt(i);
        // Do we have a replacement to do ?
        if (c < ESCAPES.length && (escaped = ESCAPES[c]) != null) {
          // First ensure buffer is initialized
          if (replacement == null) {
            replacement = new StringBuilder(length + 1);
          }
          // First copy the previous characters that didn't need a replacement
          for (; last < i; last++) {
            replacement.append(s.charAt(last));
          }
          // append the replacement
          replacement.append(escaped);
          last = i + 1;
        }
      }
      // make sure not replaced chars are copied
      if (replacement != null) {
        for (; last < length; last++) {
          replacement.append(s.charAt(last));
        }
      }
      // return the original or replacement
      return "\"" + (replacement == null ? s : replacement) + "\"";
    }
  }
}
