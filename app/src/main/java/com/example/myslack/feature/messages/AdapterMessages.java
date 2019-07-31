package com.example.myslack.feature.messages;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myslack.R;
import com.example.myslack.model.Channel;
import com.example.myslack.model.Message;
import com.example.myslack.model.User;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class AdapterMessages extends RecyclerView.Adapter<AdapterMessages.AdapterMessagesViewHolder> {

    public List<Message> mMessages;
    private User mUserRecipient;
    private Context mContext;

    public static final int SEND_MESSAGE = 0;
    public static final int RECIPIENT_MESSAGE = 1;

    public AdapterMessages(Context mContext, List<Message> mMessages, User userRecipient) {
        this.mContext = mContext;
        this.mMessages = mMessages;
        this.mUserRecipient = userRecipient;
    }

    @NonNull
    @Override
    public AdapterMessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int idLayoutItemMessage = 0;
        LayoutInflater inflater = null;

        if (viewType == SEND_MESSAGE) {
            idLayoutItemMessage = R.layout.item_sender_message;
            inflater = LayoutInflater.from(context);
        } else if (viewType == RECIPIENT_MESSAGE) {
            idLayoutItemMessage = R.layout.item_recipient_message;
            inflater = LayoutInflater.from(context);
        }

        View view = inflater.inflate(idLayoutItemMessage, parent, false);
        AdapterMessagesViewHolder viewHolder = new AdapterMessagesViewHolder(view);

        return viewHolder;
    }


    @Override
    public int getItemViewType(int position) {
        Message message = mMessages.get(position);

        if (message.getIdUser().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return SEND_MESSAGE;
        } else {
            return RECIPIENT_MESSAGE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMessagesViewHolder holder, int position) {
        Message message = mMessages.get(position);

        holder.textMessage.setText(message.getMessage());
        holder.hourMessage.setText(message.getDateHour());
        Glide.with(mContext)
                .load(mUserRecipient.getPhotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imgPhotoUser);

    }

    @Override
    public int getItemCount() {
        if (mMessages != null) {
            return mMessages.size();
        } else {
            return 0;
        }
    }

    public void updateItems(List<Message> items) {
        this.mMessages = items;
        notifyDataSetChanged();
    }

    public class AdapterMessagesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imgPhotoUser;
        public TextView textMessage;
        public TextView hourMessage;


        public AdapterMessagesViewHolder(@NonNull View itemView) {
            super(itemView);

            textMessage = itemView.findViewById(R.id.textMessage);
            hourMessage = itemView.findViewById(R.id.textMessageHour);
            this.imgPhotoUser = itemView.findViewById(R.id.imgMessage);
        }

        @Override
        public void onClick(View v) {

        }

    }

    public class AdapterMessagesViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imgPhotoUser;
        public TextView textMessage;
        public TextView hourMessage;


        public AdapterMessagesViewHolder2(@NonNull View itemView) {
            super(itemView);

            textMessage = itemView.findViewById(R.id.textMessage);
            hourMessage = itemView.findViewById(R.id.textMessageHour);
            this.imgPhotoUser = itemView.findViewById(R.id.imgMessage);
        }

        @Override
        public void onClick(View v) {

        }

    }
}
