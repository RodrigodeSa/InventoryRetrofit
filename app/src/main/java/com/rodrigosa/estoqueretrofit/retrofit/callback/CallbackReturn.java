package com.rodrigosa.estoqueretrofit.retrofit.callback;

import static com.rodrigosa.estoqueretrofit.retrofit.callback.MessageCallback.MENSSAGE_ERROR_FAIL_COMMUNICATION;
import static com.rodrigosa.estoqueretrofit.retrofit.callback.MessageCallback.MESSAGE_ERROR_RESPONSE_NOT_SUCCESSFUL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class CallbackReturn<T> implements Callback<T> {

    private final ReturnCallback<T> callback;

    public CallbackReturn(ReturnCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    @EverythingIsNonNull
    public void onResponse(Call<T> call, Response<T> response) {
        if(response.isSuccessful()){
            T result = response.body();
            if(result != null){
                callback.whenSuccess(result);
            }
        } else {
            callback.whenFail(MESSAGE_ERROR_RESPONSE_NOT_SUCCESSFUL);
        }
    }

    @Override
    @EverythingIsNonNull
    public void onFailure(Call<T> call, Throwable t) {
        callback.whenFail(MENSSAGE_ERROR_FAIL_COMMUNICATION + t.getMessage());
    }

    public interface ReturnCallback<T> {
        void whenSuccess(T result);
        void whenFail(String error);
    }
}
