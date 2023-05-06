package com.cutefool.commons.app.um.lib.ios;

import com.cutefool.commons.app.um.lib.IOSNotification;
import org.json.JSONObject;

public class IOSGroupcast extends IOSNotification {
	public IOSGroupcast(String appkey,String appMasterSecret) throws Exception {
			setAppMasterSecret(appMasterSecret);
			setPredefinedKeyValue("appkey", appkey);
			this.setPredefinedKeyValue("type", "groupcast");
	}

	public void setFilter(JSONObject filter) throws Exception {
    	setPredefinedKeyValue("filter", filter);
    }
}
