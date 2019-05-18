package com.example.onlymaavu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlymaavu.Model.User;
import com.example.onlymaavu.common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn=(Button)findViewById(R.id.btnSignActive);
        btnSignUp=(Button)findViewById(R.id.btnSignUp);

Paper.init(this);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIn=new Intent(MainActivity.this,SignIn.class);
                startActivity(signIn);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp=new Intent(MainActivity.this,SignUp.class);
                startActivity(signUp);
            }
        });


checkInternetConnection();

String phone= Paper.book().read(Common.USER_KEY);
String pwd=Paper.book().read(Common.PWD_KEY);
if(phone!=null&&pwd!=null)
{
    if(!phone.isEmpty()&&!pwd.isEmpty())
        login(phone,pwd);
}
    }

    private void checkInternetConnection() {

        if(Common.isConnectedToInternet(getBaseContext()))
        {
            System.out.println("Working");
        }
        else
        {
            Toast.makeText(MainActivity.this,"Please Check Your Internet Connection",Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void login(final String phone, final String pwd) {


        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");
        final Vibrator vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        final ProgressDialog mDialog= new ProgressDialog(MainActivity.this);
        mDialog.setMessage("Please Wait");
        mDialog.show();
        table_user.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.child(phone).exists())
                {
                    mDialog.dismiss();
                    User user = dataSnapshot.child(phone).getValue(User.class);
                    user.setPhone(phone);
                    if (user.getPassword().equals(pwd))
                    {
                        Intent homeIntent=new Intent(MainActivity.this, home.class);
                        Common.currentUser=user;
                        startActivity(homeIntent);
                        finish();
                    }

                    else
                    {
vibrator.vibrate(60);
                        Toast.makeText(MainActivity.this, "Sign in failed", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {mDialog.dismiss();
                    vibrator.vibrate(60);
                    Toast.makeText(MainActivity.this, "User doesn't exist", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        }
}
