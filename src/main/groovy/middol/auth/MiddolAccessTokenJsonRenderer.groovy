package middol.auth

import grails.converters.JSON
import grails.plugin.springsecurity.rest.oauth.OauthUser
import grails.plugin.springsecurity.rest.token.AccessToken
import grails.plugin.springsecurity.rest.token.rendering.AccessTokenJsonRenderer
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import middol.result.ResultUtils
import org.pac4j.core.profile.CommonProfile
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.util.Assert

@Slf4j
@CompileStatic
/**
* @Description:    自定义登录返回格式
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/7/21 0:54
* @Version:        1.0
*/
class MiddolAccessTokenJsonRenderer implements AccessTokenJsonRenderer  {
    String usernamePropertyName
    String tokenPropertyName
    String authoritiesPropertyName

    Boolean useBearerToken

    String generateJson(AccessToken accessToken) {
        Assert.isInstanceOf(UserDetails, accessToken.principal, "A UserDetails implementation is required")
        UserDetails userDetails = accessToken.principal as UserDetails

        Map result = [
                (usernamePropertyName) : userDetails.username,
                (authoritiesPropertyName) : accessToken.authorities.collect { GrantedAuthority role -> role.authority }
        ]

        if (useBearerToken) {
            result.token_type = 'Bearer'
            result.access_token = accessToken.accessToken

            if (accessToken.expiration) {
                result.expires_in = accessToken.expiration
            }

            if (accessToken.refreshToken) result.refresh_token = accessToken.refreshToken

        } else {
            result["$tokenPropertyName".toString()] = accessToken.accessToken
        }

        if (userDetails instanceof OauthUser) {
            CommonProfile profile = (userDetails as OauthUser).userProfile
            result.email = profile.email
            result.displayName = profile.displayName
        }

        JSON jsonResult = ResultUtils.ok(result) as JSON

        log.debug "Generated JSON:\n${jsonResult.toString(true)}"

        return jsonResult.toString()
    }
}
