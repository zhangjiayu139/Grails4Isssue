package middol.result
/**
* @Description:    输出结果对象
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/3/19 10:55
* @Version:        1.0
*/
class Result implements Serializable{
    private static final long serialVersionUID = 1L;

    private String code = 0
    private String message = "操作成功"
    private Object data

    Result(){}

    Result(String code, String message, Object data) {
        this.code = code
        this.message = message
        this.data = data
    }

    static long getSerialVersionUID() {
        return serialVersionUID
    }

    String getCode() {
        return code
    }

    void setCode(String code) {
        this.code = code
    }

    String getMessage() {
        return message
    }

    void setMessage(String message) {
        this.message = message
    }

    Object getData() {
        return data
    }

    void setData(Object data) {
        this.data = data
    }
}
