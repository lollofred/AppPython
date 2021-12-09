package app.python.spamemail;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    EditText From;
    EditText Passw;
    EditText To;
    EditText Subject;
    EditText Message;
    EditText NMessage;
    Button Spam;
    TextView output;

    SharedPreferences preference;

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

        //Inizialize Preferenze
        preference = getApplicationContext().getSharedPreferences("Preference",MODE_PRIVATE);

        String UsrCache = preference.getString("Usr","");
        String PasswCache = preference.getString("Passw","");

        if(!UsrCache.isEmpty())
            From.setText(UsrCache);
        if(!PasswCache.isEmpty())
           Passw.setText(PasswCache);


        //Link
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        View mView = getLayoutInflater().inflate(R.layout.popup, null);
                        CheckBox mCheckBox = mView.findViewById(R.id.checkBox);
    
                        builder.setCancelable(true);
                        builder.setTitle("Clear Fields");
                        builder.setMessage("Do you want to empty fields?");

                        builder.setView(mView);
    
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

                        AlertDialog dialog = builder.create();
                        dialog.show();

                        //To Handle Save Setting and not ask again Popup
                        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if(compoundButton.isChecked()){
                                    storeDialogStatus(true);
                                }else{
                                    storeDialogStatus(false);
                                }
                            }
                        });

                        if(getDialogStatus()){
                            dialog.hide();
                        }else{
                            dialog.show();
                        }

                        //Save Usr e passw in cache
                        preference.edit().putString("Usr",email).apply();
                        preference.edit().putString("Passw",passw).apply();
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private void storeDialogStatus(boolean isChecked){
        preference.edit().putBoolean("popup",isChecked).apply();
    }

    private boolean getDialogStatus(){
        return preference.getBoolean("popup", false);
    }
}