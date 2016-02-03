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
public class Adapter extends ArrayAdapter<Photo> {
   public Adapter(Context context, List<Photo> objects) {
      super(context, android.R.layout.simple_list_item_1, objects);
   }

   @Override
   // use the item_photo.xml to display each photo
   public View getView(int position, View convertView, ViewGroup parent) {
      // get the data item for this position
      Photo photo = getItem(position);
      // check if we're using the RecyclerView
      if (convertView == null)
         // if not create a new View from template
         convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
      // look up the views for populating the data (image, caption)
      TextView caption = (TextView)convertView.findViewById(R.id.tvCaption);
      // insert the model data into each of the view items
      caption.setText(photo.caption);
      ImageView image = (ImageView)convertView.findViewById(R.id.ivPhoto);
      // clear out the ImageView, since this could be a recycled item
      // calling setImageResource(0) would cause a crash
      // image.setImageResource(0);
      // so call setImageDrawable(null) instead
      image.setImageDrawable(null);
      // insert the image using Picasso
      Picasso.with(getContext()).load(photo.imageUrl).into(image);
      // return super.getView() was generated
      // return super.getView(position, convertView, parent);
      // return the created item as a view
      return convertView;
   }
}
