package middol.sys


import grails.rest.*
import grails.converters.*
import middol.base.BaseController
/**
* @Description:    菜单权限
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/25 15:37
* @Version:        1.0
*/
class PermissionController extends BaseController<Permission> {
	static responseFormats = ['json', 'xml']

    PermissionController() {
        super(Permission)
    }
}
