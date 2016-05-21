package ro.netex.upack;

import android.app.DownloadManager;
import android.os.StrictMode;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;

/**
 * Created by user on 5/20/16.
 */
public class UPackApi {
    private String serverAddress;

    public  UPackApi(String serverAddress){
        this.serverAddress = serverAddress;
    }

    public void call(String route){
        request(serverAddress+route);
    }

    public void request(String url)
    {
        // send request to API
    }
}
