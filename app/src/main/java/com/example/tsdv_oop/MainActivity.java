package com.example.tsdv_oop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterMovies adapterMovies;
    List<Movies> mvList;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Movies");
        BottomNavigationView navView = findViewById(R.id.nav);
        navView.setOnNavigationItemSelectedListener(selectedListener);
        recyclerView = findViewById(R.id.listMovies);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mvList = new ArrayList<>();
        getAllMovies();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.nav_mv:
                    actionBar.setTitle("Check Exam");
                    return true;

                case R.id.nav_invoices:
                    actionBar.setTitle("Point Table");
                    startActivity(new Intent(MainActivity.this, Invoices.class));
                    return true;
                case R.id.nav_cus:
                    actionBar.setTitle("Register Experiment");
                    startActivity(new Intent(MainActivity.this, Customers.class));
                    return true;
            }
            return false;
        }
    };
    private void getAllMovies() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CheckExam");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mvList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    Movies movies = ds.getValue(Movies.class);
                    mvList.add(movies);
                    adapterMovies = new AdapterMovies(MainActivity.this, mvList);
                    recyclerView.setAdapter(adapterMovies);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.actionSearch);
        if(item.getItemId() == R.id.actionSearch){
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if(!TextUtils.isEmpty(query.trim())){
                        searchMovie(query);
                    }
                    else {
                        getAllMovies();
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(!TextUtils.isEmpty(newText.trim())){
                        searchMovie(newText);
                    }
                    else {
                        getAllMovies();
                    }
                    return false;
                }
            });
        }
        return true;
    }

    private void searchMovie(String query) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CheckExam");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mvList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    Movies movies = ds.getValue(Movies.class);
                        if(movies.getName().toLowerCase().contains(query.toLowerCase()) ||
                                movies.getID().toLowerCase().contains(query.toLowerCase())){

                            mvList.add(movies);
                        }
                    adapterMovies = new AdapterMovies(MainActivity.this, mvList);
                    adapterMovies.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterMovies);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}