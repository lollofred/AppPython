package com.example.apppython;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class MainActivity extends AppCompatActivity {

    EditText From;
    EditText Passw;
    EditText To;
    EditText Subject;
    EditText Message;
    EditText NMessage;
    Button Run;
    TextView output;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        From = (EditText)findViewById(R.id.from);
        Passw = (EditText)findViewById(R.id.passw);
        To = (EditText)findViewById(R.id.to);
        Subject = (EditText)findViewById(R.id.subject);
        NMessage = (EditText)findViewById(R.id.nMessage);
        Run = (Button)findViewById(R.id.run);
        output = (TextView)findViewById(R.id.output);

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }


        //now on click on run button we will extract code from code area data and send that data to our python script..

        Run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Python py = Python.getInstance();

                //here we call our script with the name "myscirpt
                PyObject pyobj = py.getModule("spam_email");  //give python script name

                String email = "antix568@gmail.com";
                String passw = "passwantix568";
                String email2 = "antix568@gmail.com";
                String oggetto = "Test";
                String contenuto = "Test";
                Integer NUMBER_OF_EMAILS = 2;



                //and call main method inside script...//pass data here
                PyObject obj = pyobj.callAttr("main",
                        From.getText().toString(),Passw.getText().toString(),To.getText().toString(),
                        Subject.getText().toString(),Message.getText().toString(),Integer.parseInt(NMessage.getText().toString()));

                //here we will set returned value of our python script to our output textview
                output.setText(obj.toString());
            }
        });

    }
}