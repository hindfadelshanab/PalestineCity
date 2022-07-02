package developer.citypalestine8936ps.models

data class NewPost(
    val authorDocId: String,
    val title: String,
    val time: Long = System.currentTimeMillis(),
    val description: String = "",
    val imageUrl: String = "",
    val likes: List<String> = listOf()
)
