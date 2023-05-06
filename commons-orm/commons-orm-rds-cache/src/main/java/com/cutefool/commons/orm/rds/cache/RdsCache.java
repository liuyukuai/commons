package com.cutefool.commons.orm.rds.cache;

import com.cutefool.commons.cache.annotion.ExpiredCaching;
import com.cutefool.commons.cache.annotion.LocalExpireCaching;
import com.cutefool.commons.cache.annotion.RemoteExpireCaching;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Maps;
import com.cutefool.commons.orm.ColumnLibs;
import com.cutefool.commons.orm.rds.libs.TableLibs;
import com.cutefool.commons.orm.rds.meta.RdsMetaOperations;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ExpiredCaching
public class RdsCache {

    private final RdsMetaOperations rdsMetaOperations;

    public RdsCache(RdsMetaOperations rdsMetaOperations) {
        this.rdsMetaOperations = rdsMetaOperations;
    }

    @LocalExpireCaching(key = "#d", value = "rds-columns", expireSecond = 60 * 60)
    public Map<String, List<ColumnLibs>> loadColumns(String d) {
        try {
            List<ColumnLibs> columnLibs = this.rdsMetaOperations.columnsWithSchema(d);
            return Lists.empty(columnLibs).stream().collect(Collectors.groupingBy(ColumnLibs::getTableName));
        } catch (Exception e) {
            return Maps.hashMap();
        }
    }

    @LocalExpireCaching(value = "rds-dbs", expireSecond = 60 * 60)
    public List<String> loadDatabases() {
        return this.rdsMetaOperations.databases();
    }

    @LocalExpireCaching(value = "rds-tables", expireSecond = 60 * 60)
    public List<TableLibs> loadTables() {
        return this.rdsMetaOperations.tables();
    }
}
