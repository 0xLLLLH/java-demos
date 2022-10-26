package org.example;

import com.google.common.base.CaseFormat;

public class Main {
  public static void main(String[] args) {
    String text = CaseFormat.LOWER_UNDERSCORE.to(
        CaseFormat.UPPER_UNDERSCORE, "hello bazel");

    System.out.println(text);

    Greeting greeting = new Greeting();

    greeting.sayHi();
  }
}