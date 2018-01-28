package com.example.wautel_l.raspberry_chat;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Random;

import static java.lang.Thread.activeCount;
import static java.lang.Thread.sleep;

/**
 * Created by wautel_l on 20/06/2017.
 */

public class LocalService  {


        private String serverMessage;
        /**
         * Specify the Server Ip Address here. Whereas our Socket Server is started.
         */
        public static String SERVERIP = ""; // your computer IP address
        public static int SERVERPORT = 0;
        private OnMessageReceived mMessageListener = null;
        private boolean mRun = false;

        private PrintWriter out = null;
        private BufferedReader in = null;

        /**
         * Constructor of the class. OnMessagedReceived listens for the messages received from server
         */
        public LocalService(final OnMessageReceived listener) {
            mMessageListener = listener;
        }

        public void setinfos(String ip, int port)
        {
            SERVERIP = ip;
            SERVERPORT = port;
        }

        /**
         * Sends the message entered by client to the server
         *
         * @param message text entered by client
         */
        public void sendMessage(String message) {
            if (out != null && !out.checkError()) {
                System.out.println("message: " + message);
                out.println(message);
                out.flush();
            }
        }

        public void stopClient() {
            mRun = false;
        }

        public void run() {

            mRun = true;

            try {
                //here you must put your computer's IP address.

                InetAddress serverAddr = InetAddress.getByName(SERVERIP);


                //create a socket to make the connection with the server
                Socket socket = new Socket(serverAddr, SERVERPORT);


                try {

                    //send the message to the server
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                    //receive the message which the server sends back
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    //in this while the client listens for the messages sent by the server
                    while (mRun) {
                        serverMessage = in.readLine();

                        if (serverMessage != null && mMessageListener != null) {
                            //call the method messageReceived from MyActivity class
                            mMessageListener.messageReceived(serverMessage);
                            Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
                        }
                        serverMessage = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //the socket must be closed. It is not possible to reconnect to this socket
                    // after it is closed, which means a new socket instance has to be created.
                    socket.close();
                }

            } catch (Exception e) {

                Log.e("TCP SI Error", "SI: Error", e);

            }

        }

        //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
        //class at on asynckTask doInBackground
        public interface OnMessageReceived {
            public void messageReceived(String message);
        }
}
