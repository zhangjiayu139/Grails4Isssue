package middol.sys

import grails.compiler.GrailsCompileStatic
import grails.gorm.DetachedCriteria
import middol.auth.Role
import org.codehaus.groovy.util.HashCodeHelper

/**
* @Description:    角色权限（菜单）
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/25 15:30
* @Version:        1.0
*/
@GrailsCompileStatic
class RolePermission implements Serializable {
    private static final long serialVersionUID = 1
    Role role
    Permission permission

    @Override
    boolean equals(other) {
        if (other instanceof RolePermission) {
            other.permissionId == permission?.id && other.roleId == role?.id
        }
    }

    @Override
    int hashCode() {
        int hashCode = HashCodeHelper.initHash()
        if (permission) {
            hashCode = HashCodeHelper.updateHash(hashCode, permission.id)
        }
        if (role) {
            hashCode = HashCodeHelper.updateHash(hashCode, role.id)
        }
        hashCode
    }

    static RolePermission get(long permissionId, long roleId) {
        criteriaFor(permissionId, roleId).get()
    }

    static boolean exists(long permissionId, long roleId) {
        criteriaFor(permissionId, roleId).count()
    }

    private static DetachedCriteria criteriaFor(long permissionId, long roleId) {
        RolePermission.where {
            permission == Permission.load(permissionId) &&
                    role == Role.load(roleId)
        }
    }

    static RolePermission create(Permission permission, Role role, boolean flush = false) {
        def instance = new RolePermission(permission: permission, role: role)
        instance.save(flush: flush)
        instance
    }

    static boolean remove(Permission p, Role rg) {
        if (p != null && rg != null) {
            RolePermission.where { permission == p && role == rg }.deleteAll()
        }
    }

    static int removeAll(Permission p) {
        p == null ? 0 : RolePermission.where { permission == p }.deleteAll() as int
    }

    static int removeAll(Role rg) {
        rg == null ? 0 : RolePermission.where { role == rg }.deleteAll() as int
    }

    static constraints = {
        role nullable: false
        permission nullable: false, validator: { Permission p, RolePermission rp ->
            if (rp.role?.id) {
                if (RolePermission.exists(p.id, rp.role.id)) {
                    return ['permissionGroup.exists']
                }
            }
        }
    }

    static mapping = {
        id composite: ['role', 'permission']
        version false
    }
}
