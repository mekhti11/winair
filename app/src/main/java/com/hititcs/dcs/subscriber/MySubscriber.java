package com.hititcs.dcs.subscriber;

public interface MySubscriber<T> {

  void onResponse(T data);

}
