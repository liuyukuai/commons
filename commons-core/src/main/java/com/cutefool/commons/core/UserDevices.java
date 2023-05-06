/*
 *
 */
package com.cutefool.commons.core;

/**
 * 用户设备类型支持
 *
 * @author 271007729@qq.com
 * @date 12/22/20 5:20 PM
 */
@SuppressWarnings("unused")
public enum UserDevices {

    /**
     * PC
     */
    PC((byte) 0, "计算机"),

    /**
     * 移动设备
     */
    APP((byte) 1, "移动设备"),


    /**
     * 移动设备(教育培训)
     */
    APP_NXB((byte) 2, "移动设备(教育培训)"),


    /**
     * 未知
     */
    UNKNOWN((byte) -1, "未知设备");

    private Byte id;

    private String name;

    UserDevices(Byte id, String name) {
        this.id = id;
        this.name = name;
    }

    public Byte getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    /**
     * 通过type值获取类型
     *
     * @param type 对应的设备类型
     * @return 类型
     */
    public static String getName(Byte type) {
        UserDevices[] values = UserDevices.values();
        for (UserDevices value : values) {
            if (value.getId().equals(type)) {
                return value.getName();
            }
        }
        return UNKNOWN.getName();
    }

}
