package middol.auth

import grails.converters.JSON
import groovy.util.logging.Slf4j
import middol.enums.ResultCodeEnum
import middol.result.ResultUtils
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandlerImpl

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
/**
* @Description:    没有权限
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/7/21 18:38
* @Version:        1.0
*/
@Slf4j
class MiddolTokenAccessDeniedHandler  extends AccessDeniedHandlerImpl {
    @Override
    void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.addHeader('WWW-Authenticate', 'Bearer error="insufficient_scope"')
        response.setContentType("application/json;charset=utf-8")
        log.debug("没有权限:${accessDeniedException.message}")
//        super.handle(request, response, accessDeniedException)
        response.setStatus(HttpStatus.FORBIDDEN.value())
        ResultCodeEnum rce = ResultCodeEnum.NO_AUTH
        response << (ResultUtils.error(rce.getCode(), accessDeniedException.message) as JSON)
    }
}
