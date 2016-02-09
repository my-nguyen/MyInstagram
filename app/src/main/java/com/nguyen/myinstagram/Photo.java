package com.nguyen.myinstagram;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by My on 2/3/2016.
 */
// this class represents one Photo, with all its relevant information
public class Photo {
   public static final int IMAGE_VIEW = 0;
   public static final int VIDEO_VIEW = 1;

   public String        mUsername;
   public String        mProfilePictureUrl;
   public String        mCaption;
   public int           mMediaType;
   public Media         mMedia;
   public int           mLikesCount;
   public long          mCreatedTime;
   public List<Comment> mComments = new ArrayList<>();

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("username: ").append(mUsername).append(", profile url: ").append(mProfilePictureUrl)
            .append(", caption: ").append(mCaption).append(", media: ").append(mMedia)
            .append(", likes: ").append(mLikesCount).append(", created at: ").append(mCreatedTime);
      return builder.toString();
   }
}
