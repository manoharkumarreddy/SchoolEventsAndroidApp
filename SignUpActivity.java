package com.example.manoh.project515;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by manoh on 3/20/2018.
 */

public class SignUpActivity extends AppCompatActivity {

    // Declaring all the elements we have in activity_signup.xml
    Button register;
    EditText name_user,id_user,password_user;
    // Declaring the variables needed to store some values in program
    String name_holder,id_holder,password_holder;
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    String HttpUrl = "http://undcemcs02.und.edu/~manoharkumarreddy.da/Registration.php" ;
    HashMap<String,String> hashMap = new HashMap<>();
    String finalResult;
    HttpParse httpParse = new HttpParse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Assigning Id's
        name_user = (EditText) findViewById(R.id.UserName);
        id_user = (EditText) findViewById(R.id.UserId1);
        password_user = (EditText) findViewById(R.id.Password1);
        register = (Button) findViewById(R.id.UserSignUp1);

        // Adding click listner on button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Checking whether edittext is empty or not
                CheckEditTextIsEmptyOrNot();

                if(CheckEditText)
                {
                    // if CheckEditText is true
                    UserRegisterFunction(name_holder,id_holder,password_holder);
                }
                else
                {
                    // if CheckEditText is false
                    Toast.makeText(SignUpActivity.this,"Please fill all the fields.",Toast.LENGTH_LONG).show();
                }
            }
        });





    }

    // CheckEditTextEmptyOrNot
    public void CheckEditTextIsEmptyOrNot()
    {
        name_holder = name_user.getText().toString();
        id_holder = id_user.getText().toString();
        password_holder = password_user.getText().toString();

        if(TextUtils.isEmpty(name_holder) || TextUtils.isEmpty(id_holder) || TextUtils.isEmpty(password_holder))
        {
            CheckEditText = false;
        }
        else
        {
            CheckEditText = true;
        }
    }

    // RegisterFunction
    public void UserRegisterFunction(final String U_name, final String U_id, final String U_password)
    {
        class UserRegisterFunctionClass extends AsyncTask<String,Void,String>
        {

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(SignUpActivity.this,"Data Loading",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg)
            {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                intent.putExtra("NameOfUser",U_name);
                startActivity(intent);
                Toast.makeText(SignUpActivity.this,httpResponseMsg,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params)
            {
                hashMap.put("u_name",params[0]);
                hashMap.put("u_id",params[1]);
                hashMap.put("u_password",params[2]);
                finalResult = httpParse.postRequest(hashMap, HttpUrl);

                return finalResult;
            }

        }
        UserRegisterFunctionClass userRegisterFunctionClass = new UserRegisterFunctionClass();
        userRegisterFunctionClass.execute(U_name,U_id,U_password);
    }


}
