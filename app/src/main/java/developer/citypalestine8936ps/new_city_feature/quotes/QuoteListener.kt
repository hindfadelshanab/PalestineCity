package developer.citypalestine8936ps.new_city_feature.quotes

interface QuoteListener {
    fun onClickCopy(quote: String)
    fun onClickShare(quote: String)
    fun onClickShareWhatsapp(quote: String)
}