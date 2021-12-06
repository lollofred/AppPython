package app.python.spamemail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import app.python.spamemail.R;

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
        Message = (EditText)findViewById(R.id.message);
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





                String email = From.getText().toString();
                String passw = Passw.getText().toString();
                String email2 = To.getText().toString();
                String oggetto = Subject.getText().toString();
                String contenuto = Message.getText().toString();
                Integer NUMBER_OF_EMAILS = Integer.parseInt(NMessage.getText().toString());



                //and call main method inside script...//pass data here
                PyObject obj = pyobj.callAttr("main",
                        email,passw,email2,oggetto,contenuto,NUMBER_OF_EMAILS);

                //here we will set returned value of our python script to our output textview
                output.setText(obj.toString());
            }
        });

    }
}