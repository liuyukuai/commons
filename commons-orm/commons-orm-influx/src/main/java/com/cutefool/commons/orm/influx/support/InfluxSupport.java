/*
 *
 */
package com.cutefool.commons.orm.influx.support;

import com.cutefool.commons.orm.influx.InfluxException;
import com.influxdb.client.BucketsApi;
import com.influxdb.client.OrganizationsApi;
import com.influxdb.client.domain.Bucket;
import com.influxdb.client.domain.Organization;
import com.influxdb.exceptions.UnprocessableEntityException;
import com.cutefool.commons.core.util.Lists;

import java.util.List;
import java.util.Objects;

/**
 * @author 271007729@qq.com
 * @date 11/2/21 2:50 PM
 */
public class InfluxSupport {

    public static synchronized String initOrganizations(OrganizationsApi organizationsApi, String name) {
        List<Organization> organizations = organizationsApi.findOrganizations();

        return Lists.empty(organizations)
                .stream()
                .filter(e -> Objects.equals(name, e.getName()))
                .findAny()
                .map(Organization::getId).orElseGet(() -> {
                    try {
                        return organizationsApi.createOrganization(name).getId();
                    } catch (UnprocessableEntityException e) {
                        throw new InfluxException(e.getLocalizedMessage());
                    }
                });
    }

    public static synchronized void initBuckets(BucketsApi bucketsApi, String name, String orgId) {
        Bucket bucket = bucketsApi.findBucketByName(name);
        if (Objects.isNull(bucket) || !Objects.equals(bucket.getOrgID(), orgId)) {
            try {
                bucketsApi.createBucket(name, orgId);
            } catch (Exception e) {
                throw new InfluxException(e.getLocalizedMessage());
            }
        }
    }
}
