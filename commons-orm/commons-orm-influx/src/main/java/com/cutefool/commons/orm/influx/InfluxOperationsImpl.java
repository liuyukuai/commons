/*
 *
 */
package com.cutefool.commons.orm.influx;

import com.influxdb.annotations.Measurement;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.query.FluxTable;
import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.page.PageResponse;
import com.cutefool.commons.core.page.Paging;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.ProcessUtils;
import com.cutefool.commons.orm.influx.libs.InfluxLibs;
import com.cutefool.commons.orm.influx.query.InfluxQuery;
import com.cutefool.commons.orm.influx.support.InfluxResults;
import com.cutefool.commons.orm.influx.support.InfluxTemplate;
import com.cutefool.commons.orm.influx.support.Restrictions;
import com.cutefool.commons.snowflake.IdWorker;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author 271007729@qq.com
 * @date 11/1/21 5:11 PM
 */
public class InfluxOperationsImpl<DTO, E extends InfluxLibs, Q extends InfluxQuery> implements InfluxOperations<DTO, E, Q> {

    @Resource
    private InfluxTemplate influxTemplate;

    @Resource
    private InfluxProperties influxProperties;

    @Resource
    private IdWorker idWorker;

    @Override
    public BiConsumer<E, DTO> preCreate() {
        return (d, s) -> {
            d.setId(idWorker.nextId());
            if (Objects.isNull(d.getTime())) {
                d.setTime(Instant.now());
            }
        };
    }


    @Override
    public Restrictions restrictions() {
        return Restrictions.of(this.influxProperties.getDatabase());
    }

    @Override
    public String measurement() {
        return this.getMeasurement();
    }

    @Override
    public void create(DTO dto) {
        this.create(dto, (d, s) -> {
        });
    }

    @Override
    public void create(List<DTO> e) {
        this.create(e, (d, s) -> {
        });
    }

    @Override
    public void create(DTO dto, BiConsumer<E, DTO> consumer) {
        E process = process(dto, this.preCreate().andThen(consumer));
        WriteApi writeApi = influxTemplate.writeApi();
        writeApi.writeMeasurement(influxProperties.getDatabase(), influxProperties.getUsername(), WritePrecision.NS, process);
        this.postCreate().accept(process, dto);
        writeApi.flush();
    }

    @Override
    public void create(List<DTO> dtoList, BiConsumer<E, DTO> consumer) {
        if (Lists.iterable(dtoList)) {
            BiConsumer<E, DTO> biConsumer = this.preCreate();
            if (Objects.nonNull(consumer)) {
                biConsumer = this.preCreate().andThen(consumer);
            }
            List<E> es = ProcessUtils.processList(this.getDomainClass(), dtoList, biConsumer);
            WriteApi writeApi = influxTemplate.writeApi();
            writeApi.writeMeasurements(influxProperties.getDatabase(), influxProperties.getUsername(), WritePrecision.NS, es);
            for (int j = 0; j < es.size(); j++) {
                this.postCreate().accept(es.get(j), dtoList.get(j));
            }
            writeApi.flush();
        }
    }

    @Override
    public List<E> listByWhere(Q query) {
        Restrictions restrictions = this.restrictions(query);
        return this.list(restrictions);
    }

    @Override
    public List<E> list(Restrictions restrictions) {
        if (Objects.isNull(restrictions)) {
            return Collections.emptyList();
        }
        List<FluxTable> tables = this.influxTemplate.queryApi().query(restrictions.get(), influxProperties.getUsername());
        return InfluxResults.elements(tables, this.getDomainClass());
    }

    @Override
    public Long count(Q query) {
        Restrictions restrictions = this.restrictions(query).count();
        return this.count(restrictions);
    }

    @Override
    public Long count(Restrictions restrictions) {
        if (Objects.isNull(restrictions)) {
            return 0L;
        }
        List<Long> counts = this.influxTemplate.queryApi().query(restrictions.get(), influxProperties.getUsername(), Long.class);
        return Lists.empty(counts).stream().findAny().orElse(0L);
    }

    @Override
    public PageResponse<E> listByWhere(Q query, Paging paging) {
        return this.list(this.restrictions(query), paging);
    }

    @Override
    public PageResponse<E> list(Restrictions restrictions, Paging paging) {
        if (Objects.isNull(restrictions)) {
            return PageResponse.empty();
        }
        Long count = this.count(restrictions);
        restrictions = restrictions.page(paging);
        return PageResponse.apply(count, this.list(restrictions));
    }

    @Override
    @SuppressWarnings("ALL")
    public Class<E> getDomainClass() {
        Class cls = this.getClass();
        while (!Objects.equals(cls.getSuperclass(), InfluxOperationsImpl.class)) {
            cls = cls.getSuperclass();
        }
        ParameterizedType genericInterfaces = (ParameterizedType) cls.getGenericSuperclass();
        return (Class<E>) genericInterfaces.getActualTypeArguments()[1];
    }

    private Restrictions restrictions(Q query) {
        return this.restrictions().where(query).measurement(this.getMeasurement());
    }

    private String getMeasurement() {
        Class<E> domainClass = this.getDomainClass();
        Measurement annotation = domainClass.getAnnotation(Measurement.class);
        if (Objects.isNull(annotation)) {
            throw new BizException(" influx domain must has Measurement annotation");
        }
        return annotation.name();
    }
}
