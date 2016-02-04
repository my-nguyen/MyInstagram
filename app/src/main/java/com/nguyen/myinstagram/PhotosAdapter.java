package com.nguyen.myinstagram;

import android.content.Context;
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
   // use the item_photo.xml to display each photo
   public View getView(int position, View convertView, ViewGroup parent) {
      // extract the Photo at this position
      Photo photo = getItem(position);
      // check if an existing view is being reused
      if (convertView == null)
         // if not inflate a new View from template
         convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
      // look up the views
      TextView caption = (TextView)convertView.findViewById(R.id.caption_text_view);
      // insert the model data into each of the view items
      caption.setText(photo.mCaption);
      ImageView image = (ImageView)convertView.findViewById(R.id.photo_image_view);
      // clear out the ImageView, since this could be a recycled item
      // calling setImageResource(0) would cause a crash, so call setImageDrawable(null) instead
      image.setImageDrawable(null);
      // insert the image using Picasso
      Picasso.with(getContext()).load(photo.mImageUrl).into(image);
      // return the created item as a view
      return convertView;
   }
}
