package com.nguyen.myinstagram;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by My on 2/3/2016.
 */
// this class represents one Photo, with all its relevant information
public class Photo {
   public String        mUsername;
   public String        mProfilePictureUrl;
   public String        mCaption;
   public String        mImageUrl;
   public int           mImageHeight;
   public int           mLikesCount;
   public long          mCreatedTime;
   public List<Comment> mComments = new ArrayList<>();

   @Override
   public String toString() {
      return "username: " + mUsername + ", profile url: " + mProfilePictureUrl + ", caption: " + mCaption
            + ", image url: " + mImageUrl + ", height: " + mImageHeight + ", likes: " + mLikesCount
            + ", created at: " + mCreatedTime;
   }
}
