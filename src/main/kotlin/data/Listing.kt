package data

data class Listing( val address: String, var contact: Contact , val info: Map<String,String>, val url:String){



    fun printAddress(){
        println(address)
    }

    fun printListing(){
        println("${info.get("Price")}  $address")
    }

    fun writeToCSV():String {

        return "$address\t${contact.writeToCSV()}\t${writeInfoMapToCSV()}\t$url"
    }

    private fun writeInfoMapToCSV(): String{
        val sb = StringBuilder()
        sb.append(info["Price"] +"\t")
        sb.append(info.get("Lot Size")+"\t")
        sb.append(info.get("Sale Type")+"\t")
        sb.append(info.get("Building Size")+"\t")
        sb.append(info.get("Cap Rate")+"\t")
        sb.append(info.get("No. Stories")+"\t")
        sb.append(info.get("No. Units")+"\t")
        sb.append(info.get("Year Built")+"\t")
        sb.append(info.get("Property Type")+"\t")
        sb.append(info.get("Parking Ratio")+"\t")
        sb.append(info.get("Property Sub-type")+"\t")
        sb.append(info.get("Zoning Description")+"\t")
        sb.append(info.get("Apartment Style")+"\t")
        sb.append(info.get("APN / Parcel ID"))

        return sb.toString()
    }
}

data class Contact(val name: String = "Forrest", var employer:String="", var phone:String= "", var address:String=""){

    fun writeToCSV():String{
        return "$name"
    }
}