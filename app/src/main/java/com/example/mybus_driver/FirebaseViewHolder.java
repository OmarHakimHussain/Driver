package com.example.mybus_driver;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FirebaseViewHolder extends RecyclerView.ViewHolder {

    TextView txt_Cname,txt_Cline;
    ImageView image,imageCall,ic_enter,ic_exit;

    public FirebaseViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_Cname=itemView.findViewById(R.id.txt_Cname);
        txt_Cline=itemView.findViewById(R.id.txt_Cline);
        image=itemView.findViewById(R.id.image_account);
        imageCall=itemView.findViewById(R.id.imageCall);
        ic_enter=itemView.findViewById(R.id.ic_enter);
        ic_exit=itemView.findViewById(R.id.ic_exit);

    }
}
