package com.testable.demo;

import java.util.List;
import com.testable.demo.MagicNumberDemo;

public class ImportOrderDemo {

    public int sum(List<Integer> values) {
        int total = 0;
        for (Integer value : values) {
            total += value;
        }
        return total;
    }
}
