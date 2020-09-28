package middol.base


import middol.result.Result
import middol.result.ResultUtils

/**
* @Description:    全局异常捕获
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/7/20 18:24
* @Version:        1.0
*/
class ErrorController {
	static responseFormats = ['json', 'xml']

    Result handleError(Exception e){
        respond ResultUtils.error(e.getMessage())
    }
}
