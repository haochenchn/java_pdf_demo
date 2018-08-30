package com.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by CJ on 2018/8/29.
 */
public class Test1 {
    public static void main(String[] args) {
        List<String> proNames = Arrays.asList(new String[]{"Ni","Hao","Lambda"});

        List<String> lowercaseNames2 = proNames.stream().map(name -> name.toLowerCase()).collect(Collectors.toList());

        List<String> lowercaseNames3 = proNames.stream().map(String::toLowerCase).collect(Collectors.toList());
        System.out.println(lowercaseNames3.toString());
    }
}
