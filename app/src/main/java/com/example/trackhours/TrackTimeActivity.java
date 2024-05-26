package com.example.trackhours;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TrackTimeActivity extends AppCompatActivity {

    private Button btnPunchIn, btnPunchOut, btnBack;
    private TableLayout currentEntriesTable, oldEntriesTable;

    private boolean isPunchedIn = false;
    private String punchInTime = "";
    private String punchInKey = "";


    private DatabaseReference timeEntriesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_time);

        btnPunchIn = findViewById(R.id.btn_punch_in);
        btnPunchOut = findViewById(R.id.btn_punch_out);
        btnBack = findViewById(R.id.btn_back);
        currentEntriesTable = findViewById(R.id.current_entries_table);
        oldEntriesTable = findViewById(R.id.old_entries_table);
        String username = getIntent().getStringExtra("username");

        TextView usernameTextView = findViewById(R.id.text_username);
        usernameTextView.setText("Username: " + username);


        timeEntriesRef = FirebaseDatabase.getInstance().getReference("trackhours").child(username);

        readExistingEntries();

        if (isPunchedIn) {
            btnPunchIn.setEnabled(false);
        }

        btnPunchIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentDate = getCurrentDate();
                String currentTime = getCurrentTime();
                if (!isPunchedIn) {
                    punchInTime = currentTime;
                    punchInKey = saveTimeEntry(username, punchInTime, "", "", true);
                    addTableRow(currentEntriesTable, currentDate, currentTime, "", "");
                    isPunchedIn = true;
                    btnPunchIn.setEnabled(false);
                }
            }
        });

        btnPunchOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPunchedIn) {
                    String currentTime = getCurrentTime();
                    String totalTime = calculateTotalTime(punchInTime, currentTime);
                    updatePunchOutTime(username, punchInKey, currentTime, totalTime);
                    updateTableRow(currentEntriesTable, punchInTime, currentTime, totalTime);
                    isPunchedIn = false;
                    btnPunchIn.setEnabled(true);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addTableRow(TableLayout table, String date, String inTime, String outTime, String totalTime) {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);

        TextView tvDate = createTextView(date);
        TextView tvInTime = createTextView(inTime.isEmpty() ? "-" : inTime);
        TextView tvOutTime = createTextView(outTime.isEmpty() ? "-" : outTime);
        TextView tvTotalTime = createTextView(totalTime.isEmpty() ? "-" : totalTime);

        row.addView(tvDate);
        row.addView(tvInTime);
        row.addView(tvOutTime);
        row.addView(tvTotalTime);

        table.addView(row);
    }

    private void updateTableRow(TableLayout table, String inTime, String outTime, String totalTime) {
        TableRow row = (TableRow) table.getChildAt(table.getChildCount() - 1);

        TextView tvInTime = (TextView) row.getChildAt(1);
        TextView tvOutTime = (TextView) row.getChildAt(2);
        TextView tvTotalTime = (TextView) row.getChildAt(3);

        if (tvOutTime.getText().toString().equals("-")) {
            tvOutTime.setText(outTime.isEmpty() ? "-" : outTime);
            tvTotalTime.setText(totalTime.isEmpty() ? "-" : totalTime);
        }
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        return textView;
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private String calculateTotalTime(String punchInTime, String punchOutTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        try {
            Date startDate = sdf.parse(punchInTime);
            Date endDate = sdf.parse(punchOutTime);
            long diff = endDate.getTime() - startDate.getTime();

            long hours = diff / (60 * 60 * 1000);
            long minutes = (diff / (60 * 1000)) % 60;

            return hours + "h "+ minutes + "min";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String saveTimeEntry(String username, String punchInTime, String punchOutTime, String totalTime, boolean isPunchIn) {
        TimeEntry timeEntry = new TimeEntry(getCurrentDate(), punchInTime, punchOutTime, totalTime, username);

        if (isPunchIn) {
            String id = timeEntriesRef.push().getKey();
            timeEntriesRef.child(id).setValue(timeEntry);
            return id;
        } else {
            timeEntriesRef.child(punchInTime.replace(" ", "_")).setValue(timeEntry);
            return null;
        }
    }

    private void updatePunchOutTime(String username, String key, String punchOutTime, String totalTime) {
        DatabaseReference entryRef = timeEntriesRef.child(key);
        entryRef.child("punchOutTime").setValue(punchOutTime);
        entryRef.child("totalTime").setValue(totalTime);
    }

    private void readExistingEntries() {
        timeEntriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot entrySnapshot : dataSnapshot.getChildren()) {
                    TimeEntry entry = entrySnapshot.getValue(TimeEntry.class);
                    if (entry != null) {

                        String date = entry.getDate();
                        String inTime = entry.getPunchInTime();
                        String outTime = entry.getPunchOutTime();
                        String totalTime = entry.getTotalTime();


                        if (outTime.isEmpty()) {
                            addTableRow(currentEntriesTable, date, inTime, outTime, totalTime);
                        } else {
                            addTableRow(oldEntriesTable, date, inTime, outTime, totalTime);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}

