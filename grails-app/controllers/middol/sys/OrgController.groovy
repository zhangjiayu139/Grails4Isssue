package middol.sys


import grails.rest.*
import grails.converters.*
import middol.base.BaseController
/**
* @Description:    组织信息
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/25 15:36
* @Version:        1.0
*/
class OrgController extends BaseController<Org> {
	static responseFormats = ['json', 'xml']

    OrgController() {
        super(Org)
    }
}
