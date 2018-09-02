package com.lambda;

import org.junit.Test;

import java.util.stream.IntStream;

/**
 * IntStream、LongStream、DoubleStream。
 * 当然我们也可以用 Stream<Integer>、Stream<Long> >、Stream<Double>，
 * 但是 boxing 和 unboxing 会很耗时，所以特别为这三种基本数值型提供了对应的 Stream。
 * Created by CJ on 2018/8/30.
 */
public class ILDStreamTest {
    @Test
    public void testIntStream(){
        IntStream.of(new int[]{1, 2, 3}).forEach(System.out::print);
        System.out.println();
        IntStream.range(1, 3).forEach(System.out::print);
        System.out.println();
        IntStream.rangeClosed(1, 3).forEach(System.out::print);
    }
}
