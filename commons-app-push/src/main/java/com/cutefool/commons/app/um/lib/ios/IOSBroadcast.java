package com.cutefool.commons.app.um.lib.ios;

import com.cutefool.commons.app.um.lib.IOSNotification;

public class IOSBroadcast extends IOSNotification {
	public IOSBroadcast(String appkey,String appMasterSecret) throws Exception {
			setAppMasterSecret(appMasterSecret);
			setPredefinedKeyValue("appkey", appkey);
			this.setPredefinedKeyValue("type", "broadcast");

	}
}
