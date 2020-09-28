package middol.test

import grails.converters.JSON
import grails.gorm.transactions.Transactional
import middol.constants.i18n.RBACConstants
import middol.excel.DemoImport
import middol.result.ResultUtils
import middol.utils.LangService

import org.springframework.beans.factory.annotation.Autowired

class DemoService {

    @Autowired
    DemoImport demoImport
    LangService langService

    def test() {
//        return langService.getMessage("demo.test", ['aaaaa', 'bbbb'])
        return langService.getMessage(RBACConstants.username)
    }

    /**
     * 批量导入
     * @return
     */
    @Transactional
    def batchImport(file){
        //获取文件，此处可通过上传获取文件
        InputStream inputStream = file.inputStream
        try {
            List list = demoImport.getData(inputStream)
            list.each { v->
                new Demo(v).save()
            }
            if(list.size() > 0){
                Demo.withSession {
                    it.flush()
                    it.clear()
                }
            }
            ResultUtils.ok(list)

        } catch (Exception e) {
            e.printStackTrace()
            ResultUtils.error("Excel导入报错: ${e.message}")
        }finally{
            //关闭输入流
            if(!inputStream){
                inputStream.close()
            }
        }
    }

    def getData(){
        return Demo.list() as JSON
    }
}
