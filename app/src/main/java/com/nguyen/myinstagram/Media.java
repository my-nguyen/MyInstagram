package com.nguyen.myinstagram;

/**
 * Created by My on 2/8/2016.
 */
public class Media {
   public String  mUrl;
   public int     mWidth;
   public int     mHeight;

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("width: ").append(mWidth).append(", height: ").append(mHeight).append(", url: ").append(mUrl);
      return builder.toString();
   }
}
