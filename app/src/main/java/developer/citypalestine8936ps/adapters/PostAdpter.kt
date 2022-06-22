package developer.citypalestine8936ps.adapters


import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.common.base.Objects
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import developer.citypalestine8936ps.R
import developer.citypalestine8936ps.listeners.PostListener
import developer.citypalestine8936ps.models.Post
import developer.citypalestine8936ps.utilites.Constants
import java.lang.Exception


class PostAdpter(private val mList: List<Post>, var context: FragmentActivity?
, var userImage:String , var userId:String ,  var  postListener: PostListener
) : RecyclerView.Adapter<PostAdpter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)
        return ViewHolder(view)
    }

    var db: FirebaseFirestore =FirebaseFirestore.getInstance()

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]

        holder.textViewDec.text = itemsViewModel.postDec
        holder.textViewUserName.text = itemsViewModel.userName

        if (userId == ""){
            holder.textViewNumberOflike.visibility =View.GONE
            holder.likeImage.visibility =View.GONE
        }
        if (itemsViewModel.likeBy!!.contains(userId)){
            holder.likeImage.setImageResource(R.drawable.ic__fill_heart)
        }else{
            holder.likeImage.setImageResource(R.drawable.ic_heart)
        }



        holder.imageShare.setOnClickListener {
            val intent= Intent()
            intent.action= Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,itemsViewModel.postDec)
            intent.type="text/plain"
            context?.startActivity(Intent.createChooser(intent,"Share To:"))
        }

        holder.textViewNumberOflike.text = "${itemsViewModel.numberOfNum.toString()} اعجاب"



        holder.itemView.setOnClickListener { view -> postListener.onPostClicked(itemsViewModel)

        }
        holder.linearComment.setOnClickListener { view -> postListener.onPostClicked(itemsViewModel)

        }



        holder.likeImage.setOnClickListener{



            if(!itemsViewModel.likeBy!!.contains(userId)) {

                Log.e("hhhdhhdhhdhd" , userId +"hshhs");
                itemsViewModel.likeBy!!.add(userId);
                holder.likeImage.setImageResource(R.drawable.ic__fill_heart)
            }


            var post =Post()
            post.postDec =itemsViewModel.postDec
            post.postId =itemsViewModel.postId
            post.postImage =itemsViewModel.postImage
            post.likeBy =   itemsViewModel.likeBy
            post.isLike =true
            post.cityId = itemsViewModel.cityId
            post.numberOfComment = itemsViewModel.numberOfComment
            post.numberOfNum = itemsViewModel.likeBy!!.size
            post.userImage = itemsViewModel.userImage


            db.collection(Constants.KEY_COLLECTION_CITY)
                .document(itemsViewModel.cityId.toString()).collection("Post")
                .document(itemsViewModel.postId.toString())
                .set(post).addOnSuccessListener(OnSuccessListener {
                    holder.textViewNumberOflike.text="${post.likeBy!!.size.toString()}اعجاب "

                })
//            db.collection(Constants.KEY_COLLECTION_CITY).get().addOnSuccessListener{queryDocumentSnapshots ->
//                for (documentSnapshot in queryDocumentSnapshots){
//                    documentSnapshot.id
//                    db.collection(Constants.KEY_COLLECTION_CITY).document(documentSnapshot.id).
//                    collection("Post")
//                        .document(itemsViewModel.postId.toString())
//                        .set(post).addOnSuccessListener { queryDocumentSnapshots1 ->
//                            holder.textViewNumberOflike.text="${post.likeBy!!.size.toString()}اعجاب "
//
//                            Log.e("pisss" , itemsViewModel.postId.toString())
//
//                        }
//                }
//            }
//            db.collection(Constants.KEY_COLLECTION_CITY).document(itemsViewModel.postId.toString())
//                .set(post).addOnSuccessListener {
//                //  Toast.makeText(context ,"post update",Toast.LENGTH_LONG).show()
//
//
//                holder.textViewNumberOflike.text="${post.likeBy!!.size.toString()}اعجاب "
//
//
//
//            }
            //     updatePost(itemsViewModel.postId.toString() ,post )

        }

//        db.collection("Post").document(itemsViewModel.postId.toString()).collection("Comment").get().addOnSuccessListener { docs ->
//
//            Log.e("number of comment" , "size : ${docs.size()}");
//            holder.textViewNumberOfComment.text = "${docs.size()} تعليق"
////            if (postListener !=null){
////
////            }
//        }


        if (itemsViewModel.postImage=="" ||itemsViewModel.postImage ==null){
            holder.imageView.visibility=View.GONE
        }else if (itemsViewModel.postImage !=null) {
            Picasso.get()
                .load(itemsViewModel.postImage)
                .into(holder.imageView , object :Callback{
                    override fun onSuccess() {
                        holder.imageView.visibility =View.VISIBLE

                    }

                    override fun onError(e: Exception?) {

                    }
                })

        }


















        Picasso.get()
            .load(itemsViewModel.userImage)
            .into(holder.imageViewUser)
//
//        val bytes: ByteArray =
//            Base64.decode(userImage, Base64.DEFAULT)
//        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//        holder.imageViewUser.setImageBitmap(bitmap)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageViewUser: ImageView = itemView.findViewById(R.id.image_post_user_photo)
        val textViewDec: TextView = itemView.findViewById(R.id.txt_post_description)
        val textViewUserName: TextView = itemView.findViewById(R.id.txt_post_user_name)
        val imageView: ImageView = itemView.findViewById(R.id.img_postImage)
        val textViewClubName: TextView = itemView.findViewById(R.id.txt_post_club_name)
       val textViewNumberOflike: TextView = itemView.findViewById(R.id.txt_numberOfLike)
       val textViewNumberOfComment: TextView = itemView.findViewById(R.id.txt_numberOfComment)
        val imageShare: LinearLayout = itemView.findViewById(R.id.layoyt_share)
    val  linearComment : LinearLayout = itemView.findViewById(R.id.linearComment);
        val  linearLike : LinearLayout = itemView.findViewById(R.id.linearLike);
        val likeImage: ImageView =itemView.findViewById<ImageView>(R.id.img_like)

    }


}
