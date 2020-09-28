package middol.test


import grails.rest.*
import grails.converters.*
import middol.base.BaseController

class TestController extends BaseController<Test>{
	static responseFormats = ['json', 'xml']

    TestController() {
        super(Test)
    }
}
