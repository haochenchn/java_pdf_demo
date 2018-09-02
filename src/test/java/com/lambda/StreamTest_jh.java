package com.lambda;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by CJ on 2018/8/30.
 */
public class StreamTest_jh {
    String[] arr;

    @Before
    public void init(){
        arr = new String[]{"b","ab","abc","abcd","abcde","abcd"};
    }

    /**
     * max、min
     * 最大最小值
     */
    @Test
    public void testMaxAndMin(){
        /*Stream.of(arr).max(Comparator.comparing(String::length)).ifPresent(System.out::println);
        Stream.of(arr).min(Comparator.comparing(String::length)).ifPresent(System.out::println);
        Arrays.asList(arr).stream().forEach(x -> System.out.println(x));*/

        Stream<String> stream = Stream.of(arr);
        // 1. Array
        //String[] strArray1 = stream.toArray(String[]::new);
        // 2. Collection
        List<String> list1 = stream.collect(Collectors.toList());
        //List<String> list2 = stream.collect(Collectors.toCollection(ArrayList::new));
        //Set set1 = stream.collect(Collectors.toSet());
        //Stack stack1 = stream.collect(Collectors.toCollection(Stack::new));
        // 3. String
        //String str = stream.collect(Collectors.joining()).toString();

        System.out.println(list1.toString());
    }

    /**
     * map/flatMap 它的作用就是把 input Stream 的每一个元素，映射成 output Stream 的另外一个元素。
     * 还有一些场景，是一对多映射关系的，这时需要 flatMap。
     * flatMap 把 input Stream 中的层级结构扁平化，就是将最底层元素抽出来放到一起，最终 output 的新 Stream 里面已经没有 List 了，都是直接的数字。
     */
    @Test
    public void map(){
        //List<String> output = stream.map(String :: toUpperCase).collect(Collectors.toList());

        Stream<List<Integer>> inputStream = Stream.of(
                Arrays.asList(1),
                Arrays.asList(2, 3),
                Arrays.asList(4, 5, 6)
        );
        Stream<Integer> outputStream = inputStream.
                flatMap((childList) -> childList.stream());
        List output = outputStream.collect(Collectors.toCollection(ArrayList::new));
        System.out.println(output.toString());
    }

    /**
     * filter 对原始 Stream 进行某项测试，通过测试的元素被留下来生成一个新 Stream。
     */
    @Test
    public void filter(){
        Integer[] sixNums = {1, 2, 3, 4, 5, 6};
        //留下偶数
        Stream.of(sixNums).filter(n -> n%2 == 0).forEach(x -> System.out.print(x));

        //首先把每行的单词用 flatMap 整理到新的 Stream，然后保留长度不为 0 的，就是整篇文章中的全部单词了
        /*List<String> output = reader.lines().
                flatMap(line -> Stream.of(line.split(REGEXP))).
                filter(word -> word.length() > 0).
                collect(Collectors.toList());*/
    }

    /**
     * forEach 是 terminal 操作，因此它执行后，Stream 的元素就被“消费”掉了，
     * 你无法对一个 Stream 进行两次 terminal 运算。
     */
    @Test
    public void forEach(){
        /*// Java 8
        roster.stream()
                .filter(p -> p.getGender() == Person.Sex.MALE)
                .forEach(p -> System.out.println(p.getName()));
        // Pre-Java 8
        for (Person p : roster) {
            if (p.getGender() == Person.Sex.MALE) {
                System.out.println(p.getName());
            }
        }*/

        //相反，具有相似功能的 intermediate 操作 peek 可以达到上述目的。
        //peek 对每个元素执行操作并返回一个新的 Stream
        Stream.of("one", "two", "three", "four")
                .filter(e -> e.length() > 3)
                .peek(e -> System.out.println("Filtered value: " + e))
                .map(String::toUpperCase)
                .peek(e -> System.out.println("Mapped value: " + e))
                .collect(Collectors.toList());
    }

    /**
     * 这是一个 termimal 兼 short-circuiting 操作，它总是返回 Stream 的第一个元素，或者空。
     * @param text
     */
    @Test
    public void findFirst(){
        String strA = " abcd ", strB = null;
        print(strA);
        print("");
        print(strB);
        getLength(strA);
        getLength("");
        getLength(strB);
    }

    public void print(String text) {
        // Java 8
        Optional.ofNullable(text).ifPresent(System.out::println);
        // Pre-Java 8
        if (text != null) {
            System.out.println(text);
        }
    }
    public int getLength(String text) {
        // Java 8
        return Optional.ofNullable(text).map(String::length).orElse(-1);
        // Pre-Java 8
        // return if (text != null) ? text.length() : -1;
    }



    /**
     * count
     * 计算数量
     */
    @Test
    public void testCount(){
        long count = Stream.of(arr).count();
        System.out.println(count);
    }

    /**
     * findFirst
     * 查找第一个
     */
    @Test
    public void testFindFirst(){
        String str =  Stream.of(arr).parallel().filter(x->x.length()>3).findFirst().orElse("noghing");
        System.out.println(str);
    }

    /**
     * findAny
     * 找到所有匹配的元素
     * 对并行流十分有效
     * 只要在任何片段发现了第一个匹配元素就会结束整个运算
     */
    @Test
    public void testFindAny(){
        Optional<String> optional = Stream.of(arr).parallel().filter(x->x.length()>3).findAny();
        optional.ifPresent(System.out::println);
    }

    /**
     * anyMatch
     * 是否含有匹配元素
     */
    @Test
    public void testAnyMatch(){
        Boolean aBoolean = Stream.of(arr).anyMatch(x->x.startsWith("a"));
        System.out.println(aBoolean);
    }


    @Test
    public void testStream1() {
        Optional<Integer> optional = Stream.of(1,2,3).filter(x->x>1).reduce((x, y)->x+y);
        System.out.println(optional.get());
    }
}
