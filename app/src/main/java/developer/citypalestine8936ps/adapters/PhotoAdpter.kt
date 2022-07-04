package developer.citypalestine8936ps.adapters


import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.jsibbold.zoomage.ZoomageView
import com.squareup.picasso.Picasso
import developer.citypalestine8936ps.R


class PhotoAdpter(val context: Context, private val mList: List<String>) :
    RecyclerView.Adapter<PhotoAdpter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]
        Picasso.get().load(itemsViewModel).resize(200, 200).into(holder.imageview)

        holder.imageview.setOnClickListener {
            val dialog = Dialog(
                context,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen
            )
            dialog.window!!.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            dialog.setContentView(R.layout.layout_full_image)
            val bmImage: ZoomageView =
                dialog.findViewById<View>(R.id.myZoomageView) as ZoomageView
            Picasso.get().load(itemsViewModel)
                .into(bmImage)
            val button: Button = dialog.findViewById<View>(R.id.btn_dissmiss) as Button
            dialog.setCancelable(true)
            dialog.show()

            button.setOnClickListener(View.OnClickListener { dialog.dismiss() })
        }


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        //  val textViewName: TextView = itemView.findViewById(R.id.txt_book_name)
        val imageview: ImageView = itemView.findViewById(R.id.photo_p)
    }


}
