package middol.sys.listener

import grails.events.annotation.Subscriber
import grails.gorm.transactions.Transactional
import groovy.transform.CompileStatic
import org.grails.datastore.mapping.engine.event.PostDeleteEvent
import org.grails.datastore.mapping.engine.event.PostInsertEvent
import org.grails.datastore.mapping.engine.event.PostUpdateEvent

@CompileStatic
class MessageListenerService {
    @Subscriber
    void afterInsert(PostInsertEvent event) {
        log.info("afterInsert.....................")
    }
    @Subscriber
    void afterUpdate(PostUpdateEvent event) {
        log.info("afterUpdate.....................")
    }
    @Subscriber
    void afterDelete(PostDeleteEvent event) {
        log.info("afterDelete.....................")
    }
}
