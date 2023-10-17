package com.undergroundriga


class Places {

    var PlacesId : Int = 0
    var PlaceName : String = ""
    var Description : String = ""


    constructor(PlaceName : String, Description : String){
        this.PlaceName = PlaceName
        this.Description = Description
    }

    constructor(){
    }
}