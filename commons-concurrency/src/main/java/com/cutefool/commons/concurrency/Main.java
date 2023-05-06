/*
 *  
 */
package com.cutefool.commons.concurrency;

import lombok.ToString;
import com.cutefool.commons.core.GroupId;

import java.util.HashSet;

/**
 * @author 271007729@qq.com
 * @date 8/31/21 4:46 P
 */
public class Main {

    public static void main(String[] args) {
        HashSet<String> sets = new HashSet<>();

        System.out.println(sets.add("1"));
        System.out.println(sets.add("1"));

//        LocalDateTime before = LocalDateTime.now();
//
//        Concurrency<Num> context = new Concurrency<>(10L, false);
//
//        for (int i = 0; i < 100; i++) {
//            Num num = new Num(i);
//            context.put(num);
//        }
//
//        context.startup((id, num) -> {
//            try {
//                TimeUnit.SECONDS.sleep(1);
//                System.out.println(id + "-->" + num);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//
//        context.isDown(() -> {
//            System.out.println("end.");
//        });
//
//
//        LocalDateTime after = LocalDateTime.now();
//
//        long seconds = Duration.between(before, after).getSeconds();
//        System.out.println(seconds);
    }

    @ToString
    public static final class Num implements GroupId {

        private int v;

        public Num(int v) {
            this.v = v;
        }

        @Override
        public long group(Long num) {
            return v % num;
        }
    }
}
