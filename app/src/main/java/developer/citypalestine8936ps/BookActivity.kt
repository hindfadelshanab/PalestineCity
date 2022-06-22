package developer.citypalestine8936ps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import developer.citypalestine8936ps.adapters.BookTypeAdpter
import developer.citypalestine8936ps.databinding.ActivityBookBinding
import developer.citypalestine8936ps.databinding.FragmentBookBinding
import developer.citypalestine8936ps.models.Book
import developer.citypalestine8936ps.models.BookType

class BookActivity : AppCompatActivity() {

    lateinit var binding: ActivityBookBinding

    lateinit var database: FirebaseFirestore
    lateinit var adapter: BookTypeAdpter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseFirestore.getInstance()
        getSupportActionBar()?.setTitle("Book");

        getAllBook()

    }

    private fun getAllBook() {


        //binding.progressbarBook.visibility=View.VISIBLE
        database.collection("Book").get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                var data: ArrayList<BookType> = ArrayList<BookType>()
                for (documentSnapshot in queryDocumentSnapshots) {
                    val bookType: BookType = BookType()
                    bookType.bookType = documentSnapshot.data.get("bookType")?.toString()

                    bookType.books = ArrayList();

                    database.collection("Book").document(documentSnapshot.id)
                        .collection("AllBook").get().addOnSuccessListener { docs ->
                            var dataBook: ArrayList<Book> = ArrayList<Book>()

                            for (d in docs) {
                                var book: Book = d.toObject(Book::class.java)
                                Log.e("hinbbb",book.bookName.toString() +"booo")
                                Log.e("hinbbb",book.bookPhoto.toString() +"booo")

                                dataBook.add(book)
                            }
                            Log.e("hinbbb", "${dataBook.size} datab book")
                            bookType.books?.addAll(dataBook)
                            data.add(bookType)
                            Log.e("hinbbb", "${bookType.books!!.size}sss")
                            adapter = BookTypeAdpter(data, applicationContext)
                            binding.bookRec.adapter = (adapter)
//                            binding.progressbarBook.visibility=View.GONE
//                            binding.bookRec.visibility=View.VISIBLE

                            Log.e("hin", data.size.toString() + "")

                            binding.bookRec.setLayoutManager(LinearLayoutManager(this))
                        }
                    //


                }

            }).addOnFailureListener(OnFailureListener { e ->
                Log.e("hind", e.message!!)
              //  binding.progressbarBook.visibility=View.VISIBLE
            //    binding.bookRec.visibility=View.GONE
            })
    }

}