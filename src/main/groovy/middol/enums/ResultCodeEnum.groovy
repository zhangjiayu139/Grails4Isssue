package middol.enums

/**
* @Description:    返回结果枚举
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/3/19 11:01
* @Version:        1.0
*/
enum ResultCodeEnum {
    /**
     * 成功
     */
    OK("200", "操作成功！"),

    /**
     * 通用参数错误
     */
    AUTH_ERROR("401", "认证失败！"),
    /**
     * 通用参数错误
     */
    VALIDATE_ERROR("422", "校验不通过！"),
    /**
     *  数据不存在
     */
    NOTFOUND("432", "数据不存在！"),

    /**
     * 通用参数错误
     */
    ERROR("500", "参数错误！"),

    /**
     * 系统异常
     */
    SYSTEM_ERROR("501", "系统异常，请联系管理员！"),

    /**
     * 获取结果异常
     */
    RESULT_NULL("10002","获取结果为空"),

    /**
     * 参数为空
     */
    NO_ARGS("20000", "参数不能为空！"),

    /**
     * 非法参数
     */
    ARGS_ERROR("20001","非法参数！"),

    /**
     * 没有访问权限
     */
    NO_AUTH("403", "没有访问权限！");

    private String code
    private String msg

    ResultCodeEnum(String code, String msg) {
        this.code = code
        this.msg = msg
    }

    String getCode() {
        return code
    }

    void setCode(String code) {
        this.code = code
    }

    String getMsg() {
        return msg
    }

    void setMsg(String msg) {
        this.msg = msg
    }
}