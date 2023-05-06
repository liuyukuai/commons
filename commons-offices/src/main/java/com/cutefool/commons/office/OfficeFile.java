/*
 *
 */
package com.cutefool.commons.office;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * @author 271007729@qq.com
 * @date 8/4/21 5:35 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfficeFile {

    private String name;

    private File file;

    private int width;

    private int height;

    //XWPFDocument.PICTURE_TYPE_PNG

    private int documentType;

    private boolean hasBreak;

}
