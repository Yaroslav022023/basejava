package com.basejava.webapp;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Java8Streams {
    public static void main(String[] args) {
        int[] values = new int[]{1,2,3,2,3,1,5,2};
        System.out.println(minValue(values));

        List<Integer> listValues = Arrays.asList(1,2,4,6,10,55,26,172,324);
        System.out.println(oddOrEven(listValues));
    }

    private static int minValue(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce(0, (i,j) -> i * 10 + j);
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        int totalSum = integers.stream().mapToInt(Integer::intValue).sum();
        return integers.stream()
                .filter(totalSum % 2 == 0 ? n -> n % 2 == 0 : n -> n % 2 == 1)
                .collect(Collectors.toList());
    }
}