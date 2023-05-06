/*
 *
 */
package com.cutefool.commons.ci.maven;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author 271007729@qq.com
 * @date 2022/9/14 9:33 PM
 */
public class DependencyComparator implements Comparator<String> {

    private final List<String> packages = Arrays.asList("com.cutefool", "org.springframework");

    @Override
    public int compare(String o1, String o2) {

        String a = o1;
        String b = o2;
        for (int i = 0; i < packages.size(); i++) {
            String s = packages.get(i);
            if (o1.startsWith(s)) {
                a = "a" + i + o1;
            }
            if (o2.startsWith(s)) {
                b = "a" + i + o2;
            }
        }
        return a.compareTo(b);
    }
}
