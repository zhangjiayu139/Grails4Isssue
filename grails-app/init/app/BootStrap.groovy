package app

import middol.init.InitDataService

class BootStrap {
    InitDataService initDataService

    def init = { servletContext ->
        initDataService.initAuth()
    }
    def destroy = {
    }
}
