package com.example.rakesh.appointment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

public class AppointmentDataAdapter extends RecyclerView.Adapter<AppointmentDataAdapter.ViewHolder> {

    private Activity context;
    private List<AppointmentDataModel> data;

    AppointmentDataAdapter(Activity c, List<AppointmentDataModel> l) {
        context = c;
        data = l;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.appointment_data_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AppointmentDataModel mdata = data.get(position);

        holder.tvName.setText((position + 1) + ". " + mdata.getName());
        holder.tvDesignation.setText(mdata.getDesignation());
        holder.tvVenue.setText(mdata.getVenue());
        holder.tvTime.setText(mdata.getAppoint_time());

        if(mdata.getAppointee_type().equalsIgnoreCase("Internal")){

            ColorGenerator generator = ColorGenerator.MATERIAL;
            // generate random color
            int color = generator.getColor("Internal");

            TextDrawable drawable = TextDrawable.builder()
                    .buildRound("I", color); // radius in px

            holder.imgPerson.setImageDrawable(drawable);
        }else{
            ColorGenerator generator = ColorGenerator.MATERIAL;
            // generate random color
            int color = generator.getColor("External");

            TextDrawable drawable = TextDrawable.builder()
                    .buildRound("E", color); // radius in px

            holder.imgPerson.setImageDrawable(drawable);
        }

        holder.tvStatus.setText(mdata.getStatusName());


        switch (mdata.getAppointment_status()) {
            case 1:
               // holder.tvStatus.setText("Active");
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.active));
                break;
            case 2:
               // holder.tvStatus.setText("Completed");
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.completed));
                break;
            case 3:
               // holder.tvStatus.setText("Cancelled");
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.canceled));
                break;
            case 4:
               // holder.tvStatus.setText("Rescheduled");
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.rescheduled));
                break;
        }

        holder.tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SENDTO);
                emailIntent.setType("message/rfc822");
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[]{mdata.getEmail()});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        "Regarding Appointment");
                context.startActivity(Intent.createChooser(emailIntent, "Regarding Appointment"));
            }
        });

        holder.tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + mdata.getMobile_no()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.CALL_PHONE},1);
                    return;
                }
                context.startActivity(intent);

            }
        });

        holder.tvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("sms:"+mdata.getMobile_no()));
                context.startActivity(sendIntent);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AppointmentDetails.class);
                intent.putExtra("particular_appointment",data.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

       ImageView imgPerson;
       TextView tvName, tvDesignation,tvVenue,tvTime,tvStatus,tvMessage,tvEmail,tvCall;
       CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);

            imgPerson = itemView.findViewById(R.id.imgPerson);
            tvName = itemView.findViewById(R.id.tvName);
            tvDesignation = itemView.findViewById(R.id.tvDesignation);
            tvVenue = itemView.findViewById(R.id.tvVenue);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvCall = itemView.findViewById(R.id.tvCall);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
