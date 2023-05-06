package com.cutefool.commons.core.page;

/**
 * ResponseCode
 *
 * @author 271007729@qq.com
 * @date 2019-07-02 10:44
 */

@SuppressWarnings({"unused"})
public enum ResponseCode {

    /*** 成功
     */
    SUCCESS("0", "ok"),

    /**
     * 系统错误
     */
    SYSTEM_ERROR("10001", "系统出现异常，请稍后重试。"),



    /* ********* API 相关异常 ********/

    /**
     * 没有权限
     */
    API_PERMISSION_DENIED("10002", "对不起，您没有权限访问，请联系管理员。"),

    /**
     * 系统错误
     */
    API_UN_SUPPORT_MEDIA_TYPE("10003", "不支持的MediaType (%s)"),

    /**
     * 参数值格式不正确
     */
    API_PARAM_ERROR("10004", "参数值格式不正确"),

    /**
     * 参数值类型不正确
     */
    API_PARAM_TYPE_ERROR("10005", "参数%s只能是%s类型数据"),

    /**
     * 请求体不能为空
     */
    API_BODY_IS_EMPTY("10006", "请求体不能为空"),

    /**
     * 不支持该请求方式
     */
    API_UN_SUPPORT_METHOD("10007", "不支持%s请求方式"),

    /**
     * 请求地址不存在
     */
    API_NOT_EXISTS("10008", "请请求资源[%s] - [%s]不存在"),

    /**
     * 参数为空
     */
    API_PARAM_IS_EMPTY("10009", "参数%s不能为空"),


    /**
     * 参数值不支持
     */
    API_PARAM_IS_ERROR("10010", "参数%s不能为空"),

    /**
     * 第三方接口调用失败
     */
    API_CALL_FAILED("10011", "第三方接口调用失败"),
    /**
     * 指定内容不允许修改
     */
    API_NOT_ALLOW_MODIFY("10013", "%s不允许修改"),

    /* ********* 登录认证相关 ************/

    /**
     * 用户名或密码不正确
     */
    USER_ERROR("20001", "用户名或密码不正确"),

    /**
     * 用户不存在
     */
    USER_NOT_EXISTS("20002", "用户不存在"),

    /**
     * 用户已经被禁用
     */
    USER_DISABLED("20003", "账号目前为禁用状态，无法登录"),

    /**
     * 用户token值已经过期
     */
    USER_TOKEN_EXPIRED("20004", "登录已经失效，请重新登录。"),

    /**
     * 用户token非法，不能解析
     */
    USER_TOKEN_INVALID("20005", "登录信息非法，请重新登录。"),

    /**
     * 用户未登录
     */
    USER_NOT_LOGIN("20006", "您尚未登录，请先登录。"),

    /**
     * 用户没有操作权限
     */
    USER_NO_PERMISSION("20007", "您没有操作权限，请联系管理员。"),

    /**
     * 用户登录名为空
     */
    USER_LOGIN_NAME_EMPTY("20008", "用户登录名不能为空"),

    /**
     * 用户登录名格式不正确
     */
    USER_LOGIN_NAME_INVALID("20009", "用户登录名不正确，%s"),

    /**
     * 用户登录名已经存在
     */
    USER_LOGIN_NAME_EXISTS("20010", "用户登录名已经存在"),

    /**
     * 用户密码为空
     */
    USER_LOGIN_PASSWORD_EMPTY("20011", "用户密码不能为空"),

    /**
     * 用户密码不正确
     */
    USER_LOGIN_PASSWORD_INVALID("20012", "用户密码不正确"),

    /**
     * 用户已经被锁定
     */
    USER_LOCKED("20013", "密码输入错误%s次，账号将冻结%s小时！"),

    /**
     * 验证码错误
     */
    USER_LOGIN_CODE_ERROR("20014", "请输入正确的验证码"),

    /**
     * 请修改默认密码
     */
    USER_DEFAULT_PASSWORD("20015", "请修改默认密码"),

    /**
     * 登录密码错误次数提示
     */
    USER_LOGIN_PASSWORD_ERROR("20016", "密码输入错误%s次，输入密码错误%s次账号将被冻结%s小时！"),

    /**
     * 手机号不存在
     */
    USER_MOBILE_NOT_EXISTS("20017", "手机号不存在"),

    /**
     * 请输入正确的手机号
     */
    USER_MOBILE_INVALID("20018", "请输入正确的手机号"),

    /**
     * 两次输入的密码不一致
     */
    USER_LOGIN_PASSWORD_INCONSISTENT("20019", "两次输入的密码不一致"),

