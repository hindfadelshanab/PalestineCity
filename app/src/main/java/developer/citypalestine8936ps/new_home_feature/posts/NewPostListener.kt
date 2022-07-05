package developer.citypalestine8936ps.new_home_feature.posts

interface NewPostListener {
    fun onClickAuthorImage(post: NewPost)
    fun onClickLike(post: NewPost)
    fun onClickImage(post: NewPost)
    fun onClickComment(post: NewPost)
}