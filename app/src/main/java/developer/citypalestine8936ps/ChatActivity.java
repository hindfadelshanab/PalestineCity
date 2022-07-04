package developer.citypalestine8936ps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import developer.citypalestine8936ps.adapters.ChatAdapter;
import developer.citypalestine8936ps.databinding.ActivityChatBinding;
import developer.citypalestine8936ps.models.ChatMessage;
import developer.citypalestine8936ps.models.User;

import developer.citypalestine8936ps.network.ApiClient;
import developer.citypalestine8936ps.network.ApiService;
import developer.citypalestine8936ps.utilites.Constants;
import developer.citypalestine8936ps.utilites.PreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends BaseActivity {

    ActivityChatBinding binding;
    private User receiverUser;
    private List<ChatMessage> chatMessageList;
    private ChatAdapter chatAdapter;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    private String conversationId = null;
    private Boolean isReceiverAvailable = false;
    private Uri encodedImage = null;
    private int RESULT_LOAD_IMG = 1100;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
        loadReceiverDetails();
        binding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        init();
        listenMessage();
    }

//    private Bitmap getBitmapFromEncodedString(String encodedImage) {
//        if (encodedImage != null) {
//            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
//            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//
//        } else {
//            return null;
//        }
//    }

    private void init() {
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessageList, receiverUser.image, preferenceManager.getString(Constants.KEY_USER_ID));
        binding.chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
        binding.layoutPhoto.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {

                showPopupDialog(binding.layoutPhoto);


            }
        });

    }

    private void showPopupDialog(FrameLayout imageView) {

        PopupMenu popupMenu = new PopupMenu(this, imageView);
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        popupMenu.getMenuInflater().inflate(R.menu.photo_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_gallery) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    launchSomeActivity.launch(intent);
                } else if (menuItem.getItemId() == R.id.action_camer) {
                    dispatchTakePictureIntent();

                }

                return true;
            }
        });

        popupMenu.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void dispatchTakePictureIntent() {

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                Bitmap photo = null;
                if (data != null) {
                    photo = (Bitmap) data.getExtras().get("data");
                    encodedImage = getImageUri(ChatActivity.this, photo);
                    Log.e("imagee", encodedImage + "lll66l");
                    Log.e("imagee", data.getExtras().get("data") + "lll6l");
                }
                binding.imageInChat.setImageBitmap(photo);
            }

        }

    }



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data.getData() != null) {
                            Uri imageUri = data.getData();
                            Bitmap photo = null;

                            InputStream inputStream = null;
                            try {
                                inputStream = getContentResolver().openInputStream(imageUri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                                binding.imageInChat.setImageBitmap(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            encodedImage = imageUri;

                        }
                    }
                }
            });


    private void sendMessage(Uri fileUri , String txt) {


        if (fileUri != null) {
            String fileName = UUID.randomUUID().toString() + ".jpg";
            StorageReference refStorage = FirebaseStorage.getInstance().getReference().child("images/"+fileName);
            refStorage.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {

                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();

                                    Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();

                                    HashMap<String, Object> message = new HashMap<>();
                                    message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
                                    message.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
                                    message.put(Constants.KEY_MESSAGE, txt);
                                    message.put(Constants.KEY_TIMESTAMP, new Date());
                                    message.put("messageImage" , imageUrl);
                                    database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
                                    if (conversationId != null) {
                                        updateConversation(binding.inputMessage.getText().toString());
                                    } else {
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
                                        map.put(Constants.KEY_SENDER_NAME, preferenceManager.getString(Constants.KEY_NAME));
                                        map.put(Constants.KEY_SENDER_IMAGE, preferenceManager.getString(Constants.KEY_IMAGE));
                                        map.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
                                        map.put(Constants.KEY_RECEIVER_NAME, receiverUser.name);
                                        map.put(Constants.KEY_RECEIVER_IMAGE, receiverUser.image);
                                        map.put(Constants.KEY_LAST_MESSAGE, binding.inputMessage.getText().toString());
                                        map.put(Constants.KEY_TIMESTAMP, new Date());
                                        addConversation(map);

                                    }
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatActivity.this, "fail:" + e.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    });
        }else {
            HashMap<String, Object> message = new HashMap<>();
            message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
            message.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
            message.put(Constants.KEY_MESSAGE, txt);
            message.put(Constants.KEY_TIMESTAMP, new Date());
            message.put("messageImage", "");
            database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
            if (conversationId != null) {
                updateConversation(binding.inputMessage.getText().toString());
            } else {
                HashMap<String, Object> map = new HashMap<>();
                map.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
                map.put(Constants.KEY_SENDER_NAME, preferenceManager.getString(Constants.KEY_NAME));
                map.put(Constants.KEY_SENDER_IMAGE, preferenceManager.getString(Constants.KEY_IMAGE));
                map.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
                map.put(Constants.KEY_RECEIVER_NAME, receiverUser.name);
                map.put(Constants.KEY_RECEIVER_IMAGE, receiverUser.image);
                map.put(Constants.KEY_LAST_MESSAGE, binding.inputMessage.getText().toString());
                map.put(Constants.KEY_TIMESTAMP, new Date());
                addConversation(map);

            }
        }




        Log.e("hinnn", "mslldk");
        if (!isReceiverAvailable) {
            try {

                JSONArray tokens = new JSONArray();
                tokens.put(receiverUser.token);

                JSONObject data  = new JSONObject();
                data.put(Constants.KEY_USER_ID , preferenceManager.getString(Constants.KEY_USER_ID));
                data.put(Constants.KEY_NAME , preferenceManager.getString(Constants.KEY_NAME));
                data.put(Constants.KEY_FCM_TOKEN , preferenceManager.getString(Constants.KEY_FCM_TOKEN));
                data.put(Constants.KEY_MESSAGE ,binding.inputMessage.getText().toString());

                JSONObject body  = new JSONObject();
                body.put(Constants.REMOTE_MSG_DATA,data);
                body.put(Constants.REMOTE_MSG_REGISTRATION_IDS,tokens);

                sendNotification(body.toString());

            } catch (Exception e) {
                showToast(e.getMessage());
            }
        }
        binding.inputMessage.setText(null);
        encodedImage =null;
        binding.imageInChat.setImageResource(R.drawable.ic_baseline_photo_24);

    }

    private void showToast(String messgae) {
        Toast.makeText(this, messgae, Toast.LENGTH_SHORT).show();
    }

    private void sendNotification(String messageBody) {
        ApiClient.getClient().create(ApiService.class).sendMessage(Constants.getremoteMsgHeaders()
                , messageBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                if (response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            JSONObject responseJson = new JSONObject(response.body());
                            JSONArray results = responseJson.getJSONArray("results");
                            if (responseJson.getInt("failure") == 1) {
                                JSONObject error = (JSONObject) results.get(0);
                                showToast(error.getString("error"));
                                return;
                            }
                        }

                    } catch (JSONException ignored) {

                    }
                    showToast("Notification Send Successfully");
                } else {
                    showToast("Error" + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                showToast(t.getMessage());

            }
        });
    }

    private void listenAvailabilityOfReceiver() {
        database.collection(Constants.KEY_COLLECTION_USERS)
                .document(receiverUser.id)
                .addSnapshotListener(ChatActivity.this, (value, error) -> {
                    if (error != null) {
                        return;
                    }
                    if (value != null) {
                        if (value.getLong(Constants.KEY_AVAILABILITY) != null) {
                            int availability = Objects.requireNonNull(value.getLong(Constants.KEY_AVAILABILITY)).intValue();
                            isReceiverAvailable = availability == 1;
                        }
                        receiverUser.token = value.getString(Constants.KEY_FCM_TOKEN);
                        if (receiverUser.image == null){
                            receiverUser.image = value.getString(Constants.KEY_IMAGE);
                            chatAdapter.setReceiverProfileImage(receiverUser.image);
                            chatAdapter.notifyItemRangeInserted(0,chatMessageList.size());
                        }

                    }

                    if (isReceiverAvailable) {
                        binding.textAvailability.setVisibility(View.VISIBLE);
                    } else {
                        binding.textAvailability.setVisibility(View.GONE);
                    }


                });

    }


    private void listenMessage() {
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverUser.id)
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, receiverUser.id)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = chatMessageList.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.messageImage = documentChange.getDocument().getString("messageImage");
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    Log.e("hin", chatMessage.senderId + "*******");
                    Log.e("hin", chatMessage.message + "******");
                    chatMessageList.add(chatMessage);
                }
            }
            Collections.sort(chatMessageList, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if (count == 0) {
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.notifyItemRangeInserted(chatMessageList.size(), chatMessageList.size());
                binding.chatRecyclerView.smoothScrollToPosition(chatMessageList.size() - 1);
            }

            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);
        if (conversationId == null) {
            checkForConversation();
        }
    };

    private void loadReceiverDetails() {
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.textName.setText(receiverUser.name);

    }

    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private void setListener() {
        binding.imageBack.setOnClickListener(view -> onBackPressed());
        binding.layoutSend.setOnClickListener(view ->
                sendMessage(encodedImage , binding.inputMessage.getText().toString()));
    }

    private void addConversation(HashMap<String, Object> conversation) {
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .add(conversation).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                conversationId = documentReference.getId();

            }
        });


    }

    private void updateConversation(String message) {
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .document(conversationId);
        documentReference.update(Constants.KEY_LAST_MESSAGE, message,
                Constants.KEY_TIMESTAMP, new Date());

    }

    private void checkForConversation() {
        if (chatMessageList.size() != 0) {
            checkConversationRemotely(preferenceManager.getString(Constants.KEY_USER_ID), receiverUser.id);
            checkConversationRemotely(receiverUser.id, preferenceManager.getString(Constants.KEY_USER_ID));

        }
    }

    private void checkConversationRemotely(String senderId, String receiverId) {

        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverId)
                .get()
                .addOnCompleteListener(conversationCompleteListener);

    }


    private final OnCompleteListener<QuerySnapshot> conversationCompleteListener = task -> {
        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversationId = documentSnapshot.getId();
        }
    };

//    private   pickImage = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//    ) { result: ActivityResult ->
//        if (result.resultCode == AppCompatActivity.RESULT_OK) {
//            if (result.data != null) {
//                val imageUri = result.data!!.data
//                try {
//                    val inputStream = activity?.contentResolver?.openInputStream(imageUri!!)
//                    val bitmap = BitmapFactory.decodeStream(inputStream)
//                    //binding.imageProfile.setImageBitmap(bitmap)
//                    //       binding.textAddImage.visibility = View.GONE
//                    encodedImage = result.data!!.data!!
//                    if (encodedImage !=null) {
//                        binding.imageForPost.visibility =View.VISIBLE
//                        binding.imageForPost.setImageBitmap(bitmap)
//                    }
//                } catch (e:FileNotFoundException) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
//    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
//        val bytes = ByteArrayOutputStream()
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//        val path = MediaStore.Images.Media.insertImage(
//                inContext.getContentResolver(),
//                inImage,
//                "Title",
//                null
//        )
//        return Uri.parse(path)
//    }
    @Override
    protected void onResume() {
        super.onResume();
        listenAvailabilityOfReceiver();
    }
}