package com.lambda;

import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Created by CJ on 2018/8/30.
 */
public class StreamTest {
    /**
     * map把一种类型的流转换为另外一种类型的流
     * 将String数组中字母转换为小写
     */
    @Test
    public void testMap() {
        String[] arr = new String[]{"yes", "YES", "no", "NO"};
        Arrays.stream(arr).map(x -> x.toLowerCase()).forEach(System.out::println);
    }

    /**
     *  filter：过滤流，过滤流中的元素
     */
    @Test
    public void testFilter(){
        Integer[] arr = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        Arrays.stream(arr).filter(x->x>3&&x<8).forEach(System.out::println);
    }

    /**
     * flapMap：拆解流 将流中每一个元素拆解成一个流
     */
    @Test
    public void testFlapMap1() {
        String[] arr1 = {"a", "b", "c", "d"};
        String[] arr2 = {"e", "f", "c", "d"};
        String[] arr3 = {"h", "j", "c", "d"};
        // Stream.of(arr1, arr2, arr3).flatMap(x -> Arrays.stream(x)).forEach(System.out::println);
        Stream.of(arr1, arr2, arr3).flatMap(Arrays::stream).forEach(System.out::print);
    }

    String[] arr1 = {"abc","a","bc","abcd"};
    /**
     * Comparator.comparing是一个键提取的功能
     * 以下两个语句表示相同意义
     */
    @Test
    public void testSorted1_(){
        /**
         * 按照字符长度排序
         */
        Arrays.stream(arr1).sorted((x,y)->{
            if (x.length()>y.length())
                return 1;
            else if (x.length()<y.length())
                return -1;
            else
                return 0;
        }).forEach(System.out::println);
        Arrays.stream(arr1).sorted(Comparator.comparing(String::length)).forEach(System.out::println);
    }

    /**
     * 倒序
     * reversed(),java8泛型推导的问题，所以如果comparing里面是非方法引用的lambda表达式就没办法直接使用reversed()
     * Comparator.reverseOrder():也是用于翻转顺序，用于比较对象（Stream里面的类型必须是可比较的）
     * Comparator. naturalOrder()：返回一个自然排序比较器，用于比较对象（Stream里面的类型必须是可比较的）
     */
    @Test
    public void testSorted2_(){
        Arrays.stream(arr1).sorted(Comparator.comparing(String::length).reversed()).forEach(System.out::println);
        Arrays.stream(arr1).sorted(Comparator.reverseOrder()).forEach(System.out::println);
        Arrays.stream(arr1).sorted(Comparator.naturalOrder()).forEach(System.out::println);
    }

    /**
     * thenComparing
     * 先按照首字母排序
     * 之后按照String的长度排序
     */
    @Test
    public void testSorted3_(){
        Arrays.stream(arr1).sorted(Comparator.comparing(this::com1).thenComparing(String::length).reversed()).forEach(System.out::println);
    }
    public char com1(String x){
        return x.charAt(0);
    }
}
