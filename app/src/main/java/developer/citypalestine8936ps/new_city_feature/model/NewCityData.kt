package developer.citypalestine8936ps.new_city_feature.model

data class NewCityData(
//    @DocumentId
    val docId: String = "",
    val name: String = "",
    val featuredImage: String = "",
    val lat: Double = -1.0,
    val lng: Double = -1.0,

    val photos: List<String> = listOf(),
    val families: List<String> = listOf(),
    val martyrs: List<String> = listOf()
)