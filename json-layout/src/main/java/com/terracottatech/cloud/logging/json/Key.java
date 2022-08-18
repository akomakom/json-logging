/*
 * Copyright (c) 2022-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */
package com.terracottatech.cloud.logging.json;

import java.util.Collection;
import java.util.HashSet;

import static ch.qos.logback.contrib.json.classic.JsonLayout.CONTEXT_ATTR_NAME;
import static ch.qos.logback.contrib.json.classic.JsonLayout.EXCEPTION_ATTR_NAME;
import static ch.qos.logback.contrib.json.classic.JsonLayout.FORMATTED_MESSAGE_ATTR_NAME;
import static ch.qos.logback.contrib.json.classic.JsonLayout.LEVEL_ATTR_NAME;
import static ch.qos.logback.contrib.json.classic.JsonLayout.LOGGER_ATTR_NAME;
import static ch.qos.logback.contrib.json.classic.JsonLayout.MDC_ATTR_NAME;
import static ch.qos.logback.contrib.json.classic.JsonLayout.MESSAGE_ATTR_NAME;
import static ch.qos.logback.contrib.json.classic.JsonLayout.THREAD_ATTR_NAME;
import static ch.qos.logback.contrib.json.classic.JsonLayout.TIMESTAMP_ATTR_NAME;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;

/**
 * @author Mathieu Carbou
 */
class Key {
  public static final String ACCID = "accid";
  public static final String CLUSTER = "cluster";
  public static final String ENVNAME = "envname";
  public static final String FILE = "file";
  public static final String ID = "id";
  public static final String MARKER = "marker";
  public static final String NODE = "node";
  public static final String PRODUCT = "product";
  public static final String STRIPE = "stripe";

  public static final Collection<String> SUPPORTED = unmodifiableCollection(new HashSet<>(asList(
      ACCID,
      CLUSTER,
      ENVNAME,
      FILE,
      ID, // Map<String, String>
      MARKER,
      NODE,
      PRODUCT,
      STRIPE,
      CONTEXT_ATTR_NAME,
      EXCEPTION_ATTR_NAME,
      FORMATTED_MESSAGE_ATTR_NAME,
      LEVEL_ATTR_NAME,
      LOGGER_ATTR_NAME,
      MDC_ATTR_NAME, // Map<String, String>
      MESSAGE_ATTR_NAME,
      THREAD_ATTR_NAME,
      TIMESTAMP_ATTR_NAME
  )));
}
