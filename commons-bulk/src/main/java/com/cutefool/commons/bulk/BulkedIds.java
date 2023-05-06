package com.cutefool.commons.bulk;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;


@Data
@SuppressWarnings("ALL")
public class BulkedIds {

    private boolean multiple;


    private Set ids;

    public BulkedIds() {
        this.multiple = true;
        this.ids = new HashSet<>();
    }
}
