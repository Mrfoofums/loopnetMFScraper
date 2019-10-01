import data.Contact
import data.Listing
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.PrintWriter
import java.lang.StringBuilder

fun main(){

    val doc = Jsoup.connect("https://www.loopnet.com/for-sale/tampa-fl/multifamily-properties/").get()
    val articles = doc.select("article")
//    val urls = ArrayList<String>()
    val listingsList= mutableListOf<Listing>()

    for(article in articles){
        val url = article.select("header> div > h4 > a[href]").attr("abs:href")
        val address = mapAddressData(article.select("header").first())
        val localDoc = Jsoup.connect(url).get()
        val listingTableElement = localDoc.select("table.property-data").first()
        val listingInfoMap = mapListingData(listingTableElement)

        val listing = Listing(address, Contact(), listingInfoMap, url)
        listingsList.add(listing)
    }

    createCSV(listingsList)

}


fun mapListingData(table: Element): MutableMap<String, String> {
    val dataMap = mutableMapOf<String, String>()
    val trs = table.select("tr")

    for(index in trs.indices){
        val curTds = trs[index].select("td")

        val td0 = setTdValue(curTds[0])
        val td1 = setTdValue(curTds[1])
        val td2 = setTdValue(curTds[2])
        val td3 = setTdValue(curTds[3])

        dataMap[td0] = td1
        dataMap[td2] = td3
    }

//    println(dataMap)
    return dataMap
}

fun setTdValue(td:Element): String {
    if(td.select("div").html()!= ""){
        //We are in the APN/PARCEL field or Property SUbtype Field
        return td.select("div").first().html().trim()
    }
    if(td.select("ul").html()!= ""){
        //We are in the APN/PARCEL field or Property SUbtype Field
        return td.select("li").map{ it.html()}.toList().toString().trim()
    }

    return if(td.select("span").html().trim() != "") td.select("span").html().trim() else td.html().trim()
}

fun mapAddressData(headerElement: Element): String{
    val address = headerElement.select("a").attr("title")
    val city = headerElement.select("a").last().html()

    return "$address $city"
}


fun createCSV(listings:  MutableList<Listing>){
    val sb = StringBuilder()
    val writer = PrintWriter("data.tsv")
        sb.append(getCSVHeaders()+"\n")
    for(listing in listings){
        sb.append(listing.writeToCSV()+"\n")
    }
    writer.use{ out->
        out.print(sb.toString())
    }
}
fun getCSVHeaders():String{
    return "Address\tCONTACT\tPrice\tLOT SIZE\tSALE TYPE\tBUILDING SIZE\tCAP RATE\tNo STORIES\tNo Units\tYEAR BUILT\tPROP TYPE\tPARKING RATIO\tProp Sub Type\tZONING\tApt Styl\tParcel Number\tURL"
}