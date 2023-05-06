package com.cutefool.commons.orm.rds.cache.context;

import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Maps;
import com.cutefool.commons.orm.ColumnLibs;
import com.cutefool.commons.orm.rds.cache.RdsCache;
import com.cutefool.commons.orm.rds.config.RdsConfiguration;
import com.cutefool.commons.orm.rds.config.RdsDatabaseConfiguration;
import com.cutefool.commons.orm.rds.libs.TableLibs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Order
public class RdsContext implements CommandLineRunner {

    private final RdsCache rdsCache;

    private final RdsConfiguration rdsConfiguration;

    private final RdsDatabaseConfiguration rdsDatabaseConfiguration;

    public RdsContext(RdsCache rdsCache, RdsConfiguration rdsConfiguration, RdsDatabaseConfiguration rdsDatabaseConfiguration) {
        this.rdsCache = rdsCache;
        this.rdsConfiguration = rdsConfiguration;
        this.rdsDatabaseConfiguration = rdsDatabaseConfiguration;
    }

    public Map<String, List<ColumnLibs>> columns(String d) {
        return this.rdsCache.loadColumns(d);
    }

    public List<ColumnLibs> columns(String d, String table) {
        return Lists.empty(Maps.empty(columns(d)).get(table));
    }

    public List<ColumnLibs> columnsDefault(String table) {
        return columns(rdsConfiguration.getDb(), table);
    }

    public List<String> databases() {
        return Lists.empty(this.rdsCache.loadDatabases()).stream().filter(this::isContain).collect(Collectors.toList());
    }

    public boolean isContain(String name) {
        return (Lists.isEmpty(rdsDatabaseConfiguration.getExclusions()) || !rdsDatabaseConfiguration.getExclusions().contains(name))
                && (Lists.isEmpty(rdsDatabaseConfiguration.getIncludes()) || rdsDatabaseConfiguration.getIncludes().contains(name));
    }

    public List<TableLibs> tables() {
        return this.rdsCache.loadTables();
    }

    public Map<String, List<TableLibs>> groupByTables() {
        return Lists.empty(tables()).stream().filter(e -> isContain(e.getDatabase())).collect(Collectors.groupingBy(TableLibs::getDatabase));
    }

    public String db() {
        return rdsConfiguration.getDb();
    }

    @Override
    public void run(String... args) {
        // 查询database
        this.databases();
        this.groupByTables();
        // 初始化数据 (默认只加载当前数据库的所有数据）
        rdsCache.loadColumns(rdsConfiguration.getDb());
    }
}
