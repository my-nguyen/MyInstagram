package com.nguyen.myinstagram;

import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;

/**
 * Created by My on 2/6/2016.
 */
public class Utils {
   // this method returns an abbreviated "time ago" string in the format of 20m, 2h, etc.
   public static String timeAgo(long timeCreated) {
      // get the "time ago" (e.g. 20 minutes ago, 2 hours ago) string
      CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(
            timeCreated * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
      // split string into tokens
      String[] tokens = relativeTime.toString().split(" ");
      // return number appended with "m" or "h", e.g. 20m, 2h
      return tokens[0] + tokens[1].charAt(0);
   }

   // this method returns a string with username in bold blue and text that has all the @-tags and
   // #-tags in non-bold blue
   public static Spanned htmlText(String username, String text) {
      // set username at the beginning
      StringBuilder builder = new StringBuilder();
      builder.append(formatUsername(username)).append(formatText(text));
      return Html.fromHtml(builder.toString());
   }

   // this method returns a text that has all the @-tags and #-tags in non-bold blue
   public static Spanned htmlText(String text) {
      return Html.fromHtml(formatText(text));
   }

   // this method returns a username in bold blue
   public static Spanned htmlUsername(String username) {
      return Html.fromHtml(formatUsername(username));
   }

   // this method returns a username (in html format) that is turned bold blue
   private static String formatUsername(String username) {
      StringBuilder builder = new StringBuilder();
      builder.append("<b>").append(formatBlue(username)).append("</b>");
      return builder.toString();
   }

   // this method returns a text (in html format) that has all the @-tags and #-tags in non-bold blue
   private static String formatText(String text) {
      StringBuilder builder = new StringBuilder();
      String[] tokens = text.split(" ");
      for (String token : tokens) {
         builder.append(" ");
         if (token.charAt(0) == '#')
            builder.append(formatBlue(token));
         else if (token.charAt(0) == '@')
            builder.append(formatBlue(token));
         else
            builder.append(token);
      }
      return builder.toString();
   }

   // this method returns a string turned blue in html format
   private static String formatBlue(String string) {
      StringBuilder builder = new StringBuilder();
      builder.append("<font color='#000080'>").append(string).append("</font>");
      return builder.toString();
   }
}
