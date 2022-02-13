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

public class AdapterInvoices extends RecyclerView.Adapter<AdapterInvoices.MyHolder> {
    Context context;
    List<Tickets> ivList;
    int changeBG = 0;

    public AdapterInvoices(Context context, List<Tickets> ivList) {
        this.context = context;
        this.ivList = ivList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_invoices, parent, false);
        return new MyHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        String cusID = ivList.get(position).getID();
        String cusName = ivList.get(position).getName();
        String cusSchoolClass = ivList.get(position).getSchoolClass();
        String cusEmail = ivList.get(position).getEmail();
        String cusTel = ivList.get(position).getTelephone();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(context, Buy_ticket.class);
                // intent.putExtra("key", cusID);
                //context.startActivity(intent);
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
    }

    private void Edit(int position){
        String cusID = ivList.get(position).getID();
        Intent intent = new Intent(context, Buy_ticket.class);
        intent.putExtra("key", cusID);
        context.startActivity(intent);
    }

    private void delete(int position) {
        String stuID = ivList.get(position).getID();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("PointTable");
        Query query = dbref.orderByChild("ID").equalTo(stuID);
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
        return ivList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView cusName, cusID, cusSchoolClass, cusEmail, cusTel;
        public MyHolder(@NonNull View itemView){
            super(itemView);
            cusName = itemView.findViewById(R.id.txtCusName);
            cusID = itemView.findViewById(R.id.txtCusAge);
            cusSchoolClass = itemView.findViewById(R.id.txtCusClass);
            cusEmail = itemView.findViewById(R.id.txtCusEmail);
            cusTel = itemView.findViewById(R.id.txtCusTel);

        }
    }
}
