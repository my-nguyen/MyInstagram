package com.nguyen.myinstagram;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
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

   // apply the ViewHolder pattern to speed up the population of the ListView considerably by
   // caching view lookups for smoother, faster item loading
   private static class ViewHolder {
      ImageView profilePicture;
      TextView username;
      TextView createdTime;
      ImageView image;
      VideoView video;
      ImageView heart;
      TextView likes;
      TextView caption;
      TextView viewAllComments;
      TextView nextToLastCommentView;
      TextView lastCommentView;
   }

   @Override
   public int getViewTypeCount() {
      return 2;
   }

   @Override
   public int getItemViewType(int position) {
      return getItem(position).mMediaType;
   }

   // view lookup cache stored in tag
   ViewHolder viewHolder;

   @Override
   // use item.xml for each photo
   public View getView(int position, View view, ViewGroup parent) {
      // extract the Photo at this position
      final Photo photo = getItem(position);
      // reset ViewHolder
      viewHolder = new ViewHolder();
      // check if this View is being recycled
      if (view == null) {
         // not recycled: inflate new View
         int resource = getItemViewType(position) == Photo.IMAGE_VIEW ? R.layout.item_photo : R.layout.item_video;
         // resource = R.layout.item_photo;
         view = LayoutInflater.from(getContext()).inflate(resource, parent, false);

         // cache the results from findViewById() since these calls are really slow
         viewHolder.profilePicture = (ImageView)view.findViewById(R.id.photo_profile_picture);
         viewHolder.username = (TextView)view.findViewById(R.id.photo_username);
         viewHolder.createdTime = (TextView)view.findViewById(R.id.photo_created_time);
         if (photo.mMediaType == Photo.IMAGE_VIEW)
            viewHolder.image = (ImageView) view.findViewById(R.id.photo_media);
         else
            viewHolder.video = (VideoView)view.findViewById(R.id.photo_media);
         viewHolder.heart = (ImageView)view.findViewById(R.id.photo_heart);
         viewHolder.likes = (TextView)view.findViewById(R.id.photo_likes);
         viewHolder.caption = (TextView)view.findViewById(R.id.photo_caption);
         viewHolder.viewAllComments = (TextView)view.findViewById(R.id.photo_view_all_comments);
         if (photo.mComments.size() >= 1) {
            if (photo.mComments.size() >= 2)
               viewHolder.nextToLastCommentView = (TextView)view.findViewById(R.id.photo_next_to_last_comment);
            viewHolder.lastCommentView = (TextView)view.findViewById(R.id.photo_last_comment);
         }
         view.setTag(viewHolder);
      }
      else
         // this View is recycled, so re-use the cached results from findViewById() saved earlier
         viewHolder = (ViewHolder)view.getTag();

      // set up profile picture
      Picasso.with(getContext()).load(photo.mProfilePictureUrl).resize(75, 0)
            .transform(new CircleTransform()).into(viewHolder.profilePicture);
      // set up username
      viewHolder.username.setText(Utils.htmlUsername(photo.mUsername));
      // set up relative timestamp
      viewHolder.createdTime.setText(Utils.timeAgo(photo.mCreatedTime));
      // set up media (photo or video)
      int widthPixels = getContext().getResources().getDisplayMetrics().widthPixels;
      float ratio = photo.mMedia.mHeight / photo.mMedia.mWidth;
      if (photo.mMediaType == Photo.IMAGE_VIEW) {
         // set up central photo. first clear out the ImageView, since this could be a recycled item
         // calling setImageResource(0) would cause a crash, so call setImageDrawable(null) instead
         viewHolder.image.setImageDrawable(null);
         // set the ImageView width to screen width and the height to correct calculated height to
         // maintain the aspect ratio of the image
         viewHolder.image.getLayoutParams().width = widthPixels;
         viewHolder.image.getLayoutParams().height = (int)(widthPixels * ratio);
         // insert the image using Picasso
         Picasso.with(getContext()).load(photo.mMedia.mUrl).placeholder(R.drawable.placeholder)
               .into(viewHolder.image);
      }
      else {
         // set up video
         viewHolder.video.getLayoutParams().width = widthPixels;
         viewHolder.video.getLayoutParams().height = (int)(widthPixels * ratio);
         viewHolder.video.setVideoPath(photo.mMedia.mUrl);
         MediaController mediaController = new MediaController(getContext());
         mediaController.setAnchorView(viewHolder.video);
         viewHolder.video.setMediaController(mediaController);
         viewHolder.video.requestFocus();
         viewHolder.video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
               viewHolder.video.start();
            }
         });
      }
      // set up heart
      Picasso.with(getContext()).load(R.drawable.blueheart).resize(25, 0).into(viewHolder.heart);
      // set up likes count
      viewHolder.likes.setText(String.format("%,d", photo.mLikesCount) + " likes");
      // set up caption
      viewHolder.caption.setText(Utils.htmlText(photo.mUsername, photo.mCaption));
      // set up "view all comments"
      viewHolder.viewAllComments.setText("View all " + photo.mComments.size() + " comments");
      viewHolder.viewAllComments.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Comment header = new Comment(photo.mProfilePictureUrl, photo.mUsername,
                  photo.mCaption, photo.mCreatedTime);
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
            Comment nextToLastCommentModel = photo.mComments.get(photo.mComments.size() - 2);
            viewHolder.nextToLastCommentView.setText(Utils.htmlText(nextToLastCommentModel.mUsername,
                  nextToLastCommentModel.mText));
         }
         // set up last comment
         Comment lastCommentModel = photo.mComments.get(photo.mComments.size() - 1);
         viewHolder.lastCommentView.setText(Utils.htmlText(lastCommentModel.mUsername, lastCommentModel.mText));
      }

      // return the created item as a view
      return view;
   }
}
