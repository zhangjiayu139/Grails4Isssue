package middol.test

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class DemoServiceSpec extends Specification implements ServiceUnitTest<DemoService>{

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
        expect:"fix me"
            true == false
    }
}
