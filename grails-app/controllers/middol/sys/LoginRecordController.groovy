package middol.sys

import middol.base.BaseController

/**
* @Description:    登录日志
* @Author:         zhangjiayu@middol.com
* @CreateDate:     2020/9/25 15:44
* @Version:        1.0
*/
class LoginRecordController extends BaseController<LoginRecord> {
	static responseFormats = ['json', 'xml']

    LoginRecordController() {
        super(LoginRecord)
    }
}
