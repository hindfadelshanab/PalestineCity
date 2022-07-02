package developer.citypalestine8936ps.listeners


import developer.citypalestine8936ps.models.Post

interface PostListener {

    fun onClickLike(post: Post)
    fun onPostClicked(post: Post)

}