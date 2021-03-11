package com.example.bridge;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bridge.routesheet.RouteSheetActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView textUserName = findViewById(R.id.login_text_username);
        TextView textPassword = findViewById(R.id.login_text_password);
        Button buttonLogin = findViewById(R.id.login_button_login);
        SharedPreferences sharedPreferences = getSharedPreferences("Bridge_Preferences", Context.MODE_PRIVATE);

        buttonLogin.setOnClickListener(view -> {
            textPassword.onEditorAction(EditorInfo.IME_ACTION_DONE);
            textUserName.onEditorAction(EditorInfo.IME_ACTION_DONE);
            String userName = textUserName.getText().toString();
            String password = textPassword.getText().toString();

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://apistage.burgess-inc.com/api/Bridge/Login?userName="+userName+"&password=" + password;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                try {
                    Toast.makeText(getApplicationContext(), response.getString("AuthorizationToken"), Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("AuthorizationToken", response.getString("AuthorizationToken"));
                    editor.putString("SecurityUserId", response.getString("SecurityUserId"));
                    editor.putString("InspectorId", response.getString("InspectorId"));
                    editor.apply();
                    Intent routeSheetIntent = new Intent(LoginActivity.this, RouteSheetActivity.class);
                    startActivity(routeSheetIntent);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Incorrect Username / Password", Toast.LENGTH_SHORT).show();
                }
            }, error -> {
                NetworkResponse response = error.networkResponse;
                if (error instanceof AuthFailureError && response != null) {
                    Toast.makeText(getApplicationContext(), "Incorrect Username / Password", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(request);
        });
    }
}