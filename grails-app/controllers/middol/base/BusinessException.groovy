package middol.base

/**
* @Description:    自定义业务异常
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/3/19 13:43
* @Version:        1.0
*/
class BusinessException extends RuntimeException {

    BusinessException(String msg){
        super(msg)
    }
}
