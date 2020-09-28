package middol.init

import grails.gorm.transactions.Transactional
import middol.auth.Authority
import middol.auth.Requestmap
import middol.auth.Role
import middol.auth.RoleAuthority
import middol.auth.User
import middol.auth.UserAuthority
import middol.auth.UserRole
import middol.sys.Permission
import middol.sys.RolePermission
import middol.test.Test
import static grails.async.Promises.task

/**
* @Description:    初始化用户权限数据
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/8/20 16:11
* @Version:        1.0
*/
class InitDataService {

    def springSecurityService

    /**
     * 初始化 用户权限信息
     * @return
     */
    @Transactional
    def initAuth() {
        def adminRole
        if(User.count() == 0){
            //权限
            def adminAuth = new Authority(authority: 'ROLE_ADMIN').save(flush: true)
            def userAuth = new Authority(authority: 'ROLE_USER').save(flush: true)
            def otherAuth = new Authority(authority: 'ROLE_OTHER').save(flush: true)

            //角色
            adminRole = new Role(name: "超管", code: "admin").save(flush: true)
            def userRole = new Role(name: "用户", code: "user").save(flush: true)
            def AQERole = new Role(name: "AQE", code: "AQE").save(flush: true)
            def otherRole = new Role(name: "第三方系统", code: "other").save(flush: true)

            //用户
            def admin = new User(username: "admin", name: "超管", password: "admin", email: "admin@middol.com", phoneNum: "18114470965").save(flush: true)
            def user = new User(username: "user", name: "用户", password: "user", email: "user@middol.com", phoneNum: "13913346790").save(flush: true)
            def other = new User(username: "other", name: "第三方", password: "other", email: "other@middol.com", phoneNum: "18762815463").save(flush: true)
            def zjy = new User(username: "zjy", name: "张家余", password: "zjy", email: "zjy@middol.com", phoneNum: "13776543956").save(flush: true)

            //权限和组
            new RoleAuthority(role: adminRole, authority: adminAuth).save(flush: true)
            new RoleAuthority(role: userRole, authority: userAuth).save(flush: true)
            new RoleAuthority(role: AQERole, authority: userAuth).save(flush: true)
            new RoleAuthority(role: otherRole, authority: otherAuth).save(flush: true)

            //用户和组
            new UserRole(user: admin, role: adminRole).save(flush: true)
            new UserRole(user: user, role: userRole).save(flush: true)
            new UserRole(user: zjy, role: userRole).save(flush: true)
            new UserRole(user: other, role: otherRole).save(flush: true)
        }

//        if(Permission.count() == 0){
//            def systemSetting = new Permission(label: "系统设置", labelEn: "System Setting", icon: "", type: "", href: "", path: "", component: "").save(flush: true)
//            def permission = new Permission(label: "权限管理", labelEn: "System Setting", icon: "", type: "", href: "", path: "", component: "", parentId: systemSetting.id).save(flush: true)
//            def userMenu = new Permission(label: "用户管理", labelEn: "System Setting", icon: "", type: "", href: "", path: "", component: "", parentId: permission.id).save(flush: true)
//            def role = new Permission(label: "角色管理", labelEn: "System Setting", icon: "", type: "", href: "", path: "", component: "", parentId: permission.id).save(flush: true)
//            def menu = new Permission(label: "菜单管理", labelEn: "System Setting", icon: "", type: "", href: "", path: "", component: "", parentId: permission.id).save(flush: true)
//            def org = new Permission(label: "部门管理", labelEn: "System Setting", icon: "", type: "", href: "", path: "", component: "", parentId: permission.id).save(flush: true)
//            def system = new Permission(label: "系统管理", labelEn: "System Setting", icon: "", type: "", href: "", path: "", component: "", parentId: systemSetting.id).save(flush: true)
//            def loginLog = new Permission(label: "登录日志", labelEn: "System Setting", icon: "", type: "", href: "", path: "", component: "", parentId: system.id).save(flush: true)
//            def operationLog = new Permission(label: "操作日志", labelEn: "System Setting", icon: "", type: "", href: "", path: "", component: "", parentId: system.id).save(flush: true)
//            def dic = new Permission(label: "字典管理", labelEn: "System Setting", icon: "", type: "", href: "", path: "", component: "", parentId: system.id).save(flush: true)
//            def token = new Permission(label: "令牌管理", labelEn: "System Setting", icon: "", type: "", href: "", path: "", component: "", parentId: system.id).save(flush: true)
//            def quartz = new Permission(label: "Quartz管理", labelEn: "System Setting", icon: "", type: "", href: "", path: "", component: "", parentId: system.id).save(flush: true)
//
//            RolePermission.create(systemSetting, adminRole, true)
//            RolePermission.create(permission, adminRole, true)
//            RolePermission.create(userMenu, adminRole, true)
//            RolePermission.create(role, adminRole, true)
//            RolePermission.create(menu, adminRole, true)
//            RolePermission.create(org, adminRole, true)
//            RolePermission.create(system, adminRole, true)
//            RolePermission.create(loginLog, adminRole, true)
//            RolePermission.create(operationLog, adminRole, true)
//            RolePermission.create(dic, adminRole, true)
//            RolePermission.create(token, adminRole, true)
//            RolePermission.create(quartz, adminRole, true)
//        }

        if(Requestmap.count() == 0){
            for (String url in [
                    '/', '/error', '/index', '/index.gsp', '/**/favicon.ico', '/shutdown',
                    '/assets/**', '/**/js/**', '/**/css/**', '/**/images/**','/api/login/*', '/stomp/**',
                    '/api/logout', '/oauth/*']) {
                new Requestmap(url: url, configAttribute: 'permitAll').save()
            }

            new Requestmap(url: '/api/**', configAttribute: 'ROLE_ADMIN,ROLE_USER').save()
            new Requestmap(url: '/webService/**', configAttribute: 'ROLE_OTHER').save()

//            new Requestmap(url: '/api/logout', configAttribute: 'isAuthenticated()').save()

            springSecurityService.clearCachedRequestmaps()
        }
    }
}
