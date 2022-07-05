package developer.citypalestine8936ps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


import developer.citypalestine8936ps.fragment.*
import developer.citypalestine8936ps.databinding.ActivityMainBinding
import developer.citypalestine8936ps.new_city_feature.CitiesFragment
import developer.citypalestine8936ps.new_home_feature.NewHomeFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //    private lateinit var preferenceManager: PreferenceManager
//    private var conversions: MutableList<ChatMessage>? = null
//    private var recentConversationAdapter: RecentConversationAdapter? = null
//    private var database: FirebaseFirestore? = null
    lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(NewHomeFragment())
        //   bottomNav = nav_view
        supportActionBar?.title = getString(R.string.app_name);

        binding.bottomNav.setOnNavigationItemSelectedListener setOnNavigationItemReselectedListener@{it

            when(it.itemId) {
                R.id.homeAction -> {
                    loadFragment(NewHomeFragment())
                    true
                }
                R.id.messageAction -> {
                    loadFragment(ChatFragment())
                    true                }
                R.id.bookAction -> {
                    loadFragment(CitiesFragment())
                    true                }
                R.id.moreAction -> {
                    loadFragment(MoreFragment())
                    true                }
                else -> {
                    false
                }
            }
        }


//        init()
//        preferenceManager = PreferenceManager(getApplicationContext())
//        loadUserDetails()
//        token
//        setListener()
//        conversions!!.clear()
//
//        listenConversion()

    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
//
//    private fun setListener() {
//        binding.imageSignOut.setOnClickListener { view -> signOut() }
//        binding.fabNewChat.setOnClickListener { view ->
//            startActivity(
//                Intent(
//                    getApplicationContext(),
//                    UserActivity::class.java
//                )
//            )
//        }
//    }
//
//    private fun init() {
//        conversions = ArrayList<ChatMessage>()
//        recentConversationAdapter = RecentConversationAdapter(conversions, this)
//        binding.conversionRecycleView.setAdapter(recentConversationAdapter)
//        database = FirebaseFirestore.getInstance()
//    }
//
//    private fun showToast(message: String) {
//        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show()
//    }
//
//    private fun listenConversion() {
//        database!!.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
//            .whereEqualTo(
//                Constants.KEY_SENDER_ID,
//                preferenceManager.getString(Constants.KEY_USER_ID)
//            )
//            .addSnapshotListener(eventListener)
//        database!!.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
//            .whereEqualTo(
//                Constants.KEY_RECEIVER_ID,
//                preferenceManager.getString(Constants.KEY_USER_ID)
//            )
//            .addSnapshotListener(eventListener)
//    }
//
//    private val eventListener =
//        EventListener { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
//            if (error != null) {
//                return@EventListener
//            }
//            if (value != null) {
//
//                for (documentChange in value.documentChanges) {
//                    if (documentChange.type == DocumentChange.Type.ADDED) {
//                        val senderId =
//                            documentChange.document.getString(Constants.KEY_SENDER_ID)
//                        val recevierId =
//                            documentChange.document.getString(Constants.KEY_RECEIVER_ID)
//                        val chatMessage = ChatMessage()
//                        chatMessage.senderId = senderId
//                        chatMessage.receiverId = recevierId
//                        if (preferenceManager.getString(Constants.KEY_USER_ID).equals(senderId)) {
//                            chatMessage.converstionImage =
//                                documentChange.document.getString(Constants.KEY_RECEIVER_IMAGE)
//                            chatMessage.converstionName =
//                                documentChange.document.getString(Constants.KEY_RECEIVER_NAME)
//                            chatMessage.converstionId =
//                                documentChange.document.getString(Constants.KEY_RECEIVER_ID)
//                        } else {
//                            chatMessage.converstionImage =
//                                documentChange.document.getString(Constants.KEY_SENDER_IMAGE)
//                            chatMessage.converstionName =
//                                documentChange.document.getString(Constants.KEY_SENDER_NAME)
//                            chatMessage.converstionId =
//                                documentChange.document.getString(Constants.KEY_SENDER_ID)
//                        }
//                        chatMessage.message =
//                            documentChange.document.getString(Constants.KEY_LAST_MESSAGE)
//                        chatMessage.dateObject =
//                            documentChange.document.getDate(Constants.KEY_TIMESTAMP)
//                        conversions!!.clear()
//                        conversions!!.add(chatMessage)
//                    } else if (documentChange.type == DocumentChange.Type.MODIFIED) {
//                        var i = 0
//                        while (i < conversions!!.size) {
//                            val senderId =
//                                documentChange.document.getString(Constants.KEY_SENDER_ID)
//                            val recevierId =
//                                documentChange.document.getString(Constants.KEY_RECEIVER_ID)
//                            if (conversions!![i].senderId.equals(senderId) && conversions!![i].receiverId.equals(
//                                    recevierId
//                                )
//                            ) {
//                                conversions!![i].message =
//                                    documentChange.document.getString(Constants.KEY_LAST_MESSAGE)
//                                conversions!![i].dateObject =
//                                    documentChange.document.getDate(Constants.KEY_TIMESTAMP)
//                                break
//                            }
//                            i++
//                        }
//                    }
//                }
//                Collections.sort(conversions, Comparator { obj1, obj2 ->
//                    obj2.dateObject.compareTo(
//                        obj1.dateObject
//                    )
//                })
//                recentConversationAdapter?.notifyDataSetChanged()
//                binding.conversionRecycleView.smoothScrollToPosition(0)
//                binding.conversionRecycleView.setVisibility(View.VISIBLE)
//                binding.avi.visibility = View.GONE
//            }
//        }
//    val token: Unit
//        get() {
//            FirebaseMessaging.getInstance().getToken().addOnSuccessListener { token: String ->
//                updateToken(
//                    token
//                )
//            }
//        }
//
//    private fun loadUserDetails() {
//        binding.textName.setText(preferenceManager.getString(Constants.KEY_NAME))
//        val bytes: ByteArray =
//            Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT)
//        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//        binding.imageProfile.setImageBitmap(bitmap)
//    }
//
//    private fun updateToken(token: String) {
//        preferenceManager?.putString(Constants.KEY_FCM_TOKEN, token)
//        val database = FirebaseFirestore.getInstance()
//        val documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(
//            preferenceManager.getString(Constants.KEY_USER_ID)
//        )
//        documentReference.update(Constants.KEY_FCM_TOKEN, token).addOnSuccessListener(
//            OnSuccessListener { unsend: Void? -> showToast("Token update Successfully") })
//            .addOnFailureListener(OnFailureListener { e: Exception? -> showToast("Unable to update token") })
//    }
//
//    fun signOut() {
//        showToast("signing out ....")
//        val database = FirebaseFirestore.getInstance()
//        val documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(
//            preferenceManager.getString(Constants.KEY_USER_ID)
//        )
//        val updates = HashMap<String, Any>()
//        updates[Constants.KEY_FCM_TOKEN] = FieldValue.delete()
//        documentReference.update(updates).addOnSuccessListener { unsend: Void? ->
//            preferenceManager.clear()
//            startActivity(Intent(getApplicationContext(), SignInActivity::class.java))
//            finish()
//        }.addOnFailureListener { e: Exception? -> showToast("unable to sign out") }
//    }
//
//    override fun onConversationClicked(user: User?) {
//        val i = Intent(this, ChatActivity::class.java)
//        i.putExtra(Constants.KEY_USER, user)
//        startActivity(i)
//    }
}