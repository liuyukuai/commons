/*
 *
 */
package com.cutefool.commons.sms.unicom;

import lombok.Data;

/**
 * 短信发送请求
 *
 * @author 271007729@qq.com
 * @date 2020/12/4 16:52
 */
@Data
public class WoSmsRequest {

    /**
     * 【必填】-渠道在融合云信平台申请的 cpcode 登录自服务平台，商户信息页面中的【商户编码】
     */
    private String cpcode;

    /**
     * 【必填】-模板参数值，如果包含多个参数，以半角英文逗号分隔
     * <p>
     * 【神州专车】您的专车银色别克GL8 黑AS15L5已出发，司机陈师傅 17074636468，客服电话10101111，祝您用车愉快！
     * 申请模板内容应为： 【神州专车】您的#P#已出发，司机#P#，客服电话#P# ，祝您用车愉快！
     * 实际TXT编辑内容为：18513801778,专车银色别克GL8 黑AS15L5,陈师傅 17074636468,10101111
     */
    private String msg;

    /**
     * 【必填】-11位手机号，如果包含多个手机号，请用半角英文逗号分隔，最多支持300个手机号
     */
    private String mobiles;

    /**
     * 【必填】-渠道自定义接入号的扩展码，可为空；为时传空字符串""
     */
    private String excode;

    /**
     * 【必填】-渠道在融合云信平台申请的模板短信ID
     */
    private String templetid;

    /**
     * 【必填】-MD5签名，签名字符串为cpcode+ msg+mobiles+excode+templetid+key，其中key为融合云信平台分配的私钥，生成签名后转换为小写字符串。
     */
    private String sign;


}


/*

{
    "cpcode": "558",
    "msg": "183001,2,10010",
    "mobiles": "18607714703,18648807841",
    "excode": "00000",
    "templetid": "2",
    "sign": "20e90defeea6ea29e643bdd8a80b18e0"
}

【神州专车】您的专车银色别克GL8 黑AS15L5已出发，司机陈师傅 17074636468，客服电话10101111，祝您用车愉快！
申请模板内容应为： 【神州专车】您的#P#已出发，司机#P#，客服电话#P# ，祝您用车愉快！
实际TXT编辑内容为：18513801778,专车银色别克GL8 黑AS15L5,陈师傅 17074636468,10101111


*/