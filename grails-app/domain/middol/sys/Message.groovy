package middol.sys

import grails.compiler.GrailsCompileStatic
import middolauditlog.Stampable
/**
* @Description:    消息信息
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/8/28 13:06
* @Version:        1.0
*/
@GrailsCompileStatic
class Message implements Serializable, Stampable<Message>{
    String title
    String content

    static constraints = {
    }

    static mapping = {
        content type: "text"
    }
}
