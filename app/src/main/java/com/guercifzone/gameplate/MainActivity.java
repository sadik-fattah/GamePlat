package com.guercifzone.gameplate;


import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.guercifzone.gameplate.Adapters.RecyclerViewAdapter;
import com.guercifzone.gameplate.Models.Feed;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.util.Locale.filter;


public class MainActivity extends AppCompatActivity {

 private final String JSON_URL ="https://raw.githubusercontent.com/sadik-fattah/GamedataBase/main/sitemapgame.json";
     private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private List<Feed> lsArzone;
    RecyclerView recyclerView;
    RecyclerViewAdapter myadapter;
    SearchView searchView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lsArzone = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewtools);
        searchView = (SearchView)findViewById(R.id.searchview);
        searchView.clearFocus();
        Jsonrequest();
        editableSearch();
    }

    private void editableSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });
    }
    private void filterList(String text){
        List<Feed> filteredList = new ArrayList<>();
        for (Feed item : lsArzone){
            if (item.getGamename().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            } else if (item.getGameType().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }

        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "لا يوجد في  قائمة الالعاب", Toast.LENGTH_SHORT).show();
        }else {
            myadapter.setFilteredList(filteredList);
        }
    }
    private void Jsonrequest() {
        request = new JsonArrayRequest(JSON_URL.toString().trim(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray responce) {
                JSONObject jsonObject = null;
                for (int i = 0;i< responce.length();i++){
                    try {
                        jsonObject = responce.getJSONObject(i);
                        Feed arzone = new Feed();
                       arzone.setGamename(jsonObject.getString("name"));
                       arzone.setGameLoc(jsonObject.getString("gameloc"));
                       arzone.setGameType(jsonObject.getString("categorie"));
                        arzone.setGameImage(jsonObject.getString("imagelink"));
                        lsArzone.add(arzone);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                setuprecyclerview(lsArzone);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

            }
        });
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
    }
    private void setuprecyclerview(List<Feed> lsArzone) {
        myadapter = new RecyclerViewAdapter(this,lsArzone);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setAdapter(myadapter);
    }

}