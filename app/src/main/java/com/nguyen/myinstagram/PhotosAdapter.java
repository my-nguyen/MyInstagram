package com.nguyen.myinstagram;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by My on 2/3/2016.
 */
public class PhotosAdapter extends ArrayAdapter<Photo> {
   public PhotosAdapter(Context context, List<Photo> objects) {
      super(context, 0, objects);
   }

   @Override
   // use item.xml for each photo
   public View getView(int position, View view, ViewGroup parent) {
      // extract the Photo at this position
      final Photo photo = getItem(position);
      // check if an existing view is being reused
      if (view == null)
         // if not inflate a new View from template
         view = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);

      // set up profile picture
      ImageView profilePicture = (ImageView)view.findViewById(R.id.photo_profile_picture);
      Picasso.with(getContext()).load(photo.mProfilePictureUrl).transform(new CircleTransform()).into(profilePicture);
      // set up username
      TextView username = (TextView)view.findViewById(R.id.photo_username);
      username.setText(Utils.htmlUsername(photo.mUsername));
      // set up relative timestamp
      TextView createdTime = (TextView)view.findViewById(R.id.photo_created_time);
      createdTime.setText(Utils.timeAgo(photo.mCreatedTime));
      // set up likes count
      TextView likes = (TextView)view.findViewById(R.id.photo_likes);
      likes.setText(String.format("%,d", photo.mLikesCount) + " likes");
      // set up central photo
      ImageView image = (ImageView)view.findViewById(R.id.photo_image);
      // clear out the ImageView, since this could be a recycled item
      // calling setImageResource(0) would cause a crash, so call setImageDrawable(null) instead
      image.setImageDrawable(null);
      // insert the image using Picasso
      Picasso.with(getContext()).load(photo.mImageUrl).placeholder(R.drawable.placeholder).into(image);
      // set up caption
      TextView caption = (TextView)view.findViewById(R.id.photo_caption);
      // caption.setText(photo.mCaption);
      caption.setText(Utils.htmlText(photo.mUsername, photo.mCaption));
      // set up "view all comments"
      TextView viewAllComments = (TextView)view.findViewById(R.id.photo_view_all_comments);
      viewAllComments.setText("View all " + photo.mComments.size() + " comments");
      viewAllComments.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = CommentsActivity.newIntent(getContext(), photo.mComments);
            getContext().startActivity(intent);
         }
      });
      // set up next-to-last comment
      TextView nextToLastCommentView = (TextView)view.findViewById(R.id.photo_next_to_last_comment);
      Comment nextToLastCommentModel = photo.mComments.get(photo.mComments.size() - 2);
      nextToLastCommentView.setText(Utils.htmlText(nextToLastCommentModel.mUsername, nextToLastCommentModel.mText));
      // set up last comment
      TextView lastCommentView = (TextView)view.findViewById(R.id.photo_last_comment);
      Comment lastCommentModel = photo.mComments.get(photo.mComments.size() - 1);
      lastCommentView.setText(Utils.htmlText(lastCommentModel.mUsername, lastCommentModel.mText));

      // return the created item as a view
      return view;
   }
}