    /**
     * 用户密码格式错误
     */
    USER_LOGIN_PASSWORD_FORMAT_INVALID("20020", "请设置符合规则的密码"),

    /**
     * 用户名或手机号不存在
     */
    USER_NAME_OR_MOBILE_NOT_EXIST("20021", "用户名或手机号不存在"),

    /**
     * 账号信息不完整，无法登录，请联络管理员
     */
    USER_INCOMPLETE("20022", "账号信息不完整，无法登录，请联络管理员"),

    /**
     * 待验证
     */
    USER_WAIT_FOR_VERIFICATION("20023", "待验证"),

    /**
     * 手机号已经存在
     */
    USER_MOBILE_EXISTS("20024", "手机号%s被占用,请重试"),

    /**
     * 手机号和登录名都已经存在
     */
    USER_LOGIN_AND_MOBILE_EXISTS("20025", "手机号%s,登录名%s被占用,请重试"),

    /**
     * 拒绝用户登录
     */
    USER_LOGIN_REFUSE("20026", "系统升级中，请稍后访问"),

    /**
     * 访问过期
     */
    USER_LOGIN_DOMAIN_EXPIRE("20027", "抱歉，您暂无此系统的操作权限"),

    /**
     * 访问被禁用
     */
    USER_LOGIN_DOMAIN_DISABLED("20028", "抱歉，您暂无此系统的操作权限"),
    /**
     * 访问不存在
     */
    USER_LOGIN_DOMAIN_NOT_EXISTS("20029", "抱歉，您暂无此系统的操作权限"),

    /**
     * 签名错误
     */
    DIGESTS_ERROR("21000", "数据签名出现错误。"),

    ///////////////////////

    /**
     * 数据主键为空
     */
    DATA_FIELD_IS_NULL("30001", "%s不能为空"),

    /**
     * 数据已经存在
     */
    DATA_IS_EXISTS("30002", "%s已经存在"),

    /**
     * 数据不存在
     */
    DATA_NOT_EXISTS("30003", "%s不存在"),

    /**
     * 数据有关联不能删除
     */
    DATA_RELATION_CAN_NOT_DELETE("30004", "%s有%s，不能删除"),

    /**
     * 数据名称重复
     */
    DATA_NAME_IS_EXISTS("30005", "%s名称重复"),

    /**
     * 数据不能提交
     */
    DATA_CAN_NOT_COMMIT("30006", "数据不能提交"),

    ///////////////////////

    /**
     * redis服务宕机
     */
    REDIS_SERVICE_IS_DOWN("40001", "缓存服务器不可用。"),

    /**
     * redis服务超时
     */
    REDIS_SERVICE_TIMEOUT("40002", "缓存服务器操作超时。"),

    ///////////////////////

    /**
     * 服务不可用
     */
    SERVICE_IS_DOWN("50001", "%s服务不可用"),


    /**
     * 百度人脸识别
     */
    BAIDU_FACE("50100", "百度人脸识别出现异常：%s"),


    // oauth认证相关编码

    OAUTH_CODE_ERROR("60001", "code不正确或者已经过期"),

    OAUTH_CLIENT_ERROR("60002", "clientId不正确"),

    OAUTH_NOT_UNAUTHORIZED_ERROR("60003", "not unauthorized"),

    OAUTH_NOT_SUPPORT_GRANT_TYPE_ERROR("60004", "不支持的grantType类型"),

    OAUTH_SCOPE_ERROR("60005", "scope错误"),

    OAUTH_TOKEN_ERROR("60006", "token错误"),

    OAUTH_BAD_CREDENTIALS_ERROR("60007", "bad Credentials : %s"),

    /**
     * Expression
     */
    EXPRESSION_ERROR("70101", "表达式错误"),

    /**
     * workflow
     */
    WORKFLOW_ERROR("70201", "工作流配置错误"),

    /**
     * workflow condition
     */
    WORKFLOW_CONDITION_ERROR("70301", "工作流条件配置错误，条件为%s，实际为%s"),

    /**
     * MYSQL
     */
    MYSQL_DATA_ERROR("80100", "数据格式错误"),

    /**
     * influx
     */
    INFLUX_ERROR("80200", "Influx数据库异常：%s"),

    /**
     * iot
     */
    IOT_ERROR("80300", "IOT服务不可用");

    //////////////////////

    /**
     * 编号
     */
    private String code;

    /**
     * 消息
     */
    private String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
