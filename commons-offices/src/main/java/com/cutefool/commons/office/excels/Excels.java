package com.cutefool.commons.office.excels;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.export.ExcelExportService;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Maps;
import com.cutefool.commons.office.excels.libs.SheetLibs;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Excels工具类
 */
@Slf4j
public final class Excels {

    public static void write(WriteContext context) {
        try (FileOutputStream fos = new FileOutputStream(context.getFile()); Workbook workbook = new HSSFWorkbook();) {
            List<SheetLibs> sheets = context.getSheets();
            Lists.empty(sheets)
                    .stream()
                    .filter(e -> Lists.iterable(e.getData()))
                    .forEach(sheetLibs -> {
                        Object o = sheetLibs.getData().get(0);
                        ExcelExportService excelExportService = new ExcelExportService();
                        if (o instanceof Map) {
                            List<ExcelExportEntity> entities = Maps.empty(sheetLibs.getMapping())
                                    .entrySet()
                                    .stream()
                                    .map(x -> new ExcelExportEntity(x.getValue(), x.getKey()))
                                    .collect(Collectors.toList());
                            excelExportService.createSheetForMap(workbook, new ExportParams(sheetLibs.getTitle(), sheetLibs.getName()), entities, sheetLibs.getData());
                        } else {
                            excelExportService.createSheet(workbook, new ExportParams(sheetLibs.getTitle(), sheetLibs.getName()), o.getClass(), sheetLibs.getData());
                        }
                    });
            workbook.write(fos);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
