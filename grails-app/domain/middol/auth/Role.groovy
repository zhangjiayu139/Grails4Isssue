package middol.auth

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic
import middolauditlog.Stampable

/**
* @Description:    角色
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/8/20 16:06
* @Version:        1.0
*/
@GrailsCompileStatic
//@EqualsAndHashCode(includes='name')
//@ToString(includes='name', includeNames=true, includePackage=false)
class Role implements Serializable, Stampable<Role> {

	private static final long serialVersionUID = 1

	String name
	String code
	String remarks

	Set<Authority> getAuthorities() {
		(RoleAuthority.findAllByRole(this) as List<RoleAuthority>)*.authority as Set<Authority>
	}

	static constraints = {
		name nullable: false, blank: false, unique: true
	}

	static mapping = {
		cache true
		comment "角色信息"
		remarks type: "text", comment: "备注"
		name comment: "角色名称"
		code comment: "角色编码"
	}
}
