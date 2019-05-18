package com.example.onlymaavu;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class SignUp extends AppCompatActivity {

    EditText edtPhone, edtName, edtPassword;


    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtName=(MaterialEditText)findViewById(R.id.edtName);
        edtPhone=(MaterialEditText)findViewById(R.id.edtPhone);
        edtPassword=(MaterialEditText)findViewById(R.id.edtPassword);
        btnSignUp=(Button) findViewById(R.id.btnSignUp);
        final Vibrator vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.isConnectedToInternet(getBaseContext())) {
                    final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setMessage("Please Wait");
                    mDialog.show();
                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                mDialog.dismiss();
                                vibrator.vibrate(60);
                                Toast.makeText(SignUp.this, "Phone Number Already Registered", Toast.LENGTH_LONG).show();
                            } else {
                                mDialog.dismiss();
                                User user = new User(edtName.getText().toString(), edtPassword.getText().toString());
                                table_user.child(edtPhone.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent homeIntent=new Intent(SignUp.this, home.class);
                                Common.currentUser=user;
                                startActivity(homeIntent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {vibrator.vibrate(60);
                        Toast.makeText(SignUp.this,"Please Check Your Internet Connection",Toast.LENGTH_LONG).show();
                        return;
                    }
            }
        });

    }
}