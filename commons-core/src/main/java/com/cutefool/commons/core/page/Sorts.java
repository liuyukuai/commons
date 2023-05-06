package com.cutefool.commons.core.page;

import com.cutefool.commons.core.Transfer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 271007729@qq.com
 * @date 2019-07-03 09:51
 */
@Slf4j
@SuppressWarnings({"unused"})
public class Sorts implements Transfer<String, String> {

    private static final Sorts instance = new Sorts();

    private Sorts() {
    }

    public static Sorts newInstance() {
        return instance;
    }


    public List<Sort> sorts(String sort) {
        return sorts(sort, null);

    }

    public List<Sort> sorts(String sort, Transfer<String, String> transfer) {
        if (StringUtils.isBlank(sort)) {
            return Collections.emptyList();
        }

        return Arrays.stream(sort.split(";")).map(s -> {
            try {
                boolean nullLast = false;
                String[] split = s.split("_");
                if (split.length == 3) {
                    String nullSort = split[2];
                    nullLast = Objects.equals(nullSort, "last");
                }
                if (split.length == 1) {
                    return Sort.desc(this.apply(split[0]));
                }
                String direction = split[1];
                direction = !Sort.DESC.equalsIgnoreCase(direction) && !Sort.ASC.equalsIgnoreCase(direction) ? Sort.DESC : direction;
                if (Objects.nonNull(transfer)) {
                    return new Sort(transfer.apply(split[0]), direction, nullLast);
                }
                return new Sort(this.apply(split[0]), direction, nullLast);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return null;
            }
        }).collect(Collectors.toList());

    }


    /**
     * 将排序的类型转换为数据库对应的值
     * 默认排序名称和数据库名称相同
     *
     * @param name 排序的名称
     * @return 返回数据库的名称
     */
    @Override
    public String apply(String name) {
        return name;
    }
}
