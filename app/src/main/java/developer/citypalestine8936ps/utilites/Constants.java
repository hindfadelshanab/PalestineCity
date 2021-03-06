package developer.citypalestine8936ps.utilites;

import java.util.HashMap;

public class Constants {

    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_COLLECTION_CITY = "cities";
    public static final String KEY_COLLECTION_VILLAGE = /*"villages"*/"Villages";
    public static final String KEY_COLLECTION_TRADITION = "Tradition";
    public static final String KEY_COLLECTION_BOOK = "Book";
    public static final String KEY_COLLECTION_CITY_LANDMARK = "landmarks";
    public static final String KEY_COLLECTION_NEW_POSTS = "new_posts";
    public static final String KEY_COLLECTION_COMMENTS = "comments";

    public static final String KEY_POSTS_STORAGE_REF = "posts";


    public static final String KEY_NAME = "name";
    public static final String KEY_CITY_NAME = "cityName";
    public static final String KEY_CITY_LAT = "lat";
    public static final String KEY_CITY_LNG = "lng";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_CITY = "city";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PREFERENCE_NAME = "chatAppPreference";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_USER_EMAIL = "userEmail";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_USER = "user";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timesTamp";
    public static final String KEY_COLLECTION_CONVERSATIONS = "conversations";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_RECEIVER_IMAGE = "receiverImage";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_AVAILABILITY = "availability";
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";

    public static final String KEY_POST_DOC_ID = "post_doc_id";
    public static final String KEY_CITY_DOC_ID = "city_doc_id";
    public static final String KEY_IMAGE_PREVIEW_URL = "image_preview_url";
    public static final String KEY_CURRENT_NAME = "current_name";
    public static final String KEY_ON_SUCCESS = "on_success";


    public static HashMap<String, String> remoteMsgHeaders = null;

    public static HashMap<String, String> getremoteMsgHeaders() {

        if (remoteMsgHeaders == null) {
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(REMOTE_MSG_AUTHORIZATION, "key=AAAAdN5O84o:APA91bFlQYyGDDriki5l91xj-6c89clmIqfTwnLn_PEJsZj7lJoLcxo6EocVCluSpObyVePZtW_Cqt5KqoCBxVFUpYnzyMYvF37aNfXs-BiwGryp9jAZ7CtP3dHQ8Ds6CsHjTj4ZKE8r");
            remoteMsgHeaders.put(REMOTE_MSG_CONTENT_TYPE, "application/json");
        }
        return remoteMsgHeaders;
    }

}
