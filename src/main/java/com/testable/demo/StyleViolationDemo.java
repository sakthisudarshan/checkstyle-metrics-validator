package com.testable.demo;

public class StyleViolationDemo {

  public void badlyIndented() {
    int x=1+2;
      int y = 3;
    if(x>0){
    System.out.println(x);
    }
  }
}
