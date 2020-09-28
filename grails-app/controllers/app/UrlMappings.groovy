package app

class UrlMappings {

    static mappings = {
        group("/api"){
            delete "/$controller/$id(.$format)?"(action:"delete")
            get "/$controller(.$format)?"(action:"index")
            get "/$controller/$id(.$format)?"(action:"show")
            post "/$controller(.$format)?"(action:"save")
            put "/$controller/$id(.$format)?"(action:"update")
            patch "/$controller/$id(.$format)?"(action:"patch")

            "/$controller/$action?/$id?(.$format)?" {
                constraints {
                    // apply constraints here
                }
            }
        }

        get "/webService/demo"(controller: "demo", action: "index")

        "/"(controller: 'application', action:'index')
        "500"(controller: "error", action: "handleError")
        "404"(view: '/notFound')
    }
}
