package middol.config

import io.micronaut.context.annotation.ConfigurationProperties
/**
* @Description:    上传文件配置
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/14 14:14
* @Version:        1.0
*/
@ConfigurationProperties("upload")
class UploadCfg {
    String rootDir              //根目录
}
