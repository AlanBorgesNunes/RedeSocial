package com.app.redesocial

class User {
    var name: String? = null
    var email: String? = null
    var password: String? = null


    constructor() {}

    constructor(name: String?, email: String?, password: String?) {
        this.name = name
        this.email = email
        this.password = password


    }
}