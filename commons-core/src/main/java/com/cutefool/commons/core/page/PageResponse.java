package com.cutefool.commons.core.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import com.cutefool.commons.core.util.Lists;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * PageResponse
 *
 * @author 271007729@qq.com
 * @date 2019-07-02 10:45
 */

@Data
@SuppressWarnings({"unused"})
public class PageResponse<T> implements Serializable {

    private static final long serialVersionUID = -1816325636807144628L;
    /**
     * total
     */
    private Number total;

    /**
     * elements
     */
    private List<T> list;

    private PageResponse() {
    }

    private PageResponse(Number total, List<T> data) {
        this.total = BigInteger.valueOf(Objects.isNull(total) ? 0L : total.longValue());
        this.list = data == null || data.isEmpty() ? Collections.emptyList() : data;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return Objects.isNull(this.total) || Objects.equals(this.total.longValue(), 0L) || Lists.isEmpty(list);
    }

    public static <E> PageResponse<E> empty() {
        return new PageResponse<>(BigInteger.valueOf(0L), Collections.emptyList());
    }

    public static <E> PageResponse<E> apply(Number total, List<E> data) {
        BigInteger count = BigInteger.valueOf(Math.max(total.longValue(), 0L));
        return new PageResponse<>(total, data);
    }
}
