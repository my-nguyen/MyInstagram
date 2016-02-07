package com.nguyen.myinstagram;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by My on 2/5/2016.
 */
// this class represents one comment by one user. it implements Parcelable so that it can be passed
// in a List from one Activity to another.
public class Comment implements Parcelable {
   public String  mProfilePictureUrl;
   public String  mUsername;
   public String  mText;
   public long    mCreatedTime;

   public Comment() {
   }

   @Override
   public String toString() {
      return "profile URL: " + mProfilePictureUrl + ", username: " + mUsername + ", text: " + mText + ", created at: " + mCreatedTime;
   }

   @Override
   public int describeContents() {
      return 0;
   }

   @Override
   public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(mProfilePictureUrl);
      dest.writeString(mUsername);
      dest.writeString(mText);
      dest.writeLong(mCreatedTime);
   }

   public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
      @Override
      public Comment createFromParcel(Parcel source) {
         return new Comment(source);
      }
      @Override
      public Comment[] newArray(int size) {
         return new Comment[size];
      }
   };

   public Comment(Parcel source) {
      mProfilePictureUrl = source.readString();
      mUsername = source.readString();
      mText = source.readString();
      mCreatedTime = source.readLong();
   }
}
