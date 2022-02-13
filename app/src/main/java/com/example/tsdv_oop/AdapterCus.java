package com.example.tsdv_oop;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class AdapterCus extends RecyclerView.Adapter<AdapterCus.MyHolder> {
    Context context;
    List<Customer> cusList;
    int changeBG = 0;

    public AdapterCus(Context context, List<Customer> cusList) {
        this.context = context;
        this.cusList = cusList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_cus, parent, false);
        return new MyHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        String cusID = cusList.get(position).getId();
        String cusName = cusList.get(position).getName();
        String cusSchoolClass = cusList.get(position).getSchoolClass();
        String cusEmail = cusList.get(position).getEmail();
        String cusTel = cusList.get(position).getTelephone();
        String cusSub = cusList.get(position).getSubject();
        String cusCurClass = cusList.get(position).getCurClass();
        String cusExpClass = cusList.get(position).getExpClass();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(changeBG == 0){
                    holder.itemView.setBackgroundColor(Color.GRAY);
                    changeBG = 1;
                }
                else{
                    holder.itemView.setBackgroundColor(Color.WHITE);
                    changeBG = 0;
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete or edit information");
                builder.setMessage("Do you want to delete or edit this information?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(position);
                    }
                });
                builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Edit(position);
                    }
                });
                builder.create().show();
                return false;
            }
        });
        holder.cusName.setText(cusName);
        holder.cusID.setText(cusID);
        holder.cusSchoolClass.setText(cusSchoolClass);
        holder.cusEmail.setText(cusEmail);
        holder.cusTel.setText(cusTel);
        holder.cusSub.setText("Subject: "+cusSub);
        holder.cusCurClass.setText("-  "+cusCurClass);
        holder.cusExpClass.setText("Expected class: "+cusExpClass);
    }

    private void Edit(int position){
        String cusID = cusList.get(position).getId();
        Intent intent = new Intent(context, Buy_ticket.class);
        intent.putExtra("key", cusID);
        context.startActivity(intent);
    }

    private void delete(int position) {
        String cusID = cusList.get(position).getId();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("RegisExpClass");
        Query query = dbref.orderByChild("ID").equalTo(cusID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    ds.getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return cusList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView cusName, cusID, cusSchoolClass, cusEmail, cusTel, cusSub, cusCurClass, cusExpClass;

        public MyHolder(@NonNull View itemView){
            super(itemView);
            cusName = itemView.findViewById(R.id.txtCusName);
            cusID = itemView.findViewById(R.id.txtCusAge);
            cusSchoolClass = itemView.findViewById(R.id.txtCusClass);
            cusEmail = itemView.findViewById(R.id.txtCusEmail);
            cusTel = itemView.findViewById(R.id.txtCusTel);
            cusSub = itemView.findViewById(R.id.txtCusSub);
            cusCurClass = itemView.findViewById(R.id.txtCusCurClass);
            cusExpClass = itemView.findViewById(R.id.txtCusExpClass);
        }
    }
}
