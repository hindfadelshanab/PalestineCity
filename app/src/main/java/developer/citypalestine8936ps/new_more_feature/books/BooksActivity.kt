package developer.citypalestine8936ps.new_more_feature.books

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import developer.citypalestine8936ps.databinding.ActivityBooksBinding
import developer.citypalestine8936ps.utilites.Constants

class BooksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBooksBinding

    private lateinit var database: FirebaseFirestore
    private lateinit var booksCollectionRef: CollectionReference

    private val booksAdapter by lazy {
        BooksAdapter(this, mutableListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBooksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseFirestore.getInstance()
        booksCollectionRef = database.collection(Constants.KEY_COLLECTION_BOOK)
        initBooksList()
        initBooksData()
    }

    private fun initBooksList() {
        binding.rvBooks.adapter = booksAdapter
    }

    private fun initBooksData() {
        booksCollectionRef.get().addOnCompleteListener {
            if (!it.isSuccessful) {
                return@addOnCompleteListener
            }
            it.result.documents.forEach { snapshot ->
                val bookCat = snapshot.toObject(BookData::class.java)
                bookCat?.let {
                    booksCollectionRef
                        .document(snapshot.id)
                        .collection("AllBook")
                        .get()
                        .addOnCompleteListener { task ->
                            val book = if (!task.isSuccessful) {
                                BookData(bookCat.bookType)
                            } else {
                                val innerBooks = task.result.toObjects(InnerBook::class.java)
                                BookData(bookCat.bookType, books = innerBooks)
                            }
                            booksAdapter.insertBook(book)
                        }
                }
            }
        }
    }
}