/*
 *  
 */
package com.cutefool.commons.office.xls;

import com.cutefool.commons.office.OfficesContext;
import com.cutefool.commons.office.OfficesTemplate;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * @author 271007729@qq.com
 * @date 9/26/21 3:36 PM
 */
public class ExcelTemplate implements OfficesTemplate<HSSFWorkbook> {

    private OfficesContext context;

    public ExcelTemplate(OfficesContext context) {
        this.context = context;
    }


    @Override
    public HSSFWorkbook open() throws IOException {
        File source = context.getSource();

        if (Objects.isNull(source)) {
            return new HSSFWorkbook();
        }

        try (FileInputStream is = new FileInputStream(source)) {
            return new HSSFWorkbook(is);
        }
    }

    @Override
    public void doWrite(HSSFWorkbook document) throws IOException {
        File dest = context.getDest();
        try (FileOutputStream is = new FileOutputStream(dest)) {
            document.write(is);
        }
    }


    public boolean write() {
        try (HSSFWorkbook workbook = this.open()) {
            // 替换段落
            // 处理表格
            this.doWrite(workbook);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
