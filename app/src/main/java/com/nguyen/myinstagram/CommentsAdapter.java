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

   // View lookup cache
   private static class ViewHolder {
      ImageView profilePicture;
      TextView username;
      TextView text;
      TextView createdTime;
   }

   @Override
   public View getView(int position, View view, ViewGroup parent) {
      Comment comment = getItem(position);
      // view lookup cache stored in tag
      ViewHolder viewHolder = new ViewHolder();
      if (view == null) {
         view = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);

         viewHolder.profilePicture = (ImageView)view.findViewById(R.id.comment_profile_picture);
         viewHolder.username = (TextView)view.findViewById(R.id.comment_username);
         viewHolder.text = (TextView)view.findViewById(R.id.comment_text);
         viewHolder.createdTime = (TextView)view.findViewById(R.id.comment_created_time);
         view.setTag(viewHolder);
      }
      else
         viewHolder = (ViewHolder)view.getTag();

      // set up profile picture
      Picasso.with(getContext()).load(comment.mProfilePictureUrl).resize(75, 0)
            .transform(new CircleTransform()).into(viewHolder.profilePicture);
      // set up username
      viewHolder.username.setText(Utils.htmlUsername(comment.mUsername));
      // set up comment text
      viewHolder.text.setText(Utils.htmlText(comment.mText));
      // set up relative timestamp
      viewHolder.createdTime.setText(Utils.timeAgo(comment.mCreatedTime));

      return view;
   }
}
