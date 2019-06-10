package ke.co.taxityzltd.taxityz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    //defining view objects
    private EditText editTextEmail;
    private EditText editTextPassword,editTextName,editTextPhone;
    private Button buttonSignup;
    private FloatingActionButton fab;
    private Button textViewSignin;

    private ProgressDialog progressDialog,pd;
    DatabaseReference databaseUser;
    String myPhone,myName,myMail;


    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_t);

        fab= findViewById(R.id.fab);
        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //initialise firbase database
        databaseUser= FirebaseDatabase.getInstance().getReference("Taxityz_Users");
fab.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        //and open profile activity
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();

    }
});
        //if getCurrentUser does not returns null
        if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);

        buttonSignup = (Button) findViewById(R.id.buttonSignup);

        progressDialog = new ProgressDialog(this);
        pd = new ProgressDialog(this);

        //attaching listener to button
        buttonSignup.setOnClickListener(this);
        //textViewSignin.setOnClickListener(this);

    }

    private void registerUser(){

        //getting email and password from edit texts
        myMail = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();
       myName = editTextName.getText().toString().trim();
        myPhone = editTextPhone.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(myMail)){
            Toast.makeText(this,"Enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Eenter password",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(myName)){
            Toast.makeText(this,"Enter Name",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(myPhone)){
            Toast.makeText(this,"Eenter phone",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(myMail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                                pd.setMessage("Please wait..........");
                                pd.show();

                                userFirebase();

                        }else{
                            //display some message here
                            Toast.makeText(SignUp.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }

                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {

        if(view == buttonSignup){
            registerUser();
        }

        if(view == textViewSignin){
            //open login activity when user taps on the already registered textview
            startActivity(new Intent(this, Login.class));
        }

    }

    private void mDialog(String title, String message,int code){
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(title)
                .titleGravity(GravityEnum.CENTER)
                .customView(R.layout.success_dialog, true)
                .positiveText("OK")
                .cancelable(false)
                .widgetColorRes(R.color.colorPrimary)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        dialog.dismiss();
                        finish();
                    }
                })
                .build();
        View view=dialog.getCustomView();
        TextView messageText = (TextView)view.findViewById(R.id.message);
        ImageView imageView = (ImageView)view.findViewById(R.id.success);
        if (code != 0){
            imageView.setVisibility(View.GONE);
        }
        messageText.setText(message);
        dialog.show();
    }

    private void userFirebase(){
       FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        // FirebaseUser user =firebaseAuth.getCurrentUser();

        //we are creating a unique ID using the push method of firebase
        // String ID = databaseUser.push().getKey();
        //create a new UserReg by calling the model class
        UserModel userModel= new UserModel(myMail,myPhone,myName);
        //upload stuff to firebase database
        if (user != null) {
            databaseUser.child(user.getUid()).setValue(userModel);
        }


        //display a toast message
        pd.dismiss();
        Toast.makeText(this,"Thank you for choosing Taxityz",Toast.LENGTH_LONG).show();
        mDialog("Welcome to Taxityz Family",myName+" Thank you for choosing Taxityz",0);

    }
}