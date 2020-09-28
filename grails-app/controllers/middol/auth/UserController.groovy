package middol.auth


import grails.rest.*
import grails.converters.*
import middol.base.BaseController
/**
* @Description:    用户信息
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/25 15:15
* @Version:        1.0
*/
class UserController extends BaseController<User> {
	static responseFormats = ['json', 'xml']

    UserService userService

    UserController() {
        super(User)
    }

    /**
     * 获取当前登录信息
     * @return
     */
    def getCurrentUser(){
        render userService.getCurrentUser() as JSON
    }

    /**
     * 获取当前登录人的一级菜单
     * @return
     */
    def getTopMenu(){
        render userService.getTopMenu() as JSON
    }

    /**
     * 获取当前登录人的菜单信息
     * @return
     */
    def getCurrentMenu(){
        def record = request.JSON
        render userService.getMenu(record.parentId) as JSON
    }

    /**
     * 添加用户角色信息
     * @return
     */
    def saveUserRole(){
        def record = request.JSON
        render userService.saveUserRole(record) as JSON
    }
}
