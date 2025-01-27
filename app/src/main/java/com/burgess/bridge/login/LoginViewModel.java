package com.burgess.bridge.login;

import static com.burgess.bridge.Constants.*;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.ServerCallback;
import com.burgess.bridge.SharedPreferencesRepository;
import com.burgess.bridge.apiqueue.BridgeAPIQueue;
import com.burgess.bridge.apiqueue.LoginAPI;

import org.json.JSONException;
import org.json.JSONObject;

import data.Repositories.BuilderRepository;
import data.Repositories.CannedCommentRepository;
import data.Repositories.DirectionRepository;
import data.Repositories.FaultRepository;
import data.Repositories.InspectorRepository;
import data.Repositories.RoomRepository;
import data.Tables.Builder_Table;
import data.Tables.CannedComment_Table;
import data.Tables.Direction_Table;
import data.Tables.Fault_Table;
import data.Tables.Inspector_Table;
import data.Tables.Room_Table;

public class LoginViewModel extends AndroidViewModel {
    private final CannedCommentRepository mCannedCommentRepository;
    private final BuilderRepository mBuilderRepository;
    private final InspectorRepository mInspectorRepository;
    private final RoomRepository mRoomRepository;
    private final DirectionRepository mDirectionRepository;
    private final FaultRepository mFaultRepository;
    private final SharedPreferencesRepository sharedPreferences;
    private final MutableLiveData<String> snackbarMessage = new MutableLiveData<>();
    private final MutableLiveData<String> statusMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> completeLogin = new MutableLiveData<>();

    private static final String TAG = "LOGIN";

    public LoginViewModel(@NonNull Application application) {
        super(application);
        mCannedCommentRepository = new CannedCommentRepository(application);
        mBuilderRepository = new BuilderRepository(application);
        mInspectorRepository = new InspectorRepository(application);
        mRoomRepository = new RoomRepository(application);
        mDirectionRepository = new DirectionRepository(application);
        mFaultRepository = new FaultRepository(application);
        sharedPreferences = new SharedPreferencesRepository(application);
        completeLogin.setValue(false);
    }

    public void setSharedPreferences(JSONObject response, String username, String password) {
        sharedPreferences.setAuthToken(response.optString("AuthorizationToken"));
        sharedPreferences.setSecurityUserId(response.optString("SecurityUserId"));
        sharedPreferences.setInspectorId(response.optString("InspectorId"));
        sharedPreferences.setDivisionId(response.optString("DivisionId"));
        sharedPreferences.setLoginName(username);
        sharedPreferences.setLoginPassword(password);
        sharedPreferences.setIndividualRemaining(0);
        sharedPreferences.setTeamRemaining(0);
        sharedPreferences.setAuthTokenAge(System.currentTimeMillis());
    }

    public LiveData<String> getSnackbarMessage() {
        return snackbarMessage;
    }
    public void showSnackbarMessage(String message) {
        snackbarMessage.setValue(message);
    }

    public LiveData<String> getStatus() {
        return statusMessage;
    }
    public void showStatus(String message) {
        statusMessage.setValue(message);
    }

    public LiveData<Boolean> getCompleteLogin() { return completeLogin; }
    public void setCompleteLogin(boolean value) { completeLogin.setValue(value); }

    //region API Calls
    public void requestLogin(String userName, String password) {
        showStatus("Getting token...");
        JSONObject loginObj = new JSONObject();
        try {
            loginObj.put("username", userName);
            loginObj.put("password", password);
        } catch (JSONException e) {
            BridgeLogger.log('E', TAG, "ERROR in requestLogin: " + e.getMessage());
            showSnackbarMessage("Error in username or password, please check and try again.");
        }
        JsonObjectRequest loginRequest = LoginAPI.loginUser(loginObj, this, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                updateCannedComments(sharedPreferences.getAuthToken());
            }

            @Override
            public void onFailure(String message) { showSnackbarMessage(message); }
        });
        BridgeAPIQueue.getInstance().getRequestQueue().add(loginRequest);
    }
    public void updateCannedComments(String authToken) {
        showStatus("Getting canned comments...");
        JsonArrayRequest cannedCommentsRequest = LoginAPI.getCannedComments(this, authToken, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                updateBuilders(sharedPreferences.getAuthToken());
            }

            @Override
            public void onFailure(String message) { showSnackbarMessage(message); }
        });
        BridgeAPIQueue.getInstance().getRequestQueue().add(cannedCommentsRequest);
    }
    public void updateBuilders(String authToken) {
        showStatus("Getting builders...");
        JsonArrayRequest buildersRequest = LoginAPI.getBuilders(this, authToken, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                updateInspectors(sharedPreferences.getAuthToken());
            }

            @Override
            public void onFailure(String message) { showSnackbarMessage(message); }
        });
        BridgeAPIQueue.getInstance().getRequestQueue().add(buildersRequest);
    }
    public void updateInspectors(String authToken) {
        showStatus("Getting inspectors...");
        JsonArrayRequest inspectorsRequest = LoginAPI.getInspectorsV2(this, authToken, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                updateRooms(sharedPreferences.getAuthToken());
            }
            @Override
            public void onFailure(String message) { showSnackbarMessage(message); }
        });
        BridgeAPIQueue.getInstance().getRequestQueue().add(inspectorsRequest);
    }
    public void updateRooms(String authToken) {
        showStatus("Getting rooms...");
        JsonArrayRequest roomsRequest = LoginAPI.getRooms(this, authToken, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                updateDirections(sharedPreferences.getAuthToken());
            }
            @Override
            public void onFailure(String message) { showSnackbarMessage(message); }
        });
        BridgeAPIQueue.getInstance().getRequestQueue().add(roomsRequest);
    }
    public void updateDirections(String authToken) {
        showStatus("Getting directions...");
        JsonArrayRequest directionsRequest = LoginAPI.getDirections(this, authToken, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                updateFaults(sharedPreferences.getAuthToken());
            }

            @Override
            public void onFailure(String message) {
                showSnackbarMessage(message);
            }
        });
        BridgeAPIQueue.getInstance().getRequestQueue().add(directionsRequest);
    }
    public void updateFaults(String authToken) {
        showStatus("Getting faults...");
        JsonArrayRequest faultsRequest = LoginAPI.getFaults(this, authToken, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                setCompleteLogin(true);
            }
            @Override
            public void onFailure(String message) {
                showSnackbarMessage(message);
            }
        });
        BridgeAPIQueue.getInstance().getRequestQueue().add(faultsRequest);
    }
    //endregion

    //region Database Calls
    public void insertCannedComment(CannedComment_Table cannedComment) {
        mCannedCommentRepository.insert(cannedComment);
    }
    public void insertBuilder(Builder_Table builder) {
        mBuilderRepository.insert(builder);
    }
    public void insertInspector(Inspector_Table inspector) {
        mInspectorRepository.insert(inspector);
    }
    public void insertRoom(Room_Table room) {
        mRoomRepository.insert(room);
    }
    public void insertDirection(Direction_Table direction) {
        mDirectionRepository.insert(direction);
    }
    public void insertFault(Fault_Table fault) {
        mFaultRepository.insert(fault);
    }
    //endregion
}
