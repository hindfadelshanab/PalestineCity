package developer.citypalestine8936ps.models

import android.os.Parcel
import android.os.Parcelable

//class Post {
//    var postId: String? = null
//    var postDec: String? = null
//    var userImage: String? = null
//    var userName: String? = null
//    var postImage: String? = null
//    var isLike :Boolean? =null
//    var numberOfNum: Int? = null
//    var numberOfComment:Int?= null
//    var likeBy:ArrayList<String> ? =null
//
//}

class Post (
    var postId: String? = null ,
    var postDec: String? = null,
    var isLike :Boolean? =null,
    var postImage: String? = null,
    var numberOfNum: Int? = null,
    var userName: String? = null,
    var userImage: String? = null,
    var numberOfComment:Int?= null ,
    var cityId :String ?=null,
    var likeBy:ArrayList<String> ? =null

):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(postId)
        parcel.writeString(postDec)
        parcel.writeValue(isLike)
        parcel.writeString(postImage)
        parcel.writeValue(numberOfNum)
        parcel.writeString(userName)
        parcel.writeString(userImage)
        parcel.writeValue(numberOfComment)
        parcel.writeString(cityId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }

}