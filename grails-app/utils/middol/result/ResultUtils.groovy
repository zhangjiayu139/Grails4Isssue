package middol.result

import middol.enums.ResultCodeEnum

/**
* @Description:    统一返回工具类
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/3/19 12:17
* @Version:        1.0
*/
class ResultUtils {
    static Result ok(){
        return put(ResultCodeEnum.OK)
    }

    static Result ok(Object data){
        return put(ResultCodeEnum.OK, data)
    }

    static Result ok(String msg){
        return put(ResultCodeEnum.OK, msg)
    }

    static Result error(String msg){
        return put(ResultCodeEnum.ERROR, msg)
    }

    static Result error(String code, String msg){
        return put(code, msg)
    }

    static Result error(ResultCodeEnum resultCodeEnum){
        return put(resultCodeEnum)
    }

    static Result error(ResultCodeEnum resultCodeEnum, Object data){
        return put(resultCodeEnum, data)
    }

    private static Result put(ResultCodeEnum resultCodeEnum){
        return put(resultCodeEnum.getCode(), resultCodeEnum.getMsg(), null)
    }

    private static Result put(String code, String msg){
        return put(code, msg, null)
    }

    private static Result put(ResultCodeEnum resultCodeEnum, Object data){
        return new Result(resultCodeEnum.getCode(), resultCodeEnum.getMsg(), data)
    }

    private static Result put(String code, String msg, Object data){
        return new Result(code, msg, data)
    }
}
