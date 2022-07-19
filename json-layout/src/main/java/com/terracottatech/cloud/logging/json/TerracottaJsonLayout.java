/*
 * Copyright (c) 2022-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */
package com.terracottatech.cloud.logging.json;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.json.classic.JsonLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TerracottaJsonLayout extends JsonLayout {

  private String file;
  private String product;

  @Override
  public void start() {
    validate();
    super.start();
  }

  private void validate() {
    Objects.requireNonNull(file);
    Objects.requireNonNull(product);
  }

  @Override
  protected void addCustomDataToJsonMap(Map<String, Object> map, ILoggingEvent logEvent) {
    String accid = System.getProperty("terracotta.cloud.logging.accid");
    if (accid != null) {
      map.put("accid", accid);
    }

    String envname = System.getProperty("terracotta.cloud.logging.envname");
    if (envname != null) {
      map.put("envname", envname);
    }

    map.put("file", getFile());

    Map<String, String> ctx = new HashMap<>();

    ctx.put("product", getProduct());

    String node = System.getProperty("terracotta.cloud.logging.node");
    if (node != null) {
      ctx.put("node", node);
    }

    String stripe = System.getProperty("terracotta.cloud.logging.stripe");
    if (stripe != null) {
      ctx.put("stripe", stripe);
    }

    String cluster = System.getProperty("terracotta.cloud.logging.cluster");
    if (cluster != null) {
      ctx.put("cluster", cluster);
    }
    map.put("id", ctx);
  }

  private String getFile() {
    return file;
  }

  public void setFile(final String file) {
    this.file = file;
  }

  private String getProduct() {
    return product;
  }

  public void setProduct(final String product) {
    this.product = product;
  }
}
