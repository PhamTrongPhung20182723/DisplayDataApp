package com.example.tsdv_oop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Invoices extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterInvoices adapterInvoices;
    List<Tickets> ivList;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Point Table");
        BottomNavigationView navView = findViewById(R.id.nav);
        navView.setOnNavigationItemSelectedListener(selectedListener);
        recyclerView = findViewById(R.id.listInvoices);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Invoices.this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        ivList = new ArrayList<>();
        getAllInvoices();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.nav_mv:
                    actionBar.setTitle("Check Exam");
                    startActivity(new Intent(Invoices.this, MainActivity.class));
                    return true;

                case R.id.nav_invoices:
                    actionBar.setTitle("Point Table");
                    return true;
                case R.id.nav_cus:
                    actionBar.setTitle("Register Experiment");
                    startActivity(new Intent(Invoices.this, Customers.class));
                    return true;
            }
            return false;
        }
    };
    private void getAllInvoices() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("PointTable");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ivList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    Tickets tickets = ds.getValue(Tickets.class);
                    ivList.add(tickets);
                    adapterInvoices = new AdapterInvoices(Invoices.this, ivList);
                    recyclerView.setAdapter(adapterInvoices);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Invoices.this, "Error", Toast.LENGTH_SHORT).show();
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
                        getAllInvoices();
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(!TextUtils.isEmpty(newText.trim())){
                        searchMovie(newText);
                    }
                    else {
                        getAllInvoices();
                    }
                    return false;
                }
            });
        }
        return true;
    }

    private void searchMovie(String query) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("PointTable");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ivList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    Tickets movies = ds.getValue(Tickets.class);
                    if(movies.getName().toLowerCase().contains(query.toLowerCase()) ||
                            movies.getID().toLowerCase().contains(query.toLowerCase())){

                        ivList.add(movies);
                    }
                    adapterInvoices = new AdapterInvoices(Invoices.this, ivList);
                    adapterInvoices.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterInvoices);
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