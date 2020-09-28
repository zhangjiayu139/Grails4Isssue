package middol.base


import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.rest.RestfulController
import middol.enums.ResultCodeEnum
import middol.result.ResultUtils
import middol.utils.FileService

/**
* @Description:    基类（实现基本的增、删、改、查、导入、导出操作）
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/7/22 18:21
* @Version:        1.0
*/
class BaseController<T> extends RestfulController<T> {

    static responseFormats = ['json', 'xml']

    BaseController(Class<T> resource) {
        super(resource)
    }

    BaseService baseService

    FileService fileService

    /**
     * 查询
     * @param max
     */
    @Override
    def index(Integer max){
        render ResultUtils.ok(baseService.index(params, resource) ?: []) as JSON
    }

    /**
     * 新增
     */
    @Override
    @Transactional
    def save(){
        if(handleReadOnly()) {
            return
        }
        def instance = createResource()

        instance.validate()
        if (instance.hasErrors()) {
            transactionStatus.setRollbackOnly()
            render ResultUtils.error(ResultCodeEnum.VALIDATE_ERROR, instance.errors) as JSON// STATUS CODE 422
            return
        }

        saveResource instance
        render ResultUtils.ok(instance) as JSON
    }

    /**
     * 修改
     */
    @Transactional
    @Override
    def update(){
        if(handleReadOnly()) {
            return
        }

        T instance = queryForResource(params.id)
        if (instance == null) {
            transactionStatus.setRollbackOnly()
            render ResultUtils.error(ResultCodeEnum.NOTFOUND) as JSON
            return
        }

        instance.properties = getObjectToBind()

        instance.validate()
        if (instance.hasErrors()) {
            transactionStatus.setRollbackOnly()
            render ResultUtils.error(ResultCodeEnum.VALIDATE_ERROR, instance.errors) as JSON
            return
        }

        updateResource instance
        render ResultUtils.ok(instance) as JSON
    }

    /**
     * 根据id获取
     */
    @Override
    def show(){
        if(handleReadOnly()) {
            return
        }

        T instance = queryForResource(params.id)
        if (instance == null) {
            render ResultUtils.error(ResultCodeEnum.NOTFOUND) as JSON
            return
        }
        render ResultUtils.ok(instance) as JSON
    }

    /**
     * 删除
     */
    @Override
    @Transactional
    def delete(){
        if(handleReadOnly()) {
            return
        }

        def instance = queryForResource(params.id)
        if (instance == null) {
            transactionStatus.setRollbackOnly()
            render ResultUtils.error(ResultCodeEnum.NOTFOUND) as JSON
            return
        }

        deleteResource instance
        render ResultUtils.ok(ResultCodeEnum.OK.getMsg()) as JSON
    }

    /**
     * 批量删除
     */
    @Transactional
    def deleteBatch(){
        if(handleReadOnly()) {
            return
        }
        List<T> list = resource.findAllByIdInList(request.JSON.ids as List) ?: []
        if (list.size() == 0) {
            transactionStatus.setRollbackOnly()
            render ResultUtils.error(ResultCodeEnum.NOTFOUND) as JSON
            return
        }
        resource.deleteAll(list)
        render ResultUtils.ok(ResultCodeEnum.OK.getMsg()) as JSON
    }

    /**
     * 附件上传
     * @return
     */
    def upload(){
        def file = request.getFile("file") ?: request.getFiles("files")
        println "上传附件===================="
        render fileService.upload(file, resource.simpleName, params.moduleId) as JSON
    }
}
