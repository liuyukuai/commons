/*
 *
 */
package com.cutefool.commons.ci.nexus;

import lombok.Data;

import java.util.List;

/**
 * @author 271007729@qq.com
 * @date 7/27/21 5:24 PM
 */
@Data
@SuppressWarnings({"unused", "WeakerAccess"})
public class NexusResponse {

    private List<Component> items;


}
