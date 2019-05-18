package com.example.onlymaavu;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.onlymaavu.Model.User;
import com.example.onlymaavu.common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    Button btnSignIn;
    CheckBox ckbRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword=(MaterialEditText)findViewById(R.id.edtPassword);
        edtPhone=(MaterialEditText)findViewById(R.id.edtPhone);
        btnSignIn=(Button)findViewById(R.id.btnSignIn);
        ckbRemember=(com.rey.material.widget.CheckBox)findViewById(R.id.ckbRemember);
        final Vibrator vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if(Common.isConnectedToInternet(getBaseContext()))
{
if(ckbRemember.isChecked())
{
    Paper.book().write(Common.USER_KEY,edtPhone.getText().toString());
    Paper.book().write(Common.PWD_KEY,edtPassword.getText().toString());

}

                final ProgressDialog mDialog= new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please Wait");
                mDialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(edtPhone.getText().toString()).exists())
                        {
                            mDialog.dismiss();
                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                            if (user.getPassword().equals(edtPassword.getText().toString()))
                            {
                                Intent homeIntent=new Intent(SignIn.this, home.class);
                                Common.currentUser=user;
                                startActivity(homeIntent);
                                finish();
                            }
                            else
                                {
                                    vibrator.vibrate(60);
                                    Toast.makeText(SignIn.this, "Sign in failed", Toast.LENGTH_LONG).show();
                                }
                        }
                        else
                        {mDialog.dismiss();
                            vibrator.vibrate(60);
                            Toast.makeText(SignIn.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
else
{vibrator.vibrate(60);
    Toast.makeText(SignIn.this,"Please Check Your Internet Connection",Toast.LENGTH_SHORT).show();
    return;
}}
        });
    }
}
