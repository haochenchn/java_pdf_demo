package com.lambda;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by CJ on 2018/8/30.
 */
public class LambdaTest {
    @Test
    public void test1(){
        List<Student> studentList = new ArrayList<Student>(){
            {
                add(new Student("stu1",100.0));
                add(new Student("stu2",97.0));
                add(new Student("stu4",85.0));
                add(new Student("stu3",96.0));
            }
        };
        /*Collections.sort(studentList, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return Double.compare(o1.getScore(),o2.getScore());
            }
        });*/
        /*Collections.sort(studentList,(Student o1, Student o2)-> Double.compare(o1.getScore(),o2.getScore()));
        System.out.println(studentList);*/

        List<String> stuList = studentList.stream()
                .filter(x->x.getScore()>85)
                .sorted(Comparator.comparing(Student::getScore).reversed())
                .map(Student::getName)
                .collect(Collectors.toList());
        System.out.println(stuList.toString());
    }
}