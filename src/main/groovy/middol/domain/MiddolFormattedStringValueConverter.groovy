package middol.domain

import grails.databinding.converters.FormattedValueConverter
/**
* @Description:    自动将domain字段值转换大小写
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/10 11:41
* @Version:        1.0
*/
class MiddolFormattedStringValueConverter implements FormattedValueConverter {

    @Override
    Object convert(Object value, String format) {
        if('UPPERCASE' == format) {
            value = value?.toUpperCase()
        } else if('LOWERCASE' == format) {
            value = value?.toLowerCase()
        }
        value
    }

    @Override
    Class<?> getTargetType() {
        String
    }
}
