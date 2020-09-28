package middol.auth

import middol.result.ResultUtils
import middol.sys.RolePermissionService
import middol.utils.SecurityService

class UserService {
    UserDataService userDataService
    SecurityService securityService
    RoleService roleService
    RolePermissionService rolePermissionService

    /**
     * 获取当前用户信息和角色信息
     * @return
     */
    def getCurrentUser() {
        def result = [:]
        //获取当前用户信息
        User user = securityService.getCurrentUser()
        //获取用户对应的角色信息
        def roleList = securityService.getCurrentUserRole()
        result.userInfo = user
        result.roles = roleList
        result.permission = []
        return ResultUtils.ok(result)
    }
    /**
     * 获取当前登录人的一级菜单
     * @return
     */
    def getTopMenu(){
        //获取用户对应的角色信息
        List<Role> roleList = securityService.getCurrentUserRole()
        def permissionList = rolePermissionService.findAllByRoleInList(roleList)*.permission.unique().collect{
            if(!it.parentId){
                [
                        id       : it.id,
                        label    : it.label,
                        meta: [keepAlive: it.keepAlive, i18n: it.labelEn],
                        path     : it.path,
                        component: it.component,
                        icon     : it.icon,
                        parentId  : it.parentId
                ]
            }
        }

        return ResultUtils.ok(permissionList)
    }

    /**
     * 获取当前登录人的菜单信息
     * @return
     */
    def getMenu(parantId = null){
        //获取用户对应的角色信息
        List<Role> roleList = securityService.getCurrentUserRole()
        def permissionList = rolePermissionService.findAllByRoleInList(roleList)*.permission.unique().collect{
            [
                    id       : it.id,
                    label    : it.label,
                    meta: [keepAlive: it.keepAlive, i18n: it.labelEn],
                    path     : it.path,
                    component: it.component,
                    icon     : it.icon,
                    parentId  : it.parentId
            ]
        }
        def result = getPermissionTree(permissionList, parantId)
        return ResultUtils.ok(result)
    }

    /**
     * 递归
     * @param permissionList
     * @return
     */
    def getPermissionTree(def permissionList, Long parentId){
        def menus = []
        def newMenuList = permissionList.findAll {
            it.parentId == parentId
        }
        newMenuList.each {
            it.children = getPermissionTree(permissionList, it.id)
            menus.add(it)
        }
        return menus
    }

    /**
     * 保存用户角色信息
     * @param record
     */
    def saveUserRole(Map record){
        User user = userDataService.get(record.userId)
        def roleIdList = record.roleIds.tokenize(",")
        roleIdList.each(){
            UserRole.create(user, roleService.get(it))
        }
        UserRole.withSession {
            it.flush()
            it.clear()
        }
        return ResultUtils.ok()
    }
}
