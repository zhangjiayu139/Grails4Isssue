package middol.sys

import grails.gorm.services.Service

@Service(Message)
interface MessageService {

    Message get(Serializable id)

    List<Message> list(Map args)

    Long count()

    Message delete(Serializable id)

    Message save(Message message)

}
