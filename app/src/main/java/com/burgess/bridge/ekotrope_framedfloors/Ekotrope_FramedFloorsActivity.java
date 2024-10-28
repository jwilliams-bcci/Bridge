package com.burgess.bridge.ekotrope_framedfloors;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.burgess.bridge.R;

public class Ekotrope_FramedFloorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_framed_floors);
        setSupportActionBar(findViewById(R.id.framed_floors_toolbar));
    }
}