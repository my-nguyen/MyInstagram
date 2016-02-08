package com.nguyen.myinstagram;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
      Picasso.with(getContext()).load(photo.mProfilePictureUrl).resize(75, 0).transform(new CircleTransform()).into(profilePicture);
      // set up username
      TextView username = (TextView)view.findViewById(R.id.photo_username);
      username.setText(Utils.htmlUsername(photo.mUsername));
      // set up relative timestamp
      TextView createdTime = (TextView)view.findViewById(R.id.photo_created_time);
      createdTime.setText(Utils.timeAgo(photo.mCreatedTime));
      // set up likes count
      TextView likes = (TextView)view.findViewById(R.id.photo_likes);
      likes.setText(String.format("%,d", photo.mLikesCount) + " likes");
      if (photo.mVideoUrl == null) {
         // set up central photo. first clear out the ImageView, since this could be a recycled item
         // but since calling setImageResource(0) would cause a crash, call setImageDrawable(null)
         // instead. then insert the image using Picasso
         ImageView image = (ImageView) view.findViewById(R.id.photo_image);
         image.setImageDrawable(null);
         Picasso.with(getContext()).load(photo.mImageUrl).fit().placeholder(R.drawable.placeholder).into(image);
      }
      else {
         // set up videoView
         final VideoView videoView = (VideoView)view.findViewById(R.id.photo_video);
         videoView.setVideoPath(photo.mVideoUrl);
         Log.i("NGUYEN", "setting up video from " + photo.mVideoUrl);
         MediaController mediaController = new MediaController(getContext());
         mediaController.setAnchorView(videoView);
         videoView.setMediaController(mediaController);
         videoView.requestFocus();
         videoView.start();
         /*
         videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
               Log.i("NGUYEN", "PLAYING videoView from " + photo.mVideoUrl);
               videoView.start();
            }
         });
         */
      }
      // set up caption
      TextView caption = (TextView)view.findViewById(R.id.photo_caption);
      caption.setText(Utils.htmlText(photo.mUsername, photo.mCaption));
      // set up "view all comments"
      TextView viewAllComments = (TextView)view.findViewById(R.id.photo_view_all_comments);
      viewAllComments.setText("View all " + photo.mComments.size() + " comments");
      viewAllComments.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Comment header = new Comment(photo.mProfilePictureUrl, photo.mUsername, photo.mCaption, photo.mCreatedTime);
            // build a list of Comment's that's comprised of a header and all the accompanying
            // Comments. a header is a special Comment object that includes the original caption and
            // all the relevant info such as profile image, username and created time.
            List<Comment> comments = new ArrayList<>();
            comments.add(header);
            comments.addAll(photo.mComments);
            // start CommentsActivity
            Intent intent = CommentsActivity.newIntent(getContext(), comments);
            getContext().startActivity(intent);
         }
      });
      if (photo.mComments.size() >= 1) {
         if (photo.mComments.size() >= 2) {
            // set up next-to-last comment
            TextView nextToLastCommentView = (TextView) view.findViewById(R.id.photo_next_to_last_comment);
            Comment nextToLastCommentModel = photo.mComments.get(photo.mComments.size() - 2);
            nextToLastCommentView.setText(Utils.htmlText(nextToLastCommentModel.mUsername, nextToLastCommentModel.mText));
         }
         // set up last comment
         TextView lastCommentView = (TextView) view.findViewById(R.id.photo_last_comment);
         Comment lastCommentModel = photo.mComments.get(photo.mComments.size() - 1);
         lastCommentView.setText(Utils.htmlText(lastCommentModel.mUsername, lastCommentModel.mText));
      }

      // return the created item as a view
      return view;
   }
}
