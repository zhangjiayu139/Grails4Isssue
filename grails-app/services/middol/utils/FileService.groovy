package middol.utils

import grails.gorm.transactions.Transactional
import middol.base.FileInfo
import middol.config.UploadCfg
import middol.constants.i18n.CommonConstants
import middol.result.ResultUtils
import org.springframework.beans.factory.annotation.Autowired

import java.text.SimpleDateFormat
/**
* @Description:    文件上传
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/25 16:28
* @Version:        1.0
*/
@Transactional
class FileService {
    @Autowired
    UploadCfg uploadCfg

    LangService langUtils

    /**
     * 附件上传
     * @param f
     * @param dirPath
     * @param moduleId
     */
    def upload(file, dirPath = '', moduleId){
        if(file instanceof List){
            return multiUpload(file, dirPath, moduleId)
        }else{
            return singleUpload(file, dirPath, moduleId)
        }
    }

    /**
     * 单个文件上传
     * @param file 文件
     * @param module 模块名
     * @param moduleId 模块ID
     * @param domainClass 实体类
     * @param variable 文件字段
     * @return
     */
    def singleUpload(f, dirPath = '', moduleId) {
        log.debug("开始上传附件。。。。。")
        def uploadDir = uploadCfg.rootDir
        try {
            if (f.getSize() > 0) {
                String fileName = f.getOriginalFilename()                            // 获取文件名称
                def fileType
                if (fileName != null && fileName != '') {
                    fileType = fileName[fileName.indexOf(".")..fileName.length() - 1]  // 获取文件类型
                }
                dirPath = dirPath.replace('\\', '/')
                File dir = new File(uploadDir + dirPath)
                if (!dir.exists()) {
                    dir.mkdirs()
                }                                                                    // 生成新的文件名 原文件名+时间戳
                def newFileName = fileName.substring(0, fileName.indexOf('.')) + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + fileType
                def filePath = dirPath + "/" + newFileName

                File file = new File(uploadDir + filePath)
                f.transferTo(file)  // 存入对应文件夹，将文件信息存入数据库
                def fi = new FileInfo(fileName: fileName, path: filePath, module: dirPath, url: filePath, moduleId: moduleId, name: fileName, size: f.getSize())
                fi.save(flush: true)
                return ResultUtils.ok(fi)
            }else{
                return ResultUtils.error(langUtils.getMessage(CommonConstants.FILE_UPLOAD_ISNULL))
            }
        } catch (e) {
            e.printStackTrace()
            def msg = langUtils.getMessage(CommonConstants.FILE_UPLOAD_ERROR, e.message)
            log.error("上传文件出错", e.message)
            return ResultUtils.error(msg)
        }
    }

    /**
     * 多附件上传
     * @param f
     * @param dirPath
     * @param moduleId
     */
    def multiUpload(f, dirPath = '', moduleId){
        def list = []
        f.each{ fItem ->
            def result = singleUpload(fItem, dirPath, moduleId)
            if(result.code == 500){
                return result
            }
            list << result?.data
        }
        return ResultUtils.ok(list)
    }
}
