package developer.citypalestine8936ps.new_home_feature.comments

import developer.citypalestine8936ps.models.User

data class CommentModelForAdapter(
    val comment: NewComment,
    val sender: User
)
