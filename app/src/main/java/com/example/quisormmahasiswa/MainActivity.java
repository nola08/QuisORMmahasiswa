package com.example.quisormmahasiswa;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etNama, etNim, etAlamat, etAsalSekolah;
    private Button btnTambah;
    private TextView tvOutput;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNama = findViewById(R.id.etNama);
        etNim = findViewById(R.id.etNim);
        etAlamat = findViewById(R.id.etAlamat);
        etAsalSekolah = findViewById(R.id.etAsalSekolah);
        btnTambah = findViewById(R.id.btnTambah);
        tvOutput = findViewById(R.id.tvOutput);

        database = AppDatabase.getInstance(this);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mahasiswa mahasiswa = new Mahasiswa();
                mahasiswa.setNama(etNama.getText().toString());
                mahasiswa.setNim(etNim.getText().toString());
                mahasiswa.setAlamat(etAlamat.getText().toString());
                mahasiswa.setAsalSekolah(etAsalSekolah.getText().toString());

                new Thread(() -> {
                    // Insert data in background
                    database.mahasiswaDao().insert(mahasiswa);
                    // Load data on UI thread after insertion
                    runOnUiThread(() -> loadData());
                }).start();
            }
        });

        // Load data when the activity starts
        loadData();
    }

    private void loadData() {
        new Thread(() -> {
            List<Mahasiswa> list = database.mahasiswaDao().getAllMahasiswa();
            StringBuilder output = new StringBuilder();
            for (Mahasiswa m : list) {
                output.append(m.getNama()).append(" - ").append(m.getNim()).append("-").append(m.getAlamat()).append("-").append(m.getAsalSekolah()).append(" \n");
            }
            // Update UI on the main thread
            runOnUiThread(() -> tvOutput.setText(output.toString()));
        }).start();
    }
}