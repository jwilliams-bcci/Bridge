package com.burgess.bridge;

public interface ServerCallback {
    void onSuccess(String message);
    void onFailure(String message);
}
