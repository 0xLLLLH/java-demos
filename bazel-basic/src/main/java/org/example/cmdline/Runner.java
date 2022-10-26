package org.example.cmdline;

import org.example.Greeting;

public class Runner {
  public static void main(String[] args) {
    System.out.println("From Runner:");
    Greeting greeting = new Greeting();

    greeting.sayHi();
  }
}
