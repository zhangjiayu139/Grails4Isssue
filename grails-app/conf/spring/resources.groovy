import grails.plugin.springsecurity.SpringSecurityUtils
import grails.rest.render.json.JsonRenderer
import middol.auth.MiddolAccessTokenJsonRenderer
import middol.auth.MiddolAuthenticationFailureHandler
import middol.auth.MiddolAuthenticationSuccessHandler
import middol.auth.MiddolCustomUserDetailsService
import middol.auth.MiddolRestAuthenticationFilter
import middol.auth.MiddolTokenAccessDeniedHandler
import middol.auth.User
import middol.auth.UserPasswordEncoderListener
import middol.base.MiddolAuditRequestResolver
import middol.domain.MiddolFormattedStringValueConverter
import org.springframework.web.servlet.i18n.SessionLocaleResolver

import javax.servlet.http.HttpServletResponse

// Place your Spring DSL code here
beans = {
//    restAuthenticationFilter(MiddolRestAuthenticationFilter) {
//        def conf = SpringSecurityUtils.securityConfig
//        authenticationManager = ref('authenticationManager')
//        authenticationSuccessHandler = ref('restAuthenticationSuccessHandler')
//        authenticationFailureHandler = ref('restAuthenticationFailureHandler')
//        authenticationDetailsSource = ref('authenticationDetailsSource')
//        credentialsExtractor = ref('credentialsExtractor')
//        endpointUrl = conf.rest.login.endpointUrl
//        tokenGenerator = ref('tokenGenerator')
//        tokenStorageService = ref('tokenStorageService')
//        authenticationEventPublisher = ref('authenticationEventPublisher')
//        requestMatcher = ref('restAuthenticationFilterRequestMatcher')
//    }

    //自定义多种登录方式
    userDetailsService(MiddolCustomUserDetailsService)

    userPasswordEncoderListener(UserPasswordEncoderListener)

    // 过滤敏感数据
    userRenderer(JsonRenderer, User) {
        excludes = ['password']
    }

    def conf = SpringSecurityUtils.securityConfig

    restAuthenticationSuccessHandler(MiddolAuthenticationSuccessHandler) {
        renderer = ref('accessTokenJsonRenderer')
    }

    restAuthenticationFailureHandler(MiddolAuthenticationFailureHandler) {
        statusCode = conf.rest.login.failureStatusCode?: HttpServletResponse.SC_UNAUTHORIZED
    }

    restAccessDeniedHandler(MiddolTokenAccessDeniedHandler) {
        errorPage = null //403
    }

    accessTokenJsonRenderer(MiddolAccessTokenJsonRenderer){
        usernamePropertyName = conf.rest.token.rendering.usernamePropertyName
        tokenPropertyName = conf.rest.token.rendering.tokenPropertyName
        authoritiesPropertyName = conf.rest.token.rendering.authoritiesPropertyName
        useBearerToken = conf.rest.token.validation.useBearerToken
    }

    auditRequestResolver(MiddolAuditRequestResolver)

    formattedStringConverter(MiddolFormattedStringValueConverter)

    //多语言设置默认中文
    localeResolver(SessionLocaleResolver) {
        defaultLocale= new java.util.Locale('zh_CN')
    }
}
