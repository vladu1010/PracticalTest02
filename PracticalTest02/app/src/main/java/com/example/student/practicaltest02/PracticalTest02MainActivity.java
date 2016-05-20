package com.example.student.practicaltest02;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends ActionBarActivity {
    // Server widgets
    private EditText serverPortEditText = null;
    private Button connectButton = null;

    // Client widgets
    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;
    private Button getWeatherForecastButton = null;
    private TextView weatherForecastTextView = null;

    private EditText url = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Server port should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() != null) {
                serverThread.start();
            } else {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not creat server thread!");
            }

        }
    }

    private GetWeatherForecastButtonClickListener getWeatherForecastButtonClickListener = new GetWeatherForecastButtonClickListener();
    private class GetWeatherForecastButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort    = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty() ||
                    clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Client connection parameters should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (serverThread == null || !serverThread.isAlive()) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
                return;
            }

            String prefix = url.getText().toString();
            if (prefix == null || prefix.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Parameters from client (city / information type) should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            weatherForecastTextView.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(
                    clientAddress,
                    Integer.parseInt(clientPort),
                    prefix,
                    weatherForecastTextView);
            clientThread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        connectButton = (Button)findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        url = (EditText)findViewById(R.id.url);

        clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
        getWeatherForecastButton = (Button)findViewById(R.id.get_weather_forecast_button);
        getWeatherForecastButton.setOnClickListener(getWeatherForecastButtonClickListener);
        weatherForecastTextView = (TextView)findViewById(R.id.textview);
    }

    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}
