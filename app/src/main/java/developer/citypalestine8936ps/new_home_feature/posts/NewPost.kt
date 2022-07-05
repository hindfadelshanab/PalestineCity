package developer.citypalestine8936ps.new_home_feature.posts

import com.google.firebase.firestore.DocumentId

data class NewPost(
    @DocumentId
    val docId: String = "",
    val content: String = "",
    val time: Long = System.currentTimeMillis(),
    val description: String = "",
    val imageUrl: String = "",
    val likes: MutableList<String> = mutableListOf(),
    val authorDocId: String = ""
)
