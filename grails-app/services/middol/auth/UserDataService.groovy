package middol.auth

import grails.gorm.services.Service

@Service(User)
interface UserDataService {
    User findUser(String username)
    User get(Serializable id)
}
