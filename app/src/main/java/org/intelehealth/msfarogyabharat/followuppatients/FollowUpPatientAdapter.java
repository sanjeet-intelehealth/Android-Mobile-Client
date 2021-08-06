package org.intelehealth.msfarogyabharat.followuppatients;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.intelehealth.msfarogyabharat.R;
import org.intelehealth.msfarogyabharat.activities.patientDetailActivity.PatientDetailActivity;
import org.intelehealth.msfarogyabharat.models.dto.PatientDTO;
import org.intelehealth.msfarogyabharat.utilities.DateAndTimeUtils;

import java.util.List;

public class FollowUpPatientAdapter extends RecyclerView.Adapter<FollowUpPatientAdapter.Myholder> {
    List<PatientDTO> patients;
    Context context;
    LayoutInflater layoutInflater;

    public FollowUpPatientAdapter(List<PatientDTO> patients, Context context) {
        this.patients = patients;
        this.context = context;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.list_item_search, parent, false);
        return new Myholder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        final PatientDTO patinet = patients.get(position);
        if (patinet != null) {
            //int age = DateAndTimeUtils.getAge(patinet.getDateofbirth(),context);

            String age = DateAndTimeUtils.getAgeInYearMonth(patinet.getDateofbirth(), context);
            //String dob = DateAndTimeUtils.SimpleDatetoLongDate(patinet.getDateofbirth());
            String body = context.getString(R.string.identification_screen_prompt_age) + " " + age;

            if (patinet.getOpenmrsId() != null)
                holder.headTextView.setText(patinet.getFirstname() + " " + patinet.getLastname()
                        + ", " + patinet.getOpenmrsId());
            else
                holder.headTextView.setText(patinet.getFirstname() + " " + patinet.getLastname());

            holder.bodyTextView.setText(body);
            if (TextUtils.isEmpty(patinet.comment)) {
                holder.commentTextView.setVisibility(View.GONE);
            } else {
                holder.commentTextView.setText(Html.fromHtml(patinet.comment));
                holder.commentTextView.setVisibility(View.VISIBLE);
            }
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("search adapter", "patientuuid" + patinet.getUuid());
                String patientStatus = "returning";
                Intent intent = new Intent(context, PatientDetailActivity.class);
                intent.putExtra("patientUuid", patinet.getUuid());
                intent.putExtra("patientName", patinet.getFirstname() + "" + patinet.getLastname());
                intent.putExtra("status", patientStatus);
                intent.putExtra("tag", "search");
                intent.putExtra("intentTag2","findPatient");
                intent.putExtra("hasPrescription", "false");
                intent.putExtra(PatientDetailActivity.EXTRA_SHOW_MEDICAL_ADVICE, true);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return patients.size();
    }

    class Myholder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        private TextView headTextView;
        private TextView bodyTextView;
        private TextView commentTextView;

        public Myholder(View itemView) {
            super(itemView);
            headTextView = itemView.findViewById(R.id.list_item_head);
            bodyTextView = itemView.findViewById(R.id.list_item_body);
            commentTextView = itemView.findViewById(R.id.list_item_comment);
            linearLayout = itemView.findViewById(R.id.searchlinear);
        }
    }

}