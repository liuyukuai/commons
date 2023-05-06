//package com.cutefool.commons.app.um.lib;
//
//
//import com.cutefool.commons.app.um.lib.android.*;
//import com.cutefool.commons.app.um.lib.ios.*;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//
//public class Demo {
//	private String appkey = "5e93d504895ccaf90500016d";
//	private String appMasterSecret = "y5ms2up4b4demcmgj4qrllylyxvruq7d";
//	private String timestamp = null;
//	private PushClient client = new PushClient();
//
//	public Demo(String key, String secret) {
//		try {
//			appkey = key;
//			appMasterSecret = secret;
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//	}
//
//	public void sendAndroidBroadcast() throws Exception {
//		AndroidBroadcast broadcast = new AndroidBroadcast(appkey,appMasterSecret);
//		broadcast.setTicker( "Android broadcast ticker");
//		broadcast.setTitle(  "中文的title");
//		broadcast.setText(   "Android broadcast text");
//		broadcast.goAppAfterOpen();
//		broadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
//		// TODO Set 'production_mode' to 'false' if it's a test device.
//		// For how to register a test device, please see the developer doc.
//		broadcast.setProductionMode();
//		// Set customized fields
//		broadcast.setExtraField("test", "helloworld");
//		//厂商通道相关参数
//		broadcast.setChannelActivity("your channel activity");
//		broadcast.setChannelProperties("abc");
//		client.send(broadcast);
//	}
//
//	public void sendAndroidUnicast() throws Exception {
//		AndroidUnicast unicast = new AndroidUnicast(appkey,appMasterSecret);
//		// TODO Set your device token
//		unicast.setDeviceToken("Ak-UPrSM5Zi1v_zP7BA3-59dNtmQtV8Q7X2nsycBn_K2\n");
//		unicast.setTicker( "happy的测试199");
//		unicast.setTitle(  "happy的测试199");
//		unicast.setText(   "Android unicast text");
//		unicast.goAppAfterOpen();
//		unicast.setBadge(null);
//		unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
//		// TODO Set 'production_mode' to 'false' if it's a test device.
//		// For how to register a test device, please see the developer doc.
//		unicast.setProductionMode();
//		// Set customized fields
//		unicast.setExtraField("test", "helloworld");
//		unicast.setChannelActivity("your channel activity");
//		unicast.setChannelProperties("com.tsingyun.yangnong.activity.SplashActivity");
//		client.send(unicast);
//	}
//
//	public void sendAndroidGroupcast() throws Exception {
//		AndroidGroupcast groupcast = new AndroidGroupcast(appkey,appMasterSecret);
//		/*  TODO
//		 *  Construct the filter condition:
//		 *  "where":
//		 *	{
//    	 *		"and":
//    	 *		[
//      	 *			{"tag":"test"},
//      	 *			{"tag":"Test"}
//    	 *		]
//		 *	}
//		 */
//		JSONObject filterJson = new JSONObject();
//		JSONObject whereJson = new JSONObject();
//		JSONArray tagArray = new JSONArray();
//		JSONObject testTag = new JSONObject();
//		JSONObject TestTag = new JSONObject();
//		testTag.put("tag", "test");
//		TestTag.put("tag", "Test");
//		tagArray.put(testTag);
//		tagArray.put(TestTag);
//		whereJson.put("and", tagArray);
//		filterJson.put("where", whereJson);
//
//		groupcast.setFilter(filterJson);
//		groupcast.setTicker( "Android groupcast ticker");
//		groupcast.setTitle(  "中文的title");
//		groupcast.setText(   "Android groupcast text");
//		groupcast.goAppAfterOpen();
//		groupcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
//		groupcast.setChannelActivity("your channel activity");
//		// TODO Set 'production_mode' to 'false' if it's a test device.
//		// For how to register a test device, please see the developer doc.
//		groupcast.setProductionMode();
//		//厂商通道相关参数
//		groupcast.setChannelActivity("your channel activity");
//		groupcast.setChannelProperties("abc");
//		client.send(groupcast);
//	}
//
//	public void sendAndroidCustomizedcast() throws Exception {
//		AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(appkey,appMasterSecret);
//		// TODO Set your alias here, and use comma to split them if there are multiple alias.
//		// And if you have many alias, you can also upload a file containing these alias, then
//		// use file_id to send customized notification.
//		customizedcast.setAlias("alias", "alias_type");
//		customizedcast.setTicker( "Android customizedcast ticker");
//		customizedcast.setTitle(  "中文的title");
//		customizedcast.setText(   "Android customizedcast text");
//		customizedcast.goAppAfterOpen();
//		customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
//		// TODO Set 'production_mode' to 'false' if it's a test device.
//		// For how to register a test device, please see the developer doc.
//		customizedcast.setProductionMode();
//		//厂商通道相关参数
//		customizedcast.setChannelActivity("your channel activity");
//		customizedcast.setChannelProperties("abc");
//		client.send(customizedcast);
//	}
//
//	public void sendAndroidCustomizedcastFile() throws Exception {
//		AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(appkey,appMasterSecret);
//		// TODO Set your alias here, and use comma to split them if there are multiple alias.
//		// And if you have many alias, you can also upload a file containing these alias, then
//		// use file_id to send customized notification.
//		String fileId = client.uploadContents(appkey,appMasterSecret,"aa"+"\n"+"bb"+"\n"+"alias");
//		customizedcast.setFileId(fileId, "alias_type");
//		customizedcast.setTicker( "Android customizedcast ticker");
//		customizedcast.setTitle(  "中文的title");
//		customizedcast.setText(   "Android customizedcast text");
//		customizedcast.goAppAfterOpen();
//		customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
//		// TODO Set 'production_mode' to 'false' if it's a test device.
//		// For how to register a test device, please see the developer doc.
//		customizedcast.setProductionMode();
//		//厂商通道相关参数
//		customizedcast.setChannelActivity("your channel activity");
//		customizedcast.setChannelProperties("abc");
//		client.send(customizedcast);
//	}
//
//	public void sendAndroidFilecast() throws Exception {
//		AndroidFilecast filecast = new AndroidFilecast(appkey,appMasterSecret);
//		// TODO upload your device tokens, and use '\n' to split them if there are multiple tokens
//		String fileId = client.uploadContents(appkey,appMasterSecret,"aa"+"\n"+"bb");
//		filecast.setFileId( fileId);
//		filecast.setTicker( "Android filecast ticker");
//		filecast.setTitle(  "中文的title");
//		filecast.setText(   "Android filecast text");
//		filecast.goAppAfterOpen();
//		filecast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
//		//厂商通道相关参数
//		filecast.setChannelActivity("your channel activity");
//		filecast.setChannelProperties("abc");
//		client.send(filecast);
//	}
//
//	public void sendIOSBroadcast() throws Exception {
//		IOSBroadcast broadcast = new IOSBroadcast(appkey,appMasterSecret);
//        //alert值设置为字符串
//		//broadcast.setAlert("IOS 广播测试");
//		//alert的值设置为字典
//		broadcast.setAlert("今日天气" , "" , "今日可能下雨🌂");
//		broadcast.setBadge( 0);
//		broadcast.setSound( "default");
//		// TODO set 'production_mode' to 'true' if your app is under production mode
//		broadcast.setTestMode();
//		// Set customized fields
//		broadcast.setCustomizedField("test", "helloworld");
//		client.send(broadcast);
//	}
//
//	public void sendIOSUnicast() throws Exception {
//		IOSUnicast unicast = new IOSUnicast(appkey,appMasterSecret);
//		// TODO Set your device token
//		unicast.setDeviceToken( "your device_token");
//		//alert值设置为字符串
//		//unicast.setAlert("IOS 单播测试");
//		//alert的值设置为字典
//		unicast.setAlert("今日天气" , "" , "今日可能下雨🌂");
//		unicast.setBadge( 0);
//		unicast.setSound( "default");
//		// TODO set 'production_mode' to 'true' if your app is under production mode
//		unicast.setTestMode();
//		// Set customized fields
//		unicast.setCustomizedField("test", "helloworld");
//		client.send(unicast);
//	}
//
//
//	public void sendIOSGroupcast() throws Exception {
//		IOSGroupcast groupcast = new IOSGroupcast(appkey,appMasterSecret);
//		/*  TODO
//		 *  Construct the filter condition:
//		 *  "where":
//		 *	{
//    	 *		"and":
//    	 *		[
//      	 *			{"tag":"iostest"}
//    	 *		]
//		 *	}
//		 */
//		JSONObject filterJson = new JSONObject();
//		JSONObject whereJson = new JSONObject();
//		JSONArray tagArray = new JSONArray();
//		JSONObject testTag = new JSONObject();
//		testTag.put("tag", "iostest");
//		tagArray.put(testTag);
//		whereJson.put("and", tagArray);
//		filterJson.put("where", whereJson);
//		System.out.println(filterJson.toString());
//
//		// Set filter condition into rootJson
//		groupcast.setFilter(filterJson);
//		//groupcast.setAlert("IOS 组播测试");
//		//alert的值设置为字典
//		groupcast.setAlert("今日天气" , "subtitle" , "今日可能下雨🌂");
//		groupcast.setBadge( 0);
//		groupcast.setSound( "default");
//		// TODO set 'production_mode' to 'true' if your app is under production mode
//		groupcast.setTestMode();
//		client.send(groupcast);
//	}
//
//	public void sendIOSCustomizedcast() throws Exception {
//		IOSCustomizedcast customizedcast = new IOSCustomizedcast(appkey,appMasterSecret);
//		// TODO Set your alias and alias_type here, and use comma to split them if there are multiple alias.
//		// And if you have many alias, you can also upload a file containing these alias, then
//		// use file_id to send customized notification.
//		customizedcast.setAlias("alias", "alias_type");
//		//customizedcast.setAlert("IOS 个性化测试");
//		//alert的值设置为字典
//		customizedcast.setAlert("今日天气" , "" , "今日可能下雨🌂");
//		customizedcast.setBadge( 0);
//		customizedcast.setSound( "default");
//		// TODO set 'production_mode' to 'true' if your app is under production mode
//		customizedcast.setTestMode();
//		client.send(customizedcast);
//	}
//
//	public void sendIOSFilecast() throws Exception {
//		IOSFilecast filecast = new IOSFilecast(appkey,appMasterSecret);
//		// TODO upload your device tokens, and use '\n' to split them if there are multiple tokens
//		String fileId = client.uploadContents(appkey,appMasterSecret,"aa"+"\n"+"bb");
//		filecast.setFileId( fileId);
//		//filecast.setAlert("IOS 文件播测试");
//		//alert的值设置为字典
//		filecast.setAlert("今日天气" , "" , "今日可能下雨🌂");
//		filecast.setBadge( 0);
//		filecast.setSound( "default");
//		// TODO set 'production_mode' to 'true' if your app is under production mode
//		filecast.setTestMode();
//		client.send(filecast);
//	}
//
//	public static void main(String[] args) {
//		// TODO set your appkey and master secret here
//		Demo demo = new Demo("5f841d8af472fc6b58b04da0", "iy5a2gfezfemcbxmbky1gtc6xl4udl2p");
//		try {
//			demo.sendAndroidUnicast();
//			/* TODO these methods are all available, just fill in some fields and do the test
//			 * demo.sendAndroidCustomizedcastFile();
//			 * demo.sendAndroidBroadcast();
//			 * demo.sendAndroidGroupcast();
//			 * demo.sendAndroidCustomizedcast();
//			 * demo.sendAndroidFilecast();
//			 *
//			 * demo.sendIOSBroadcast();
//			 * demo.sendIOSUnicast();
//			 * demo.sendIOSGroupcast();
//			 * demo.sendIOSCustomizedcast();
//			 * demo.sendIOSFilecast();
//			 */
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//
//
//}
