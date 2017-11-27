package com.example.moya.chatapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Moya on 11/13/2017.
 */

public class UsersViewHolder extends RecyclerView.ViewHolder {
    View view ;

    public UsersViewHolder(View itemView) {
        super(itemView);
        view = itemView;
    }
    public void setName(String name)
    {
        TextView userName = (TextView)view.findViewById(R.id.SingleUser_Name);
        userName.setText(name);
    }

    public void setStatus(String status)
    {
        TextView userName = (TextView)view.findViewById(R.id.SingleUser_Status);
        userName.setText(status);
    }
    public void setImage(String image)
    {
        CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.SingleUser_Image);
        if (!image.equals("Default"))
            Picasso.with(view.getContext()).load(image).placeholder(R.drawable.default_avatar).into(circleImageView);
    }


}
