/*
 *
 */
package com.cutefool.commons.orm.influx.support;

import com.cutefool.commons.orm.influx.InfluxProperties;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApi;

import java.util.Objects;

/**
 * @author 271007729@qq.com
 * @date 11/3/21 1:49 PM
 */
public class InfluxTemplate {

    private InfluxDBClient client;

    private WriteApi writeApi;
    private QueryApi queryApi;

    public InfluxTemplate(InfluxProperties influxProperties) {
        this.client = InfluxDBClientFactory.create(influxProperties.getUrl(), influxProperties.getToken().toCharArray());
//        String orgId = InfluxSupport.initOrganizations(this.client.getOrganizationsApi(), influxProperties.getUsername());
//        InfluxSupport.initBuckets(this.client.getBucketsApi(), influxProperties.getDatabase(), orgId);
    }

    public synchronized WriteApi writeApi() {
        if (Objects.isNull(writeApi)) {
            this.writeApi = this.client.getWriteApi();
        }
        return writeApi;
    }

    public synchronized QueryApi queryApi() {
        if (Objects.isNull(queryApi)) {
            this.queryApi = this.client.getQueryApi();
        }
        return queryApi;
    }


}
