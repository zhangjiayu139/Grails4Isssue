package middol.auth

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional
import middol.result.ResultUtils
import middol.sys.PermissionService
import middol.sys.RolePermission

@Service(Role)
abstract class RoleService {
    abstract Role get(Serializable id)

    PermissionService permissionService
    UserService userService

    /**
     * 保存角色菜单权限信息
     * @param record
     * @return
     */
    def savePermission(Map record){
        Role role = get(record.id)
        def permissionIdList = record.permissionIds?.tokenize(",")
        permissionIdList?.each{
            RolePermission.create(permissionService.get(it), role)
        }

        RolePermission.withSession {
            it.flush()
            it.clear()
        }
        return ResultUtils.ok()
    }

    /**
     * 保存角色用户信息
     * @param record
     * @return
     */
    def saveUser(Map record){
        Role role = get(record.id)
        def userIdList = record.userIds?.tokenize(",")
        userIdList?.each{
            UserRole.create(userService.get(it), role)
        }

        UserRole.withSession {
            it.flush()
            it.clear()
        }
        return ResultUtils.ok()
    }
}
