package middol.utils

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
/**
* @Description:    多语言工具类
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/11 18:40
* @Version:        1.0
*/
class LangService {
    MessageSource messageSource

    /**
     * 获取中英文数据
     * @param key           key名称
     * @param paramsValue   参数 ['aaaa', 'bbbb']
     * @return
     */
    def getMessage(String key, Object[] paramsValue = []){
        def msg = messageSource.getMessage(key, paramsValue, null, LocaleContextHolder.getLocale())
        return msg
    }
}
