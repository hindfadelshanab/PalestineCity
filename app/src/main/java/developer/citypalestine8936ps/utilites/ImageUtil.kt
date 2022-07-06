package developer.citypalestine8936ps.utilites

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.squareup.picasso.Picasso
import developer.citypalestine8936ps.R

fun ImageView.loadImageUrl(context: Context, url: String) = apply {
    if (url.isEmpty()) return@apply
    Picasso
        .get()
        .load(url)
        .placeholder(createCircularProgressDrawable(context))
        .into(this)
}

fun createCircularProgressDrawable(context: Context): CircularProgressDrawable {
    val cpd: CircularProgressDrawable = CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 30f
        arrowEnabled = true
        start()
    }
    return cpd
}

fun ImageView.load(context: Context, url: String, previewOnClick: Boolean = false) {
    Glide.with(context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .error(R.drawable.error)
        .placeholder(createCircularProgressDrawable(context))
        .into(this)
    if (previewOnClick) {
        setOnClickListener {

        }
    }
}