package com.github.kevin.library.listener;

public interface IJsonDataListener<T> {

    void onSuccess(T m);

    void onFailure();

}
