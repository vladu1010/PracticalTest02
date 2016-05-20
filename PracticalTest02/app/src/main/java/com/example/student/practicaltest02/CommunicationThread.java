package com.example.student.practicaltest02;

import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by student on 5/20/16.
 */
public class CommunicationThread extends Thread{
    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket != null) {
            try {
                BufferedReader bufferedReader = Utilities.getReader(socket);
                PrintWriter printWriter = Utilities.getWriter(socket);
                if (bufferedReader != null && printWriter != null) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type)!");
                    String url = bufferedReader.readLine();
                   // WeatherForecastInformation weatherForecastInformation = null;
                    if (url != null && !url.isEmpty()) {
                        if(url.contains("bad")) {
                            String result = "restricted";
                            printWriter.println(result);
                            printWriter.flush();
                        }
                        else {
                        Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpGet httpGet = new HttpGet(url);
                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        String pageSourceCode = httpClient.execute(httpGet, responseHandler);
                            printWriter.println(pageSourceCode);
                            printWriter.flush();
                        }
                           /* JSONArray resultArray = content.getJSONArray(Constants.ARRAY_NAME);

                            for (int i = 0; i < resultArray.length(); ++i) {
                                JSONObject object = resultArray.getJSONObject(i);
                                String name = object.getString("name");
                                if (result.isEmpty()) {
                                    result += name;
                                } else {
                                    result += ", " + name;
                                }
                            }*/

                          //  printWriter.println(result);
                          //  printWriter.flush();


                        /*} else {
                            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error getting the information from the webservice!");
                        }*/
                    } else {
                        Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type)!");
                    }
                } else {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] BufferedReader / PrintWriter are null!");
                }
                socket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        } else {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
        }
    }
}
