package com.cutefool.commons.web.web;


import com.cutefool.commons.web.utils.WebUtils;
import com.cutefool.commons.bulk.BulkContext;
import com.cutefool.commons.bulk.BulkOperations;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.expression.Conditioning;
import com.cutefool.commons.mybatis.dsl.MybatisOperations;
import com.cutefool.commons.office.excels.Excels;
import com.cutefool.commons.office.excels.WriteContext;
import com.cutefool.commons.office.excels.libs.SheetLibs;
import com.cutefool.commons.orm.utils.Encrypts;
import com.cutefool.commons.spring.SpiSpringContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class RdsController {

    @GetMapping("/rds-download")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public ResponseEntity<org.springframework.core.io.Resource> imports(@RequestParam(name = "t") String t, Conditioning query) {
        Collection<MybatisOperations> mybatisOperations = SpiSpringContext.getSpi(MybatisOperations.class);
        Optional<MybatisOperations> optional = Lists.empty(mybatisOperations)
                .stream()
                .filter(e -> Objects.equals(t, Encrypts.encode(e.table())))
                .findAny();
        if (optional.isPresent()) {
            MybatisOperations operations = optional.get();
            String remarks = operations.remarks();
            List<?> list = operations.listByWhere(query);
            List<Object> bulking = BulkOperations.bulking(list, BulkContext.init());

            // 处理头
            Map<String, String> fieldsMap = operations.excelsRemarks();
            SheetLibs sheetLibs = SheetLibs.create(remarks, remarks, fieldsMap, bulking);
            WriteContext writeContext = WriteContext.create(remarks + ".xlsx", Collections.singletonList(sheetLibs));
            Excels.write(writeContext);
            return WebUtils.download(writeContext.getFile());
        }
        return ResponseEntity.notFound().build();
    }
}
