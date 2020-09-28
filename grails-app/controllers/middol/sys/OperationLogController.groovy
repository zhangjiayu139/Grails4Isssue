package middol.sys


import grails.rest.*
import grails.converters.*
import middol.base.BaseController

/**
* @Description:    数据操作日志
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/25 15:43
* @Version:        1.0
*/
class OperationLogController extends BaseController<OperationLog>{
	static responseFormats = ['json', 'xml']

    OperationLogController() {
        super(OperationLog)
    }
}
