package developer.citypalestine8936ps.new_city_feature

import com.google.firebase.firestore.DocumentId

data class NewCity(
    @DocumentId
    val docId: String = "",
    val cityName: String = "",
    val featuredImage: String = "",
    val lat: Long = -1L,
    val lng: Long = -1L,
)