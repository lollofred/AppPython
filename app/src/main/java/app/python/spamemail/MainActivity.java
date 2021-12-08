package app.python.spamemail;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    Button Spam;
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
        Spam = (Button)findViewById(R.id.run);
        output = (TextView)findViewById(R.id.output);

        TextView link = (TextView) findViewById(R.id.link);
        link.setMovementMethod(LinkMovementMethod.getInstance());

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }


        //now on click on run button we will extract code from code area data and send that data to our python script..

        Spam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Python py = Python.getInstance();

                //here we call our script with the name "myscirpt
                PyObject pyobj = py.getModule("spam_email");  //give python script name

                if(FieldNotBlank())
                {
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


                    if(!obj.toString().startsWith("Error"))
                    {
                        //Popup
                        CharSequence[] charSequence = new CharSequence[] {"Remember chose"};
    
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    
                        builder.setCancelable(true);
                        builder.setTitle("Clear Fields");
                        builder.setMessage("Do you want to empty fields?");
    
                        builder.setSingleChoiceItems(charSequence, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO
                            }
                        });
    
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               dialogInterface.cancel();
                            }
                        });
    
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                CleanField();
                            }
                        });
                        builder.show();
                    }
                }
            }
        });

    }

    public void ShowHidePass(View view){

        if(view.getId()==R.id.show_pass_btn){

            if(Passw.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.hide_password);

                //Show Password
                Passw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.show_password);

                //Hide Password
                Passw.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }
    
    public void CleanField()
    {
        From.setText("");
        Passw.setText("");
        To.setText("");
        Subject.setText("");
        Message.setText("");
        NMessage.setText("");
        output.setText("");
    }
    
    public boolean FieldNotBlank()
    {
        boolean res = false;
        if( From.getText().toString().trim().equals(""))
            From.setError( "Is required!" );
        else if( Passw.getText().toString().trim().equals(""))
            Passw.setError( "Is required!" );
        else if( To.getText().toString().trim().equals(""))
            To.setError( "Is required!" );
        else if( Subject.getText().toString().trim().equals(""))
            Subject.setError( "Is required!" );
        else if( Message.getText().toString().trim().equals(""))
            Message.setError( "Is required!" );
        else if( NMessage.getText().toString().trim().equals(""))
            NMessage.setError( "Is required!" );
        else
            res = true;
        return res;
    }
}