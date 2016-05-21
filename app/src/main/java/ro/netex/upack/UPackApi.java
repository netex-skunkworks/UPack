package ro.netex.upack;

import android.app.DownloadManager;
import android.os.StrictMode;

import com.google.android.gms.appdatasearch.GetRecentContextCall;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;

public class UPackApi {
    private String serverAddress;

    public  UPackApi(String serverAddress){
        this.serverAddress = serverAddress;
    }

    public void call(String route){
        request(serverAddress+route);
    }

    public void request(String url) {
        // send request to API
        // TODO: claudiu implements this
    }
}
