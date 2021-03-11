package com.example.bridge.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bridge.R;
import com.example.bridge.routesheet.RouteSheetActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import data.Tables.DefectItem_Table;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel mLoginViewModel;
    private SharedPreferences mSharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView textUserName = findViewById(R.id.login_text_username);
        TextView textPassword = findViewById(R.id.login_text_password);
        Button buttonLogin = findViewById(R.id.login_button_login);
        mSharedPreferences = getSharedPreferences("Bridge_Preferences", Context.MODE_PRIVATE);

        mLoginViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);

        buttonLogin.setOnClickListener(view -> {
            textPassword.onEditorAction(EditorInfo.IME_ACTION_DONE);
            textUserName.onEditorAction(EditorInfo.IME_ACTION_DONE);
            String userName = textUserName.getText().toString();
            String password = textPassword.getText().toString();

            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest loginRequest = loginUser("https://apistage.burgess-inc.com/api/Bridge/Login?userName="+userName+"&password="+password);
            JsonArrayRequest updateDefectItemsRequest = updateDefectItems("https://apistage.burgess-inc.com/api/Bridge/GetDefectItems");

            queue.add(loginRequest);
            queue.add(updateDefectItemsRequest);
            //queue.stop();
        });
    }

    private JsonObjectRequest loginUser(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("AuthorizationToken", response.getString("AuthorizationToken"));
                editor.putString("SecurityUserId", response.getString("SecurityUserId"));
                editor.putString("InspectorId", response.getString("InspectorId"));
                editor.apply();
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Incorrect Username / Password", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            NetworkResponse response = error.networkResponse;
            if (error instanceof AuthFailureError && response != null) {
                Toast.makeText(getApplicationContext(), "Incorrect Username / Password", Toast.LENGTH_SHORT).show();
            }
        });
        return request;
    }

    private JsonArrayRequest updateDefectItems(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        DefectItem_Table defectItem = new DefectItem_Table();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);

                    defectItem.id = obj.optInt("DefectItemID");
                    defectItem.item_number = obj.optInt("ItemNumber");
                    defectItem.item_description = obj.optString("ItemDescription");
                    defectItem.defect_category_id = obj.optInt("DefectCategoryID");
                    defectItem.category_name = obj.optString("CategoryName");

                    mLoginViewModel.insertDefectItem(defectItem);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error with getting JSON " + e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("TAG", e.getMessage());
                }
            }
            Intent routeSheetIntent = new Intent(LoginActivity.this, RouteSheetActivity.class);
            startActivity(routeSheetIntent);
        }, error -> Toast.makeText(getApplicationContext(), "Shit's fucked, yo " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + mSharedPreferences.getString("AuthorizationToken", "NULL"));
                return params;
            }
        };
        return request;
    }
}