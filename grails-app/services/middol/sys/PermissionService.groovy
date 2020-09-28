package middol.sys

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(Permission)
abstract class PermissionService {

    abstract Permission get(Serializable id)
}
