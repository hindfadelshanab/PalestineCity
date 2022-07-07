package developer.citypalestine8936ps

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import developer.citypalestine8936ps.utilites.Constants

class SplashActivity : AppCompatActivity() {
    private val SPLASH_DISPLAY_LENGTH = 1000

    private lateinit var database: FirebaseFirestore
    private lateinit var citiesCollectionRef: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }, 2500)

//        generateFakeData()
    }

    private fun generateFakeData() {
        initCities()
    }

    private fun initCities() {
        val allPhotos = listOf(
            "https://firebasestorage.googleapis.com/v0/b/citypalestine-ae1ea.appspot.com/o/jerusalem%2Fj1.jpg?alt=media&token=9f92e9a9-81c3-48bc-9af9-03a58e00991c",
            "https://firebasestorage.googleapis.com/v0/b/citypalestine-ae1ea.appspot.com/o/jerusalem%2Fj2.jpg?alt=media&token=b0b84591-559a-40b5-9586-913d7b2f4f9a",
            "https://firebasestorage.googleapis.com/v0/b/citypalestine-ae1ea.appspot.com/o/jerusalem%2Fj3.jpg?alt=media&token=da353a43-3b65-48c1-a811-1855ccedd640",
            "https://firebasestorage.googleapis.com/v0/b/citypalestine-ae1ea.appspot.com/o/jerusalem%2Fj4.jpg?alt=media&token=4e5f8478-6dee-4329-84a0-10cb71be1ee3",
            "https://firebasestorage.googleapis.com/v0/b/citypalestine-ae1ea.appspot.com/o/jerusalem%2Fj5.jpg?alt=media&token=2febfeba-2e78-4b05-aabd-9c1308b62b2d",
            "https://firebasestorage.googleapis.com/v0/b/citypalestine-ae1ea.appspot.com/o/jerusalem%2Fj6.jpg?alt=media&token=37c4ccb2-3fcc-4298-a8dc-e9ae99602856",
            "https://firebasestorage.googleapis.com/v0/b/citypalestine-ae1ea.appspot.com/o/ramallah%2Fr1.jpg?alt=media&token=22d576de-d940-4b59-87ce-d0c2aaaea158",
            "https://firebasestorage.googleapis.com/v0/b/citypalestine-ae1ea.appspot.com/o/ramallah%2Fr2.jpg?alt=media&token=e3d2d908-392f-49ef-aede-ac62af64aaba",
            "https://firebasestorage.googleapis.com/v0/b/citypalestine-ae1ea.appspot.com/o/ramallah%2Fr3.jpg?alt=media&token=a9b5bdfd-ceba-492e-88ab-23839640f30b",
            "https://firebasestorage.googleapis.com/v0/b/citypalestine-ae1ea.appspot.com/o/ramallah%2Fr3.jpg?alt=media&token=a9b5bdfd-ceba-492e-88ab-23839640f30b",
            "https://firebasestorage.googleapis.com/v0/b/citypalestine-ae1ea.appspot.com/o/ramallah%2Fr4.webp?alt=media&token=a5ece6d8-ae35-403d-bdeb-ad71be4fe1a6",
            "https://firebasestorage.googleapis.com/v0/b/citypalestine-ae1ea.appspot.com/o/ramallah%2Fr5.jpg?alt=media&token=9cdb620e-acda-42ea-ae31-d59177b058d8",
            "https://firebasestorage.googleapis.com/v0/b/citypalestine-ae1ea.appspot.com/o/ramallah%2Fr6.jpg?alt=media&token=1cd97e57-4ad6-4a43-abc5-96d8fc4d88ae",
            "https://firebasestorage.googleapis.com/v0/b/citypalestine-ae1ea.appspot.com/o/gaza.webp?alt=media&token=634d9c21-10b3-4f80-a064-df0b707bbea1",
            "https://firebasestorage.googleapis.com/v0/b/citypalestine-ae1ea.appspot.com/o/gaza1.jpg?alt=media&token=f6a0b1cc-314b-4cab-8e11-670a6d459582",
            "https://firebasestorage.googleapis.com/v0/b/citypalestine-ae1ea.appspot.com/o/gaza2.jpg?alt=media&token=98af2951-585b-4c91-b350-026c28fba613",
            "https://firebasestorage.googleapis.com/v0/b/citypalestine-ae1ea.appspot.com/o/gaza3.jpg?alt=media&token=d8bb9f92-9090-404d-b897-c6f955d76951",
            "https://firebasestorage.googleapis.com/v0/b/citypalestine-ae1ea.appspot.com/o/gaza5.webp?alt=media&token=dac1fd23-b504-4e97-963c-ebd5973fca96",
        )

        val familiesNames = listOf(
            "Family 1",
            "Family 2",
            "Family 3",
            "Family 4",
            "Family 5",
            "Family 6",
            "Family 7",
            "Family 8",
            "Family 9",
            "Family 10",
            "Family 11",
        )
        val martyrsNames = listOf(
            "Martyr 1",
            "Martyr 2",
            "Martyr 3",
            "Martyr 4",
            "Martyr 5",
            "Martyr 6",
            "Martyr 7",
            "Martyr 8",
            "Martyr 9",
            "Martyr 10",
            "Martyr 11",
            "Martyr 12",
            "Martyr 13",
            "Martyr 14",
            "Martyr 15",
            "Martyr 16",
            "Martyr 17",
            "Martyr 18",
            "Martyr 19",
            "Martyr 20",
            "Martyr 21",
            "Martyr 22",
        )

        database = FirebaseFirestore.getInstance()
        citiesCollectionRef = database.collection(Constants.KEY_COLLECTION_CITY)
        citiesCollectionRef
            .document("RQJMzKxVMHGNsHT3bK2L")
            .update("photos", allPhotos)
            .addOnCompleteListener {  }


        /*(0..3).forEach { i ->
            val cityDoc = citiesCollectionRef.document()
            shuffle(allPhotos)
            shuffle(martyrsNames)
            shuffle(familiesNames)
            val city = NewCityData(
                docId = cityDoc.id,
                name = "City $i",
                featuredImage = allPhotos[Random.nextInt(0, allPhotos.size - 1)],
                lat = Random.nextDouble(31.0, 35.00),
                lng = Random.nextDouble(31.0, 35.00),
                photos = allPhotos,
                martyrs = martyrsNames,
                families = familiesNames
            )
            cityDoc.set(city)
        }*/
    }
}