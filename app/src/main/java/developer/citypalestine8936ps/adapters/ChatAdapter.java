package developer.citypalestine8936ps.adapters;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.List;

import developer.citypalestine8936ps.ChatActivity;
import developer.citypalestine8936ps.databinding.ItemContienerReceivedMessageBinding;
import developer.citypalestine8936ps.databinding.ItemContinerSendMessageBinding;
import developer.citypalestine8936ps.models.ChatMessage;

public class ChatAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    final List<ChatMessage> chatMessageList;
    private  String receiverProfileImage;
    private final String senderId ;
    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public void setReceiverProfileImage(String bitmap){
        receiverProfileImage =bitmap;
    }

    public ChatAdapter(List<ChatMessage> chatMessageList, String receiverProfileImage, String senderId) {
        this.chatMessageList = chatMessageList;
        this.receiverProfileImage = receiverProfileImage;
        this.senderId = senderId;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            return new SentMessageViewHolder(
                    ItemContinerSendMessageBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false)
            );
        }else {
            return new ReceivedMessageViewHolder(
                    ItemContienerReceivedMessageBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false)
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT){
            ((SentMessageViewHolder) holder).setData(chatMessageList.get(position));
        }else {
            ((ReceivedMessageViewHolder) holder).setData(chatMessageList.get(position),receiverProfileImage);
        }

    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessageList.get(position).senderId.equals(senderId)){
            return VIEW_TYPE_SENT;
        }else {
            return VIEW_TYPE_RECEIVED;
        }

    }


    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContinerSendMessageBinding binding ;
        SentMessageViewHolder(ItemContinerSendMessageBinding itemContinerSendMessageBinding){
            super(itemContinerSendMessageBinding.getRoot());
            binding = itemContinerSendMessageBinding ;
        }

        void setData(ChatMessage chatMessage){
         //   binding.textMessage.setText(chatMessage.message);


            if (chatMessage.message !=null) {
                binding.textMessage.setText(chatMessage.message);
            }else {
                binding.textMessage.setVisibility(View.INVISIBLE);
            }
            binding.textDateTime.setText(chatMessage.dateTime);
            Log.e("adapterr" , chatMessage.messageImage +"immmmm");
            if (chatMessage.messageImage !=null){

                if (!chatMessage.messageImage.equals("")) {
                    binding.messageImage.setVisibility(View.VISIBLE);
                    Picasso.get().load(chatMessage.messageImage).into(binding.messageImage);
                }

            }

        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContienerReceivedMessageBinding binding;
        ReceivedMessageViewHolder(ItemContienerReceivedMessageBinding itemContienerReceivedMessageBinding){
            super(itemContienerReceivedMessageBinding.getRoot());
            binding = itemContienerReceivedMessageBinding;
        }
        void setData(ChatMessage chatMessage,String  receiverProfileImage){

            if (chatMessage.message !=null) {
                binding.textMessage.setText(chatMessage.message);
            }else {
                binding.textMessage.setVisibility(View.INVISIBLE);
            }
            binding.textDateTime.setText(chatMessage.dateTime);
            if (receiverProfileImage !=null) {
                Picasso.get().load(receiverProfileImage)
                        .into(binding.imageProfile);
            //    binding.imageProfile.setImageBitmap(receiverProfileImage);
            }

            Log.e("adapterr" , chatMessage.messageImage +"immmmm");

            if (chatMessage.messageImage !=null) {

                if (!chatMessage.messageImage.equals("")) {
                    binding.messageImage.setVisibility(View.VISIBLE);
                    Picasso.get().load(chatMessage.messageImage).into(binding.messageImage);

                }
            }
        }

    }

    }
