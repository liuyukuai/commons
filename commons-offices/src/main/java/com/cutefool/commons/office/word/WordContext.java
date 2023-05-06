/*
 *
 */
package com.cutefool.commons.office.word;

import com.cutefool.commons.office.Office;
import com.cutefool.commons.office.OfficesContext;

import java.io.File;

/**
 * @author 271007729@qq.com
 * @date 9/26/21 4:29 PM
 */
public class WordContext extends OfficesContext {

    private Office data;

    public Office getData() {
        return data;
    }

    public void setData(Office data) {
        this.data = data;
    }

    public WordContext(File source, File dest) {
        super(source, dest);
    }
}
