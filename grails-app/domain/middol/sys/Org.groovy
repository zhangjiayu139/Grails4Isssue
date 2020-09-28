package middol.sys

import grails.compiler.GrailsCompileStatic
import middolauditlog.Auditable
import middolauditlog.Stampable
/**
* @Description:    组织结构
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/25 15:08
* @Version:        1.0
*/
@GrailsCompileStatic
class Org implements Serializable, Stampable<Org>, Auditable{
    String code             //编号
    String name             //名称
    Long parentId           //上级Id
    Integer orderNum        //排序

    static constraints = {
    }

    static mapping = {
        comment "组织结构"
        code comment: "组织编码"
        name comment: "组织名称"
        parentId index: "index_parentId"
        orderNum comment: "排序"
    }
}
