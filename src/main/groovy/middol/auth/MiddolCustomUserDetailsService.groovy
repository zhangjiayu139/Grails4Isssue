package middol.auth

import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.userdetails.GrailsUserDetailsService
import grails.plugin.springsecurity.userdetails.NoStackUsernameNotFoundException
import groovy.util.logging.Slf4j
import org.springframework.dao.DataAccessException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

/**
* @Description:    多种登录方式（用户名，邮箱，手机号）
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/8/5 9:47
* @Version:        1.0
*/
@Slf4j
//@CompileStatic
class MiddolCustomUserDetailsService implements GrailsUserDetailsService{
    static final List NO_ROLES = [new SimpleGrantedAuthority(SpringSecurityUtils.NO_ROLE)]

    @Override
    UserDetails loadUserByUsername(String username, boolean loadRoles) throws UsernameNotFoundException, DataAccessException {
        return loadUserByUsername(username)
    }

    @Override
    @Transactional(readOnly=true, noRollbackFor=[IllegalArgumentException, UsernameNotFoundException])
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = User.findByUsernameOrEmailOrPhoneNum(username, username, username)
        println "用户信息"+user
        if (!user) throw new NoStackUsernameNotFoundException()
        //获取角色信息
        def roles = user.roles
        println roles

        // or if you are using role groups:
        // def roles = user.authorities.collect { it.authorities }.flatten().unique()

//        def authorities = roles.collect {
//            new SimpleGrantedAuthority(it.name)
//        }

        //根据角色获取权限信息
        def raList = RoleAuthority.findAllByRoleInList(roles)

        def authorities = raList.collect {
            println it.authority
            new SimpleGrantedAuthority(it.authority?.authority)
        }

        return new MiddolUserDetails(user.username, user.password, user.enabled,
                !user.accountExpired, !user.passwordExpired,
                !user.accountLocked, authorities ?: NO_ROLES, user.id, user.email, user.phoneNum)
    }
}
