package middol.sys

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional
import middol.auth.Role

@Service(RolePermission)
abstract class RolePermissionService {
    abstract List<RolePermission> findAllByRole(Role role)

    List<RolePermission> findAllByRoleInList(List<Role> roleList){
        RolePermission.findAllByRoleInList(roleList)
    }
}
