package middol.base

import cn.hutool.core.date.DateUtil
import grails.converters.JSON
import org.grails.web.servlet.mvc.GrailsWebRequest

class BaseService {

    static final def BASE_FIELDS = ["createUser", "dateCreated", "lastUpdated", "lastUpdatedBy"]

    /**
     * 公共查询接口
     * @param record        查询条件，分页条件
     * @param domainClass   实体类
     */
    def index(Map record, domainClass) {
        def res = queryByParams(record.params, domainClass, record.pageNo, record.pageSize)
        return res
    }

    /**
     * 封装公共查询接口
     * @param params
     * @param domainClass
     * @param pageNo
     * @param pageSize
     * @param otherParams
     * @return
     */
    def queryByParams( params, domainClass, pageNo, pageSize, Closure otherParams = null){
        def queryParams = {}
        if(params){
            if (!(params instanceof Map)){
                params = JSON.parse(params)
            }
            queryParams = queryCandition(params, domainClass)
        }

        if (otherParams){
            queryParams = queryParams.rightShift(otherParams)
        }
        def res
        def criteria = domainClass.createCriteria()
        if(!pageNo || !pageSize){
            res = criteria.list(queryParams)
        }else{
            pageSize = pageSize as Integer
            pageNo = pageNo as Integer
            def offset = (pageNo - 1) * pageSize
            def list = criteria.list([offset: offset, max: pageSize], queryParams)
            res = [total: list.totalCount, data: list]
        }

        return res
    }
    /**
     * 生成查询条件
     */
    Closure queryCandition = { Map params, doMainClass ->
        return {
            params?.each { k, v ->
                if ("sort" == k || k?.contains(",") || doMainClass.getDeclaredField(getParentField(k))) {
                    if (v.value != false && !v.value) {
                        if (k != "sort" && !k?.contains(",")) {
                            isNull(k)
                        }
                    } else {
                        String type = v.equalsType
                        if (type == 'like') {
                            if (getDomainSimpleName(doMainClass, k) == "String") {
                                like(k, "%${valueFormat(v.value, getDomainSimpleName(doMainClass, k))}%")
                            } else {
                                eq(k, valueFormat(v.value, getDomainSimpleName(doMainClass, k)))
                            }
                        } else if (type == 'lt') {
                            lt(k, valueFormat(v.value, getDomainSimpleName(doMainClass, k)))
                        } else if (type == 'gt') {
                            gt(k, valueFormat(v.value, getDomainSimpleName(doMainClass, k)))
                        } else if (type == 'ne') {
                            ne(k, valueFormat(v.value, getDomainSimpleName(doMainClass, k)))
                        } else if (type == 'in') {
                            if ("Integer".equals(getDomainSimpleName(doMainClass, k))) {
                                inList(k, v.value?.tokenize(",") as Integer[])
                            } else if ("Long".equals(getDomainSimpleName(doMainClass, k))) {
                                inList(k, v.value?.tokenize(",") as Long[])
                            } else {
                                inList(k, v.value?.tokenize(","))
                            }
                        } else if (type == 'date') {
                                Date date
                                def next
                                 if (v.value.contains('W')) {
                                    date = DateUtil.parse(v.value, "yyyy-WW")
                                    next = DateUtil.offsetWeek(date, 1)
                                } else{
                                    try {
                                        date = DateUtil.parse(v.value)
                                        next = DateUtil.offsetDay(date, 1)
                                    } catch (Exception e) {
                                        try {
                                            date = DateUtil.parse(v.value, 'yyyy-MM')
                                            next = DateUtil.offsetMonth(date, 1)
                                        } catch (Exception e1) {
                                            date = DateUtil.parse(v.value, 'yyyy')
                                            next = DateUtil.offsetDay(date, 365)
                                        }
                                    }
                                }
                                ge(k, date)
                                lt(k, next)
                        } else if (type == "dateSlot") {
                                def arr = v.value.split(",")
                                Date date1 = !arr[0] || arr[0] == 'null' ? null : DateUtil.parse(arr[0])
                                Date date2 = !arr[1] || arr[1] == 'null' ? null : DateUtil.parse(arr[1])
                                if (date1) {
                                    gt(k, DateUtil.beginOfDay(date1))
                                }
                                if (date2) {
                                    lt(k, DateUtil.endOfDay(date2))
                                }
                        } else if (type == 'or') {
                            or {
                                k.split(",").each { key ->
                                    if (getDomainSimpleName(doMainClass, key) != "String") {
                                        eq(key, valueFormat(v.value, getDomainSimpleName(doMainClass, key)))
                                    } else {
                                        like(key, "%${v.value}%")
                                    }
                                }
                            }
                        } else if (type == 'notIn') {
                            not {
                                inList(k, v.value.tokenize(",").collect { _v ->
                                    valueFormat(_v, getDomainSimpleName(doMainClass, k))
                                })
                            }
                        } else if (type == 'notLike') {
                            not {
                                like(k, "%${valueFormat(v.value, getDomainSimpleName(doMainClass, k))}%")
                            }
                        } else if (type == 'sort') {
                            v.value?.split(";").each { String sortColumn ->
                                if (sortColumn) {
                                    def arr = sortColumn.split(",")
                                    if (arr.size() > 0 && arr[0] && getDomainSimpleName(doMainClass, arr[0])) {
                                        order(arr[0], arr[1] && arr[1] != "" ? arr[1] : "asc")
                                    }
                                }
                            }
                        } else if (type == 'slot') {
                            def arr = v.value.split(",")
                            if (arr.size() > 2) {
                                def t = getDomainSimpleName(doMainClass, k)
                                between(k, valueFormat(arr[0], t), valueFormat(arr[1], t))
                            }
                        } else if (type == 'muchNotLike') {
                            def arr = v.value.split(",")
                            def t = getDomainSimpleName(doMainClass, k)
                            arr.each {
                                not {
                                    like(k, "%${valueFormat(it, t)}%")
                                }
                            }
                        } else if (type == 'isNotNull') {
                            isNotNull(k)
                        } else {
                            if (k != "sort") {
                                if (v.value == "null") {
                                    isNull(k)
                                } else {
                                    eq(k, valueFormat(v.value, getDomainSimpleName(doMainClass, k)))
                                }
                            }
                        }
                    }
                }
            }
//            eq("deleted", false)
            cache(true)
        }
    }

    /**
     * 得到正确的字段
     * @param field 字段名
     * @return
     */
    def getParentField(field){
        if (BASE_FIELDS.contains(field)){
            return "domain_base_BaseDomain__" + field
        }
        return field
    }
    /**
     * 获取domain字段的类型名称
     * @param domainClass   实体类
     * @param property      字段
     * @return
     */
    def getDomainSimpleName(domainClass, property){
        return domainClass.getDeclaredField(getParentField(property)).type.simpleName
    }
    /**
     * 数据格式转换
     * @param value     值
     * @param type      类型
     */
    def valueFormat(value, String type){

        type = type.capitalize()
        switch (type){
            case "Integer":
                value = value as Integer
                break
            case "Long":
                value = value as Long
                break
            case "Float":
                value = value as Float
                break
            case "Double":
                value = value as Double
                break
            case "Boolean":
                value = value as Boolean
                break
            case "BigDecimal":
                value = value as BigDecimal
                break
            case "Date":
                if(!(value instanceof Date)){
                    value = new Date().parse("yyyy-MM-dd HH:mm:ss", value)
                }

                break
        }
        return value
    }

    def getUri(){
        GrailsWebRequest request = GrailsWebRequest.lookup()
        def uri = request?.request?.requestURI
        return uri
    }
}
