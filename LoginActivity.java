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

public class LoginActivity extends AppCompatActivity {

    //Declaring all the elements that we have used in activity_login.xml
    Button registration, valid_user;
    EditText valid_user_id,valid_user_password;

    //Declaring the variables needed to store some values required in the program
    String holder_id, holder_password,user_name_holder;
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap1 = new HashMap<>();
    String finalResult1 = "";
    String HttpURL1 = "http://undcemcs02.und.edu/~manoharkumarreddy.da/LoginUser.php" ;
    HttpParse httpParse = new HttpParse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Directing to signup page.
        registration = (Button) findViewById(R.id.UserSignUp);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        //Assigning id's
        valid_user = (Button) findViewById(R.id.UserSignIn);
        valid_user_id = (EditText) findViewById(R.id.UserId);
        valid_user_password = (EditText) findViewById(R.id.Password);

        //Storing the name we are getting from SignUpActivity.java
        user_name_holder = getIntent().getStringExtra("NameOfUser");


        //Actions when users hit SignIn button.
        valid_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Checking whether user filled all the form fields or no.
                CheckEditTextIsEmptyOrNot();

                //Performing actions based on the value of CheckEditTextIsEmptyOrNot.
                if(CheckEditText)
                {
                    UserLoginFunction(holder_id,holder_password);
                    //Intent intent = new Intent(LoginActivity.this,AdminActivity.class);
                    //startActivity(intent);
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Please fill all the fields",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Checking whether all form fields are entered or not.
    public void CheckEditTextIsEmptyOrNot()
    {
      holder_id = valid_user_id.getText().toString();
      holder_password = valid_user_password.getText().toString();

      if(TextUtils.isEmpty(holder_id) || TextUtils.isEmpty(holder_password))
      {
          CheckEditText = false;
      }
      else
      {
          CheckEditText = true;
      }
    }

    //UserLoginFunction sending parameters to HttpParse.java
    public void UserLoginFunction(final String U_id, final String U_password)
    {
        class UserLoginFunctionClass extends AsyncTask<String,Void,String>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(LoginActivity.this,"Data Loading",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpresponseMsg)
            {
                super.onPostExecute(httpresponseMsg);
                progressDialog.dismiss();
                if(httpresponseMsg.equalsIgnoreCase("Admin")) {
                    //Toast.makeText(LoginActivity.this, httpresponseMsg, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                    startActivity(intent);
                }
                else if(httpresponseMsg.equalsIgnoreCase("User")) {
                    //Toast.makeText(LoginActivity.this, httpresponseMsg, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                    intent.putExtra("IdOfUser",U_id);
                    intent.putExtra("UserNameOfId",user_name_holder);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(LoginActivity.this,httpresponseMsg,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params)
            {
               hashMap1.put("u_id",params[0]);
               hashMap1.put("u_password",params[1]);
               finalResult1 = httpParse.postRequest(hashMap1,HttpURL1);
               return finalResult1;
            }
        }

        UserLoginFunctionClass userLoginFunctionClass = new UserLoginFunctionClass();
        userLoginFunctionClass.execute(U_id,U_password);

    }
}
