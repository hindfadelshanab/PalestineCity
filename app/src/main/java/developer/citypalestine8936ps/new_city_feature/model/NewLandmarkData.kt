package developer.citypalestine8936ps.new_city_feature.model

data class NewLandmarkData(
    val name: String = "",
    val lat: Double? = 0.0,
    val lng: Double? = 0.0,
    val photos: List<String> = listOf()
)
