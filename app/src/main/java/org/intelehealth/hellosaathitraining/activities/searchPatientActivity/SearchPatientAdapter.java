package org.intelehealth.hellosaathitraining.activities.searchPatientActivity;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import org.intelehealth.hellosaathitraining.R;
import org.intelehealth.hellosaathitraining.models.dto.PatientDTO;
import org.intelehealth.hellosaathitraining.utilities.DateAndTimeUtils;

import org.intelehealth.hellosaathitraining.activities.patientDetailActivity.PatientDetailActivity;

public class SearchPatientAdapter extends RecyclerView.Adapter<SearchPatientAdapter.Myholder> {
    List<PatientDTO> patients;
    Context context;
    LayoutInflater layoutInflater;

    public SearchPatientAdapter(List<PatientDTO> patients, Context context) {
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
    public void onBindViewHolder(@NonNull SearchPatientAdapter.Myholder holder, int position) {
        final PatientDTO patinet = patients.get(position);
        if (patinet != null) {
            //int age = DateAndTimeUtils.getAge(patinet.getDateofbirth(),context);

            String age = DateAndTimeUtils.getAgeInYearMonth(patinet.getDateofbirth(), context);
            //String dob = DateAndTimeUtils.SimpleDatetoLongDate(patinet.getDateofbirth());
            String body = context.getString(R.string.identification_screen_prompt_age) + " " + age + "\n"
                    + context.getString(R.string.mobile_number_search) + patinet.getPhonenumber();

            if (patinet.getOpenmrsId() != null) {
                if(patinet.getLastname()== null || patinet.getLastname().equalsIgnoreCase(" "))
                    holder.headTextView.setText(patinet.getFirstname() + ", " + patinet.getOpenmrsId());
                else
                    holder.headTextView.setText(patinet.getFirstname() + " " + patinet.getLastname()
                        + ", " + patinet.getOpenmrsId());
            }
            else {
                if(patinet.getLastname()== null || patinet.getLastname().equalsIgnoreCase(" "))
                    holder.headTextView.setText(patinet.getFirstname());
                else
                    holder.headTextView.setText(patinet.getFirstname() + " " + patinet.getLastname());
            }

            holder.bodyTextView.setText(body);
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(patinet.getChw_name()!=null && patinet.getChw_name().contains("CC-")) {
                    Log.d("search adapter", "patientuuid" + patinet.getUuid());
                    String patientStatus = "returning";
                    Intent intent = new Intent(context, PatientDetailActivity.class);
                    intent.putExtra("patientUuid", patinet.getUuid());
                    if (patinet.getLastname() == null)
                        intent.putExtra("patientName", patinet.getFirstname());
                    else
                        intent.putExtra("patientName", patinet.getFirstname() + "" + patinet.getLastname());
                    intent.putExtra("status", patientStatus);
                    intent.putExtra("tag", "search");
                    intent.putExtra("hasPrescription", "false");
                    intent.putExtra(PatientDetailActivity.EXTRA_SHOW_MEDICAL_ADVICE, true);
                    context.startActivity(intent);
                }
                else
                {
                    Toast.makeText(context,"This patient cannot be accessed as is created by agent.",Toast.LENGTH_LONG).show();
                }
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

        public Myholder(View itemView) {
            super(itemView);
            headTextView = itemView.findViewById(R.id.list_item_head);
            bodyTextView = itemView.findViewById(R.id.list_item_body);
            linearLayout = itemView.findViewById(R.id.searchlinear);
        }
    }

}