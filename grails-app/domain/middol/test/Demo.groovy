package middol.test

import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.databinding.BindingFormat
import middolauditlog.Auditable
import middolauditlog.Stampable
/**
* @Description:    测试
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/11 10:58
* @Version:        1.0
*/
@GrailsCompileStatic
class Demo implements Serializable, Stampable, Auditable{
//    @BindingFormat("UPPERCASE")
//    String name
//    String code
//    @BindingFormat("yyyyMMdd")
//    Date time = new Date()

    String demo1
    String demo2
    String demo3
    BigDecimal demo4
    String demo5
    String demo6
    Date demo7
    String demo8
    String demo9
    String demo10

    DemoService demoService

    static transients = ['demoService']

    static constraints = {
    }

    static mapping = {
        autowire true
        demo9 type: "text"
        demo8 type: "text"
    }

    def beforeUpdate(){
        println "---------------beforeUpdate---------------"
        println demoService.getData()
    }

    def afterUpdate(){
        println '--------------------afterUpdate--------------------'
        println demoService.getData()
    }
}
