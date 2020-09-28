package middol.sys

import grails.compiler.GrailsCompileStatic
import middolauditlog.Auditable
import middolauditlog.Stampable

/**
* @Description:    权限（菜单，按钮）
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/25 15:21
* @Version:        1.0
*/
@GrailsCompileStatic
class Permission implements Serializable, Stampable<Permission>, Auditable{
    private static final long serialVersionUID = 1

    String label            // 菜单名称
    String labelEn          // 菜单英文名
    Long parentId           // 上级菜单ID
    Integer sortNo = 1         // 菜单排序
    String icon = "icon-caidan"             // 菜单图标
    Integer type             //类型（0：一级菜单；1：子菜单 ；2：按钮权限）
    String href             // 菜单URL
    String path             // 菜单路径
    String permission       //权限标识
    String component        // 菜单组件
    boolean isShow = 1           //是否显示。0：不显示。1：显示。
    boolean keepAlive = 0        //是否缓存页面: 0:不是  1:是（默认值1）

    static constraints = {
        label maxSize: 50
        href maxSize: 500
        path maxSize: 500
        component maxSize: 500
    }

    static mapping = {
        comment "权限（菜单、按钮）"
        label comment: "菜单名称(中文)"
        labelEn comment: "菜单名称（外文）"
        parentId comment: "上级菜单ID", index: "index_parentId"
        sortNo comment: "菜单排序"
        icon comment: "菜单图标"
        type comment: "类型（0：一级菜单；1：子菜单 ；2：按钮权限）"
        href comment: "菜单URL"
        path comment: "菜单路径"
        permission comment: "权限标识"
        component comment: "菜单组件"
        isShow comment: "是否显示。0：不显示。1：显示。"
        keepAlive comment: "是否缓存页面: 0:不是  1:是（默认值1）"
    }
}
