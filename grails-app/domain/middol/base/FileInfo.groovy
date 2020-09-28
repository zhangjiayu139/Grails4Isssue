package middol.base

import middolauditlog.Stampable
/**
* @Description:    文件信息
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/25 15:27
* @Version:        1.0
*/
class FileInfo implements Stampable, Serializable {

    String name          // 文件名
    String path              // 文件存放地址
    String module            // 文件所属模块
    Long moduleId            // 文件所属模块ID
    String url               // 文件使用url
    String description       // 描述
    BigDecimal size         //文件大小

    static constraints = {
        name maxSize: 150
        path maxSize: 150
        module maxSize: 50
        url maxSize: 150
        description maxSize: 100
        size scale: 2
    }

    static mapping = {
        comment "文件信息"
        name comment: "文件名"
        path comment: "文件存放地址"
        module comment: "文件所属模块", index: "moduleIndex"
        moduleId comment: "文件所属模块ID"
        url comment: "文件使用url"
        description comment: "文件使用url"
        size comment: "文件大小"
    }
}
