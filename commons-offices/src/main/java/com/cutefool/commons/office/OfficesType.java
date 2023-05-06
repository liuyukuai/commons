/*
 *
 */
package com.cutefool.commons.office;

import com.cutefool.commons.office.word.WordTemplate;
import com.cutefool.commons.office.xls.ExcelTemplate;

/**
 * @author 271007729@qq.com
 * @date 7/30/21 10:52 AM
 */
public interface OfficesType {
    /**
     * word
     *
     * @return word
     */
    WordTemplate word();

    /**
     * excel
     *
     * @return excel
     */
    ExcelTemplate excel();
}
