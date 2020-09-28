package middol.auth

import grails.plugin.springsecurity.SpringSecurityService
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic
import middolauditlog.Stampable

/**
* @Description:    用户
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/8/20 16:05
* @Version:        1.0
*/
@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User implements Serializable, Stampable<User> {
    SpringSecurityService springSecurityService

    private static final long serialVersionUID = 1

    String username         //登录账号
    String name         //真实姓名
    String avatar           //头像
    Integer sex             //性别（1：男 2：女）
    String email            //电子邮件
    String phoneNum         //电话
    String password         //密码
    String orgCode          //组织代码
    Integer status           //状态(1：正常  2：冻结 ）
    String workNo           //工号
    String post             //职务
    Long orgId              //组织ID
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    Set<Role> getRoles() {
        (UserRole.findAllByUser(this) as List<UserRole>)*.role as Set<Role>
    }

    def beforeInsert() {
        encodePassword()
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
    }

    static transients = ['springSecurityService']

    static constraints = {
        password nullable: false, blank: false, password: true
        username nullable: false, blank: false, unique: true
    }

    static mapping = {
        comment "用户信息"
        password comment: "密码", column: '`password`'
        username comment: "用户名", index: "index_username"
        phoneNum comment: "手机号", index: "index_phoneNum"
        email comment: "邮箱", index: "index_email"
        name comment: "真实姓名"
        avatar comment: "头像"
        sex comment: "性别（1：男 2：女）"
        orgCode comment: "组织代码"
        status comment: "状态(1：正常  2：冻结 ）"
        workNo comment: "工号"
        post comment: "职务"
        enabled comment: "是否启用"
        accountExpired comment: "账号是否过期"
        accountLocked comment: "账号是否锁住"
        passwordExpired comment: "密码是否过期"
        orgId comment: "组织Id"
    }
}
