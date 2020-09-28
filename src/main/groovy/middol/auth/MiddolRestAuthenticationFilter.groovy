package middol.auth

import grails.converters.JSON
import grails.plugin.springsecurity.rest.SpringSecurityRestFilterRequestMatcher
import grails.plugin.springsecurity.rest.authentication.RestAuthenticationEventPublisher
import grails.plugin.springsecurity.rest.credentials.CredentialsExtractor
import grails.plugin.springsecurity.rest.token.AccessToken
import grails.plugin.springsecurity.rest.token.generation.TokenGenerator
import grails.plugin.springsecurity.rest.token.storage.TokenStorageService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.security.authentication.AuthenticationDetailsSource
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
/**
* @Description:    重写登录验证过滤器，增加验证码
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/8/19 16:44
* @Version:        1.0
*/
@Slf4j
@CompileStatic
class MiddolRestAuthenticationFilter extends GenericFilterBean {
    CredentialsExtractor credentialsExtractor

    String endpointUrl

    AuthenticationManager authenticationManager

    AuthenticationSuccessHandler authenticationSuccessHandler
    AuthenticationFailureHandler authenticationFailureHandler
    RestAuthenticationEventPublisher authenticationEventPublisher
    SpringSecurityRestFilterRequestMatcher requestMatcher

    AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource

    TokenGenerator tokenGenerator
    TokenStorageService tokenStorageService

    @Override
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = request as HttpServletRequest
        HttpServletResponse httpServletResponse = response as HttpServletResponse

        //Only apply filter to the configured URL
        if (requestMatcher.matches(httpServletRequest)) {
            log.debug "Applying authentication filter to this request"

            //Only POST is supported
            if (httpServletRequest.method != 'POST') {
                log.debug "${httpServletRequest.method} HTTP method is not supported. Setting status to ${HttpServletResponse.SC_METHOD_NOT_ALLOWED}"
                httpServletResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED)
                return
            }
            //增加验证码逻辑
            def jsonBody = JSON.parse(httpServletRequest)
            println "=====================验证码校验, 验证码：${jsonBody["code"]}"

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication()
            Authentication authenticationResult

            UsernamePasswordAuthenticationToken authenticationRequest = credentialsExtractor.extractCredentials(httpServletRequest)

            boolean authenticationRequestIsCorrect = (authenticationRequest?.principal && authenticationRequest?.credentials)

            if(authenticationRequestIsCorrect){
                authenticationRequest.details = authenticationDetailsSource.buildDetails(httpServletRequest)

                try {
                    log.debug "Trying to authenticate the request"
                    authenticationResult = authenticationManager.authenticate(authenticationRequest)

                    if (authenticationResult.authenticated) {
                        log.debug "Request authenticated. Storing the authentication result in the security context"
                        log.debug "Authentication result: ${authenticationResult}"

                        AccessToken accessToken = tokenGenerator.generateAccessToken(authenticationResult.principal as UserDetails)
                        log.debug "Generated token: ${accessToken}"

                        tokenStorageService.storeToken(accessToken.accessToken, authenticationResult.principal as UserDetails)
                        authenticationEventPublisher.publishTokenCreation(accessToken)
                        authenticationSuccessHandler.onAuthenticationSuccess(httpServletRequest, httpServletResponse, accessToken)
                        SecurityContextHolder.context.setAuthentication(accessToken)
                    } else {
                        log.debug "Not authenticated. Rest authentication token not generated."
                    }
                } catch (AuthenticationException ae) {
                    log.debug "Authentication failed: ${ae.message}"
                    authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, ae)
                }

            }else{
                log.debug "Username and/or password parameters are missing."
                if(!authentication){
                    log.debug "Setting status to ${HttpServletResponse.SC_BAD_REQUEST}"
                    httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST)
                    return
                }else{
                    log.debug "Using authentication already in security context."
                    authenticationResult = authentication
                }
            }

        } else {
            chain.doFilter(request, response)
        }


    }
}
