package com.rodrigosa.estoqueretrofit.retrofit.callback;

import static com.rodrigosa.estoqueretrofit.retrofit.callback.MessageCallback.MENSSAGE_ERROR_FAIL_COMMUNICATION;
import static com.rodrigosa.estoqueretrofit.retrofit.callback.MessageCallback.MESSAGE_ERROR_RESPONSE_NOT_SUCCESSFUL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class CallbackNoReturn implements Callback<Void> {

    private final ReturnCallback callback;

    public CallbackNoReturn(ReturnCallback callback) {
        this.callback = callback;
    }

    @Override
    @EverythingIsNonNull
    public void onResponse(Call<Void> call, Response<Void> response) {
        if(response.isSuccessful()){
            callback.whenSuccess();
        } else {
            callback.whenFail(MESSAGE_ERROR_RESPONSE_NOT_SUCCESSFUL);
        }
    }

    @Override
    @EverythingIsNonNull
    public void onFailure(Call<Void> call, Throwable t) {
        callback.whenFail(MENSSAGE_ERROR_FAIL_COMMUNICATION + t.getMessage());
    }

    public interface ReturnCallback {
        void whenSuccess();
        void whenFail(String error);
    }

}
