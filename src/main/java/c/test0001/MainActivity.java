package c.test0001;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {



    private EditText mPassword ;
    private EditText mEmail ;
    private ProgressDialog mProgressDialog ;
    private Button mloginButton ;
    private EditText mDisplayName  ;
    private Button mSignIn;




    // Firebase Database References
    private FirebaseAuth mAuth ;
    private DatabaseReference mDatabaseReferences ;
    private FirebaseAuth.AuthStateListener mAuthListener ;
    private static final int RC_SIGN_IN = 1;








    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        mDisplayName = (EditText)findViewById(R.id.displayName);
        mPassword = (EditText)findViewById(R.id.password);
        mEmail = (EditText)findViewById(R.id.email);
        mloginButton = (Button)findViewById(R.id.sign_upButton);
        mSignIn = (Button)findViewById(R.id.SignIn);



        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });


        mloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //   mProgressDialog.setMessage("Signing UP ...Please Wait");
                //   mProgressDialog.show();



               final String pass = mPassword.getText().toString();
               final String email = mEmail.getText().toString();



                mloginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        mDatabaseReferences = FirebaseDatabase.getInstance().getReference().child("Users");


                        Toast.makeText(MainActivity.this,"This Button Have been touched",Toast.LENGTH_SHORT).show();

                        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                if(task.isSuccessful()) {
                                    HashMap<String, String> dataMap = new HashMap<>();

                                    dataMap.put("image", "default");
                                    dataMap.put("username", mDisplayName.getText().toString());
                                    dataMap.put("status","There Hey ,Using Hermes I am");



                                    mDatabaseReferences.setValue(dataMap);


                                    Toast.makeText(MainActivity.this,"It's Done",Toast.LENGTH_SHORT).show();


                                   startActivity(new Intent(MainActivity.this,Main2Activity.class));


                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Sign Up Failed",Toast.LENGTH_SHORT);
                                }


                            }
                        });

                    }
                });

                    // TODO check
                //   startActivity(new Intent(getApplicationContext(),CreateGroup.class));

            }
        });



    }






}

