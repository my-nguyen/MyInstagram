package com.nguyen.myinstagram;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by My on 2/6/2016.
 */
public class CommentsAdapter extends ArrayAdapter<Comment> {
   public CommentsAdapter(Context context, List<Comment> comments) {
      super(context, 0, comments);
   }

   @Override
   public View getView(int position, View view, ViewGroup parent) {
      Comment comment = getItem(position);
      if (view == null)
         view = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);
      // set up profile picture
      ImageView profilePicture = (ImageView)view.findViewById(R.id.comment_profile_picture);
      Picasso.with(getContext()).load(comment.mProfilePictureUrl).resize(75, 0).transform(new CircleTransform()).into(profilePicture);
      // set up username
      TextView username = (TextView)view.findViewById(R.id.comment_username);
      username.setText(Utils.htmlUsername(comment.mUsername));
      // set up comment text
      TextView text = (TextView)view.findViewById(R.id.comment_text);
      text.setText(Utils.htmlText(comment.mText));
      // set up relative timestamp
      TextView createdTime = (TextView)view.findViewById(R.id.comment_created_time);
      createdTime.setText(Utils.timeAgo(comment.mCreatedTime));

      return view;
   }
}
