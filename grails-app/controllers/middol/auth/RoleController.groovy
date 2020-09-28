package middol.auth


import grails.rest.*
import grails.converters.*
import middol.base.BaseController
/**
* @Description:    角色信息
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/25 15:19
* @Version:        1.0
*/
class RoleController extends BaseController<Role>{
	static responseFormats = ['json', 'xml']

    RoleController() {
        super(Role)
    }
    RoleService roleService

    /**
     * 保存角色菜单权限
     * @return
     */
    def savePermission(){
        def record = request.JSON
        render roleService.savePermission(record) as JSON
    }

    def saveUser(){
        def record = request.JSON
        render roleService.saveUser(record) as JSON
    }
}
