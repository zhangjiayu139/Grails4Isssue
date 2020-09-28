package middol.utils


import grails.plugin.springsecurity.SpringSecurityService
import middol.auth.UserDataService
import middol.auth.User
import middol.auth.UserRole

/**
* @Description:    springSecurity工具类
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/16 18:59
* @Version:        1.0
*/

class SecurityService {
    UserDataService userDataService
    SpringSecurityService springSecurityService
    /**
     * 获取当前登录人用户名
     * @return
     */
    String getLoginUsername(){
        springSecurityService?.getPrincipal()?.getUsername()
    }

    /**
     * 获取当前登录人的权限信息（超级管理员，用户，第三方）
     * @return
     */
    def getAuthority(){
        springSecurityService?.getAuthentication()?.authorities*.authority
    }

    /**
     * 获取当前登录人
     * @return
     */
    def getCurrentUser(){
        //获取当前用户信息
        String username = getLoginUsername()
        log.info("当前系统登录人：${username}")
        println username
        userDataService.findUser(username)
    }

    /**
     * 获取当前登录人的角色信息
     * @return
     */
    def getCurrentUserRole(){
        User user = getCurrentUser()
        //获取用户对应的角色信息
        UserRole.findAllByUser(user)*.role
    }
}
