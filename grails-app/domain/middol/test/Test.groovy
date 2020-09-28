package middol.test

import middolauditlog.Auditable
import middolauditlog.Stampable
/**
* @Description:    下拉框数据
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/15 16:57
* @Version:        1.0
*/
class Test implements Serializable, Stampable<Test>, Auditable{

    String code
    String name

    static constraints = {
    }

    static mapping = {
        code index: "code_index"
    }
}
