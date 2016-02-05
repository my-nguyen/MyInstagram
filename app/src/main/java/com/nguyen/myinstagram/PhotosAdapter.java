package com.nguyen.myinstagram;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Date;
import java.util.List;

/**
 * Created by My on 2/3/2016.
 */
public class PhotosAdapter extends ArrayAdapter<Photo> {
   public PhotosAdapter(Context context, List<Photo> objects) {
      super(context, 0, objects);
   }

   @Override
   // use the item.xmlsplay each photo
   public View getView(int position, View convertView, ViewGroup parent) {
      // extract the Photo at this position
      Photo photo = getItem(position);
      // check if an existing view is being reused
      if (convertView == null)
         // if not inflate a new View from template
         convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);

      // set up profile picture
      ImageView profilePicture = (ImageView)convertView.findViewById(R.id.profile_picture);
      Picasso.with(getContext()).load(photo.mProfilePictureUrl).transform(new CircleTransform()).into(profilePicture);
      /*
      Transformation transformation = new RoundedTransformationBuilder()
            .borderColor(Color.BLACK).borderWidthDp(3).cornerRadiusDp(30).oval(false).build();
      Picasso.with(getContext()).load(photo.mProfilePictureUrl).fit().transform(transformation).into(profilePicture);
      */
      // set up username
      TextView username = (TextView)convertView.findViewById(R.id.username);
      username.setText(photo.mUsername);
      // set up relative timestamp
      TextView created_time = (TextView)convertView.findViewById(R.id.created_time);
      CharSequence relative_time = DateUtils.getRelativeTimeSpanString(
            photo.mCreatedTime, System.currentTimeMillis() / 1000, DateUtils.SECOND_IN_MILLIS);
      created_time.setText(relative_time);
      // set up likes count
      TextView likes = (TextView)convertView.findViewById(R.id.likes);
      likes.setText(String.format("%,d", photo.mLikesCount) + " likes");
      // set up caption
      TextView caption = (TextView)convertView.findViewById(R.id.caption);
      caption.setText(photo.mCaption);
      // set up central photo
      ImageView image = (ImageView)convertView.findViewById(R.id.photo);
      // clear out the ImageView, since this could be a recycled item
      // calling setImageResource(0) would cause a crash, so call setImageDrawable(null) instead
      image.setImageDrawable(null);
      // insert the image using Picasso
      Picasso.with(getContext()).load(photo.mImageUrl).into(image);
      // return the created item as a view
      return convertView;
   }
}
