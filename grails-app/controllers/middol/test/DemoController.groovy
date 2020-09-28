package middol.test

import grails.compiler.GrailsCompileStatic
import grails.gorm.PagedResultList
import grails.rest.*
import grails.converters.*
import groovy.transform.CompileStatic
import middol.base.BaseController
import middol.result.ResultUtils
import middol.utils.FileService
import org.hibernate.criterion.Restrictions
import org.springframework.context.MessageSource
import org.springframework.web.servlet.support.RequestContextUtils
/**
* @Description:    中英文测试
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/14 14:09
* @Version:        1.0
*/
class DemoController extends BaseController<Demo> {
	static responseFormats = ['json', 'xml']

    DemoController() {
        super(Demo)
    }

    DemoService demoService

    def test(){
//        def locale = RequestContextUtils.getLocale(request)
//        def msg = messageSource.getMessage("demo.test", [] as Object[], null, locale)
//        render msg
        render demoService.test()
    }



    /**
     * 批量导入
     * @return
     */
    def batchImport(){
        render demoService.batchImport(request.getFile("file")) as JSON
    }
}
