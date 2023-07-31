package com.example.earningapp.models

class HistoryModel{

    var timeanddate:String=""
    var rupees:String=""
    var withdrawalOrEarned:String=""


    //decide variable is used to decide either we have added the coin or withdrawa coin in history
    //true for earned coin  and false for withdrawal
    constructor(timeanddate:String,rupees:String,decide:Boolean){
        this.timeanddate=timeanddate
        this.rupees=rupees
        if(decide){
            withdrawalOrEarned="Credit"
        }else{
            withdrawalOrEarned="Debit"
        }
    }
    constructor()

}

