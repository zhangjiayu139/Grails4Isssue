package middol.auth

import grails.converters.JSON
import grails.plugin.springsecurity.rest.token.AccessToken
import grails.plugin.springsecurity.rest.token.rendering.AccessTokenJsonRenderer
import grails.plugin.springsecurity.userdetails.GrailsUser
import groovy.util.logging.Slf4j
import middol.sys.LoginRecord
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import static grails.async.Promises.task

/**
* @Description:    登录成功--生成登录日志
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/7/21 9:56
* @Version:        1.0
*/
@Slf4j
class MiddolAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    AccessTokenJsonRenderer renderer

    @Override
    void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("登陆成功:${authentication as JSON}")
        if (authentication.principal instanceof GrailsUser) {
            GrailsUser user = ((GrailsUser) (authentication.principal))
            log.info("用户信息:${user as JSON}")
            log.info("detail:${authentication?.accessToken}")
            //记录登陆日志
            task {
                LoginRecord.withTransaction {
                    LoginRecord loginrecord = new LoginRecord()
                    loginrecord.login_ip = request.getRemoteHost()
                    loginrecord.login_username = user.username
                    loginrecord.login_indate = new Date()
                    loginrecord.login_http = request.getMethod()
                    if (!loginrecord.hasErrors()) {
                        log.info("保存登陆日志:${loginrecord.toString()}")
                        loginrecord.save(flush: true)
                    } else {
                        log.info("日志错误:${loginrecord.errors?.toString()}")
                    }
                }
            }
        }
        response.contentType = 'application/json'
        response.characterEncoding = 'UTF-8'
        response.addHeader 'Cache-Control', 'no-store'
        response.addHeader 'Pragma', 'no-cache'
        response << renderer.generateJson(authentication as AccessToken)
    }
}
