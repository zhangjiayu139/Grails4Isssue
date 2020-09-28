package middol.auth

import grails.converters.JSON
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import middol.enums.ResultCodeEnum
import middol.result.ResultUtils
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
* @Description:    登录失败--记录到登录日志里面
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/7/21 9:58
* @Version:        1.0
*/
@Slf4j
@CompileStatic
class MiddolAuthenticationFailureHandler implements AuthenticationFailureHandler {
    Integer statusCode
    @Override
    void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.debug("登录失败：${exception.message}")
        log.debug "Setting status code to ${statusCode}"
        response.setStatus(statusCode)
        response.addHeader('WWW-Authenticate', 'Bearer')
        response.setContentType("application/json;charset=utf-8")
        response << (ResultUtils.error(statusCode?.toString(), exception.message) as JSON)
    }
}
