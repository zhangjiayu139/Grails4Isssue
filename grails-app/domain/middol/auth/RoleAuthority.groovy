package middol.auth

import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.codehaus.groovy.util.HashCodeHelper
import grails.compiler.GrailsCompileStatic
/**
* @Description:    角色-权限
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/8/20 16:06
* @Version:        1.0
*/
@GrailsCompileStatic
@ToString(cache=true, includeNames=true, includePackage=false)
class RoleAuthority implements Serializable {

	private static final long serialVersionUID = 1

	Role role
	Authority authority

	@Override
	boolean equals(other) {
		if (other instanceof RoleAuthority) {
			other.authorityId == authority?.id && other.roleId == role?.id
		}
	}

	@Override
	int hashCode() {
	    int hashCode = HashCodeHelper.initHash()
        if (role) {
            hashCode = HashCodeHelper.updateHash(hashCode, role.id)
		}
		if (authority) {
		    hashCode = HashCodeHelper.updateHash(hashCode, authority.id)
		}
		hashCode
	}

	static RoleAuthority get(long roleId, long authorityId) {
		criteriaFor(roleId, authorityId).get()
	}

	static boolean exists(long roleId, long authorityId) {
		criteriaFor(roleId, authorityId).count()
	}

	private static DetachedCriteria criteriaFor(long roleId, long authorityId) {
		RoleAuthority.where {
			role == Role.load(roleId) &&
			authority == Authority.load(authorityId)
		}
	}

	static RoleAuthority create(Role role, Authority authority, boolean flush = false) {
		def instance = new RoleAuthority(role: role, authority: authority)
		instance.save(flush: flush)
		instance
	}

	static boolean remove(Role rg, Authority r) {
		if (rg != null && r != null) {
			RoleAuthority.where { role == rg && authority == r }.deleteAll()
		}
	}

	static int removeAll(Authority r) {
		r == null ? 0 : RoleAuthority.where { authority == r }.deleteAll() as int
	}

	static int removeAll(Role rg) {
		rg == null ? 0 : RoleAuthority.where { role == rg }.deleteAll() as int
	}

	static constraints = {
	    role nullable: false
		authority nullable: false, validator: { Authority r, RoleAuthority rg ->
			if (rg.role?.id) {
				if (RoleAuthority.exists(rg.role.id, r.id)) {
				    return ['roleGroup.exists']
				}
			}
		}
	}

	static mapping = {
		id composite: ['role', 'authority']
		version false
	}
}
