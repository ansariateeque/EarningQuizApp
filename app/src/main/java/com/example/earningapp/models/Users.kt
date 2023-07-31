package com.example.earningapp.models

class Users {
    var userid: String = ""
    var name: String = ""
    var age: String = ""
    var email: String = ""
    var password: String = ""
    var profile: String = ""
        get() {
            return field
        }

    constructor(password: String, name: String, age: String, email: String) {
        this.password = password
        this.age = age
        this.name = name
        this.email = email

    }

    constructor()
}