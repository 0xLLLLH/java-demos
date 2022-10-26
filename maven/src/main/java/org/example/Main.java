package org.example;

import com.google.common.base.CaseFormat;

public class Main {
  public static void main(String[] args) {
    String greeting = CaseFormat.LOWER_UNDERSCORE.to(
        CaseFormat.UPPER_UNDERSCORE, "hello maven");

    System.out.println(greeting);
  }
}