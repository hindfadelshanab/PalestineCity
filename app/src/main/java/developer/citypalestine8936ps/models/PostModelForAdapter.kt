package developer.citypalestine8936ps.models

import developer.citypalestine8936ps.new_home_feature.NewPost

data class PostModelForAdapter(
    val author: User,
    val post: NewPost,
    val comment: List<Comment> = listOf()
)
