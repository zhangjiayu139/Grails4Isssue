package middol.sys

import grails.compiler.GrailsCompileStatic
import grails.databinding.BindingFormat
/**
* @Description:    登录日志
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/25 15:24
* @Version:        1.0
*/
@GrailsCompileStatic
class LoginRecord implements Serializable{
    static final long serialVersionUID = 1L

    String login_username
    String login_ip
    String login_http
//    String login_token
    @BindingFormat('yyyy-MM-dd HH:mm:ss')
    Date login_indate
    @BindingFormat('yyyy-MM-dd HH:mm:ss')
    Date login_outdate

    static constraints = {
    }

    @Override
    String toString() {
        return "Loginrecord{" +
                "login_id='" + id + '\'' +
                ", login_username=" + login_username +
                ", login_ip='" + login_ip + '\'' +
                ", login_http='" + login_http + '\'' +
//                ", login_token='" + login_token + '\'' +
                ", login_indate=" + login_indate +
                ", login_outdate=" + login_outdate +
                '}';
    }

    static mapping = {
        version false
    }
}
