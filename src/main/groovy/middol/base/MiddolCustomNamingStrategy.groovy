package middol.base

import grails.util.Holders
import org.hibernate.cfg.ImprovedNamingStrategy

/**
* @Description:    自定义表名(包名之间下划线)
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/7/22 19:04
* @Version:        1.0
*/
class MiddolCustomNamingStrategy extends ImprovedNamingStrategy {

    String classToTableName(String className) {
        Holders.grailsApplication.domainClasses.find{
            it.getName() == className
        }?.getFullName()?.replaceAll("\\.", "_")
    }


    String propertyToColumnName(String propertyName) {
        propertyName.replaceAll("\\.", "_")
    }
}
