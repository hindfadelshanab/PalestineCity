package developer.citypalestine8936ps.new_home_feature.posts

import developer.citypalestine8936ps.models.Comment
import developer.citypalestine8936ps.models.User

data class PostModelForAdapter(
    val author: User,
    val post: NewPost,
    val comment: List<Comment> = listOf()
)
