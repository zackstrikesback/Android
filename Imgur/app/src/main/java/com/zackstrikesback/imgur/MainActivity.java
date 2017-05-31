package com.zackstrikesback.imgur;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.squareup.picasso.Picasso;
import com.zackstrikesback.imgur.MyAppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zackstrikesback.imgur.ImgurAuthorization;
import com.zackstrikesback.imgur.LoginActivity;
import com.zackstrikesback.imgur.RefreshAccessTokenTask;

public class MainActivity extends ChooseImageActivity {

    private OkHttpClient httpClient;
    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.uxMyToolbar);
//        setSupportActionBar(myToolbar);
        ButterKnife.bind(this);
        fetchData();
        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/gallery/user/0.json")
                .header("Authorization","Client-ID 9df18c0b96fcb0d")
                .header("User-Agent","androdimgur")
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "An error has occurred " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final List<Photo> photos = new ArrayList<Photo>();
                try{
                    JSONObject data = new JSONObject(response.body().string());
                    JSONArray items = data.getJSONArray("data");
                    for(int i=0; i<items.length();i++) {
                        JSONObject item = items.getJSONObject(i);
                        Photo photo = new Photo();
                        if(item.getBoolean("is_album")) {
                            photo.id = item.getString("cover");
                        } else {
                            photo.id = item.getString("id");
                        }
                        photo.title = item.getString("title");

                        photos.add(photo); // Add photo to list
                    }
                }catch (JSONException ignored) {
                    ignored.printStackTrace(); //now it is in log
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        render(photos);
                    }
                });
            }
        });
    }

    private void render(final List<Photo> photos) {
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv_of_photos);
        rv.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter<PhotoVH> adapter = new RecyclerView.Adapter<PhotoVH>() {
            @Override
            public PhotoVH onCreateViewHolder(ViewGroup parent, int viewType) {
                PhotoVH vh = new PhotoVH(getLayoutInflater().inflate(R.layout.item, null));
                vh.photo = (ImageView) vh.itemView.findViewById(R.id.photo);
                vh.title = (TextView) vh.itemView.findViewById(R.id.title);
                return vh;
            }

            @Override
            public void onBindViewHolder(PhotoVH holder, int position) {
                Picasso.with(MainActivity.this).load("https://i.imgur.com/" +
                        photos.get(position).id + ".jpg").into(holder.photo);
                holder.title.setText(photos.get(position).title);
            }

            @Override
            public int getItemCount() {
                return photos.size();
            }
        };
        rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 16; // Gap of 16px
            }
        });
        rv.setAdapter(adapter);
    }
    private void fetchData() {
        httpClient = new OkHttpClient.Builder().build();
    }
    private static class Photo {
        String id;
        String title;
    }
    private static class PhotoVH extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView title;

        public PhotoVH(View itemView) {
            super(itemView);
        }
    }
}
