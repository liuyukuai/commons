package com.cutefool.commons.app.um.lib.android;

import com.cutefool.commons.app.um.lib.AndroidNotification;

public class AndroidUnicast extends AndroidNotification {
	public AndroidUnicast(String appkey,String appMasterSecret) throws Exception {
			setAppMasterSecret(appMasterSecret);
			setPredefinedKeyValue("appkey", appkey);
			this.setPredefinedKeyValue("type", "unicast");
	}

	public void setDeviceToken(String token) throws Exception {
    	setPredefinedKeyValue("device_tokens", token);
    }

}
