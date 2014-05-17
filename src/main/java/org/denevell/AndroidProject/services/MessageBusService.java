package org.denevell.AndroidProject.services;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.denevell.AndroidProject.Application;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

/**
 * Perform a call on a RetroFit service and sends the result or error to the event bus.
 */
public class MessageBusService<ReturnResult, ServiceClass> {

    private static final String TAG = MessageBusService.class.getSimpleName();

    public static abstract class GetResult<ReturnResult, ServiceClass>  {
        public abstract ReturnResult getResult(ServiceClass mService);
    }

    public static class ReturnResultAndOptionalError<ReturnResult> {
        public ReturnResult returnResult;
        public ErrorResponse errorResponse;

        private ReturnResultAndOptionalError(ReturnResult returnResult, ErrorResponse errorResponse) {
            this.returnResult = returnResult;
            this.errorResponse = errorResponse;
        }
    }

    public void fetch(String endPoint,
                     Class<ServiceClass> serviceClass,
                     ErrorResponse errorResponse,
                     final GetResult<ReturnResult, ServiceClass> getResult) {
        fetch(endPoint, serviceClass, errorResponse, new GsonConverter(new Gson()), getResult);
    }

    public void fetch(String endPoint,
                      Class<ServiceClass> serviceClass,
                      final ErrorResponse errorResponse,
                      Converter converter,
                      final GetResult<ReturnResult, ServiceClass> getResult) {

        // Create the Retrofit adapter based on the service class
        RestAdapter mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(endPoint)
                .setConverter(converter)
                .build();
        final ServiceClass service = mRestAdapter.create(serviceClass);

        // Call the service in an async task, sending the success or error to the event bus
        new AsyncTask<Void, Void, ReturnResultAndOptionalError>() {
            @Override
            protected ReturnResultAndOptionalError doInBackground(Void... params) {
                try {
                    ReturnResult res = getResult.getResult(service);
                    return new ReturnResultAndOptionalError(res, null);
                } catch (RetrofitError e) {
                    errorResponse.fill(e.getResponse().getStatus(),
                                       e.getResponse().getReason(),
                                       e.getResponse().getUrl());
                    return new ReturnResultAndOptionalError(null, errorResponse);
                } catch(Exception e1) {
                    Log.e(TAG, "Unknown error", e1);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ReturnResultAndOptionalError res) {
                if(res!=null && res.returnResult!=null) {
                    super.onPostExecute(res);
                    Application.getEventBus().post(res.returnResult);
                }  else if(res!=null && res.errorResponse!=null) {
                    Application.getEventBus().post(res.errorResponse);
                }
            }
        }.execute();
    }
}
