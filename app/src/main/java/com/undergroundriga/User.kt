package com.undergroundriga

class User {

    var id : Int = 0
    var username : String = ""
    var password : String = ""
    var role : String = ""


    constructor(username : String, password : String, role : String){
        this.username = username
        this.password = password
        this.role = role

    }

    constructor(){
    }
}