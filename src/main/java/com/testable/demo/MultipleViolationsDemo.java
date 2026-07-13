package com.testable.demo;

public class MultipleViolationsDemo {

  private int bad_field;

  public void messyMethod(int X) {
    int unused = 10;
    int y=5+3;
      if(X>0){
    System.out.println(y);
      }
    int z = unused;
  }
}
