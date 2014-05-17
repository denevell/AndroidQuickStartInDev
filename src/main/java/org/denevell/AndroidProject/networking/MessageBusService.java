package org.denevell.AndroidProject.networking;

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

    public void fetch(String endPoint,
                     Class<ServiceClass> serviceClass,
                     ErrorResponse errorResponse,
                     final GetResult<ReturnResult, ServiceClass> getResult) {
        fetch(endPoint, serviceClass, errorResponse, new GsonConverter(new Gson()), getResult);
    }

    public void fetch(final String endPoint,
                      Class<ServiceClass> serviceClass,
                      final ErrorResponse errorResponse,
                      Converter converter,
                      final GetResult<ReturnResult, ServiceClass> getResult) {

        // Create the Retrofit adapter based on the service class
        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(endPoint)
                .setConverter(converter)
                .build();
        final ServiceClass service = restAdapter.create(serviceClass);

        // Call the service in an async task, sending the success or error to the event bus
        new AsyncTask<Void, Void, ReturnResult>() {
            @Override
            protected ReturnResult doInBackground(Void... params) {
                try {
                    Log.d(TAG, "Attempting to fetch result from base url: " + endPoint);
                    ReturnResult res = getResult.getResult(service);
                    if(res!=null) {
                        Log.d(TAG, "Fetched : " + res.toString() + " from " + endPoint);
                    }
                    return res;
                } catch (RetrofitError e) {
                    errorResponse.fill(e.getResponse().getStatus(),
                                       e.getResponse().getReason(),
                                       e.getResponse().getUrl(),
                                       e.isNetworkError());
                    return null;
                } catch(Exception e1) {
                    Log.e(TAG, "Unknown error", e1);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ReturnResult res) {
                if(res!=null) {
                    super.onPostExecute(res);
                    Application.getEventBus().post(res);
                }  else if(res==null && errorResponse!=null) {
                    Application.getEventBus().post(errorResponse);
                }
            }
        }.execute();
    }
}
