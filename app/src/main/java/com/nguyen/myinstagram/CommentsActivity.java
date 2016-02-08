package com.nguyen.myinstagram;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by My on 2/5/2016.
 */
public class CommentsActivity extends AppCompatActivity {
   public static final String EXTRA_COMMENTS = "extra_comments";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_comments);

      ListView commentsView = (ListView)findViewById(R.id.comments);
      List<Comment> commentsModel = getIntent().getExtras().getParcelableArrayList(EXTRA_COMMENTS);
      CommentsAdapter adapter = new CommentsAdapter(this, commentsModel);
      commentsView.setAdapter(adapter);
   }

   public static Intent newIntent(Context context, List<Comment> comments) {
      Intent intent = new Intent(context, CommentsActivity.class);
      Bundle bundle = new Bundle();
      bundle.putParcelableArrayList(EXTRA_COMMENTS, (ArrayList<Comment>)comments);
      intent.putExtras(bundle);
      return intent;
   }
}
