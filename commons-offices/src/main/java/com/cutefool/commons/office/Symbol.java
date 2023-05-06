/*
 *
 */
package com.cutefool.commons.office;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 271007729@qq.com
 * @date 12/13/21 3:04 PM
 */
@Data
@AllArgsConstructor
public class Symbol {

    private boolean left;

    private boolean right;

    private String name;

    private int index;
}
