package com.example.myslack.feature.contacts;

import android.content.Context;
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
import com.example.myslack.model.User;

import java.util.ArrayList;
import java.util.List;

public class AdapterContact extends RecyclerView.Adapter<AdapterContact.AdapterContactViewHolder> {

    private List<User> mUsers;
    private Context mContext;
    private ContactAdapterOnClickListener mContactAdapterOnClickListener;

    public interface ContactAdapterOnClickListener{
        void onMyClickListener(User user);
    }

    public AdapterContact(Context context, ArrayList<User> mUsers, ContactAdapterOnClickListener contactAdapterOnClickListener) {
        this.mContext = context;
        this.mUsers = mUsers;
        this.mContactAdapterOnClickListener = contactAdapterOnClickListener;
    }

    @NonNull
    @Override
    public AdapterContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int idLayoutItemContacts = R.layout.item_contact;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(idLayoutItemContacts, parent, false);
        AdapterContactViewHolder viewHolder = new AdapterContactViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterContactViewHolder holder, int position) {
        User user = mUsers.get(position);

        Glide.with(mContext)
                .load(user.getPhotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imageContact);


        holder.textViewName.setText(user.getNome());
//        holder.textViewMessage.setText(user.get);
//        holder.textViewDate.setText(user.getNome());
//        holder.textViewName.setText(user.getNome());

    }

    @Override
    public int getItemCount() {
        if (mUsers != null) {
            return mUsers.size();
        } else {
            return 0;
        }
    }

    public void updateItems(List<User> items){
        this.mUsers = items;
        notifyDataSetChanged();
    }

    public class AdapterContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageContact;
        public TextView textViewName, textViewMessage, textViewDate, textViewHour;

        public AdapterContactViewHolder(@NonNull View itemView) {
            super(itemView);

            imageContact = itemView.findViewById(R.id.imgItemUser);
            textViewName = itemView.findViewById(R.id.textItemContactName);
            textViewMessage = itemView.findViewById(R.id.textItemContactMessage);
            textViewDate = itemView.findViewById(R.id.textItemContactDate);
            textViewHour = itemView.findViewById(R.id.textItemContactHour);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            User user = mUsers.get(position);
            mContactAdapterOnClickListener.onMyClickListener(user);
        }
    }
}
