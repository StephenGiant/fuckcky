package com.nsk.app.utils;


import com.blankj.utilcode.util.LogUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nsk.app.config.CkyApplication;
import com.nsk.cky.ckylibrary.http.CkyException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class RxjavaUtils<T> {
  public static  <T> ObservableTransformer<T, T> transformer() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
    public static <T> ObservableTransformer<T,T> handleResult(){

        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return  observable.flatMap(new Function<T, ObservableSource<? extends T>>() {
                    @Override
                    public ObservableSource<? extends T> apply(T t) throws Exception {
                        return createData(t);
                    }
                });
            }
        };
    }


    private static<T> ObservableSource createData(final T t){
     return Observable.create(new ObservableOnSubscribe<Object>() {

          @Override
          public void subscribe(ObservableEmitter<Object> e) throws Exception {
              try {
                  if(t instanceof String){
                      JsonParser jsonParser = new JsonParser();
                      JsonObject json = jsonParser.parse((String) t).getAsJsonObject() ;
                      if(!json.get("hasError").getAsBoolean()){
                          e.onNext(json.get("data").toString());
                          e.onComplete();
                      }else {
                          CkyException ckyException = new CkyException(json.get("errorMessage").getAsString(),
                                 json.get("errorCode") .getAsString());
                          e.onError(ckyException);
                          e.onComplete();
                      }
                  }else if(t instanceof JsonObject){
                      if(!((JsonObject)t).get("hasError").getAsBoolean()&&((JsonObject)t).get("data")!=null){
                          e.onNext(((JsonObject)t).get("data"));
                          e.onComplete();
                      }else {
                          CkyException ckyException = new CkyException(((JsonObject)t).get("errorMessage").getAsString(),
                                  ((JsonObject)t).get("errorCode") .getAsString());
                          e.onError(ckyException);
                          e.onComplete();
                      }
                  }
                  else {
                      e.onNext(t);
                      e.onComplete();
                  }
              }catch (Exception e1){
                  e.onError(e1);
                  e.onComplete();
              }
          }
      });
    }

    public abstract static class CkySuccessConsumer implements Consumer<JsonElement>{
        @Override
        public void accept(JsonElement jsonElement) throws Exception {
        onSuccess(jsonElement);
        }
        public abstract void onSuccess(JsonElement jsonElement);
    }

    public abstract static class CkyErrorConsumer implements Consumer<Throwable> {
        @Override
        public void accept(Throwable e) {
//            onCkyError(e.getErrorcode(),e.getErrormessage());
//            if(e.getErrorcode().equals("400")){
//                //撤销登录
//            }
            if(e instanceof HttpException){
                onNetError(((HttpException) e).code()+"",((HttpException) e).message());
            }else if(e instanceof CkyException){
                onCkyError(((CkyException) e).getErrorcode(),((CkyException) e).getErrormessage());
            }else {
//                ToastUtils.showShort(e.getMessage());
                //其他的java级别的错误另行处理
                onError(e);
            }
        }

        public  void onNetError(String code,String message){

        };

        public void onCkyError(String code,String message){
            //判断是否是token失效
            if(code.equals("100")){
                //去除token,去掉过期token
                CkyApplication.Companion.getApp().cleaToken();
            }
        }

        public void onError(Throwable e){
            LogUtils.e(e.getMessage());
        }
    }
}
