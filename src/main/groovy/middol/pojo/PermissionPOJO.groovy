package middol.pojo

import groovy.transform.CompileStatic
import middolauditlog.Auditable
import middolauditlog.Stampable

/**
* @Description:    菜单按钮输出对象
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/24 13:45
* @Version:        1.0
*/
@CompileStatic
class PermissionPOJO implements Serializable{
    Long id
    String label
    Map meta
    String path
    String component
    String icon
    String levelId
}
