package middol.base

import grails.plugin.springsecurity.SpringSecurityService
import groovy.util.logging.Slf4j
import middolauditlog.AuditLogContext
import middolauditlog.resolvers.DefaultAuditRequestResolver
import org.springframework.beans.factory.annotation.Autowired

@Slf4j
class MiddolAuditRequestResolver extends DefaultAuditRequestResolver{
    @Autowired
    SpringSecurityService springSecurityService

    @Override
    String getCurrentActor() {
        springSecurityService?.getPrincipal()?.getUsername() ?: AuditLogContext.context.defaultActor ?: 'N/A'
    }
}
