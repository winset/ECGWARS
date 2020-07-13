package com.example.ecgwars.views.SignUp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.multidex.MultiDex;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.ecgwars.R;
import com.example.ecgwars.viewmodels.SignInActivityViewModel;
import com.example.ecgwars.views.HomeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;


public class SignInActivity extends AppCompatActivity implements onFragmentDataListener {


    public static final String TAG = "SignInActivity";
    public static final int GOOGLE_SIGN = 2454;

    private SignInActivityViewModel signInActivityViewModel;// Our view model

    private WelcomeFragment welcomeFragment;
    private SignUpFragment signUpFragment;
    private VerificationFragment verificationFragment;
    private RegistrationUserFragment registrationUserFragment;
    private LogInFragment logInFragment;
    //   private User currentUser;

    private FirebaseAuth mAuth;
    /*
        private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
        private PhoneAuthProvider.ForceResendingToken resendToken;
    */
    private GoogleSignInClient mGoogleSignInClient;

    //   private String phoneVerificationId;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        welcomeFragment = WelcomeFragment.newInstance();

        mAuth = FirebaseAuth.getInstance();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, welcomeFragment);
        fragmentTransaction.commit();

        //Google log in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
        }

        signInActivityViewModel = new ViewModelProvider(this)
                .get(SignInActivityViewModel.class);

        final Observer<PhoneAuthCredential> phoneAuthCredentialObserver = new Observer<PhoneAuthCredential>() {
            @Override
            public void onChanged(@Nullable final PhoneAuthCredential credential) {
                // Update the UI, in this case, a TextView.
                signInWithPhoneAuthCredential(credential);
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        signInActivityViewModel.getCredentialMutableLiveData().observe(this, phoneAuthCredentialObserver);

        final Observer<GoogleSignInAccount> googleSignInAccountObserver = new Observer<GoogleSignInAccount>() {
            @Override
            public void onChanged(@Nullable final GoogleSignInAccount googleSignInAccount) {
                // Update the UI, in this case, a TextView.
                firebaseAuthWithGoogle(googleSignInAccount);
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        signInActivityViewModel.getGoogleSignInAccountMutableLiveData().observe(this, googleSignInAccountObserver);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        signInActivityViewModel.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Change of fragments
    @Override
    public void onFragmentChange(String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Log.d("MainActivity", "TAG == " + tag);
        switch (tag) {
            case WelcomeFragment.TAG:
                welcomeFragment = WelcomeFragment.newInstance();
                fragmentTransaction.replace(R.id.container, welcomeFragment);
                fragmentTransaction.addToBackStack(WelcomeFragment.TAG);
                fragmentTransaction.commit();
                break;
            case SignUpFragment.TAG:
                signUpFragment = SignUpFragment.newInstance();
                fragmentTransaction.replace(R.id.container, signUpFragment);
                fragmentTransaction.addToBackStack(SignUpFragment.TAG);
                fragmentTransaction.commit();
                break;
            case VerificationFragment.TAG:
                verificationFragment = VerificationFragment.newInstance();
                fragmentTransaction.replace(R.id.container, verificationFragment);
                fragmentTransaction.addToBackStack(VerificationFragment.TAG);
                fragmentTransaction.commit();
                break;
            case RegistrationUserFragment.TAG:
                registrationUserFragment = RegistrationUserFragment.newInstance();
                fragmentTransaction.replace(R.id.container, registrationUserFragment);
                fragmentTransaction.addToBackStack(RegistrationUserFragment.TAG);
                fragmentTransaction.commit();
                break;
            case LogInFragment.TAG:
                logInFragment = LogInFragment.newInstance();
                fragmentTransaction.replace(R.id.container, logInFragment);
                fragmentTransaction.addToBackStack(LogInFragment.TAG);
                fragmentTransaction.commit();
                break;

            default:
                break;
        }
    }

    @Override
    public void logInSocial(String tag) {
        switch (tag) {
            case LogInFragment.GOOGLE_LOG_IN:
                googleSignIn();
                break;
            case LogInFragment.FACEBOOK_LOG_IN:
                break;
            case LogInFragment.TWITTER_LOG_IN:
                break;
            default:
                break;
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();

                            if (task.getResult().getAdditionalUserInfo().isNewUser()){
                                signInActivityViewModel.uploadAvatarImage(SignInActivityViewModel.PHONE_REG, null);//продумать логику возможно изменить место загрузки
                            }

                            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getApplicationContext(), "The verification code entered was invalid ",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                          if(task.getResult().getAdditionalUserInfo().isNewUser()){
                              signInActivityViewModel.createUser(SignInActivityViewModel.GOOGLE_REG,acct);
                          }

                            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                            startActivity(intent);
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication Failed.",
                                    Toast.LENGTH_SHORT).show();
                            //      updateUI(null);
                        }
                        // ...
                    }
                });
    }
}




/*   // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                currentUser = new User();
                currentUser.setFirstName(account.getGivenName());
                currentUser.setLastName(account.getFamilyName());
                currentUser.setAvatar(String.valueOf(account.getPhotoUrl()));
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Google sign in failed " + e.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
                // ...
            }
        }*/

 /*if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                if (registrationUserFragment != null) {
                    Log.d("ImageAvatr", "Uri: " + resultUri);
                    registrationUserFragment.setAvatarImage(resultUri);

                    Picasso.get().load(resultUri)
                            .resize(150, 150)
                            .centerCrop()
                            .transform(new CropCircleTransformation())
                            .into((ImageView) findViewById(R.id.userAvatarRegistrationImageButton));

                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }*/
  /* @Override
    public void userPhoneNumber(String number) {
        setUpVerificationCallback();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number, //Phone number
                60, //Timeout duration
                TimeUnit.SECONDS, //Unit of time out
                this,
                verificationCallbacks);
    }

    @Override
    public void resendCode(String number) {
        setUpVerificationCallback();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number, //Phone number
                60, //Timeout duration
                TimeUnit.SECONDS, //Unit of time out
                this,
                verificationCallbacks,
                resendToken);
    }

    @Override
    public void verifyCode(String code) {
        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(phoneVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }*/

   /* @Override
    public void setUserAvatarImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setAspectRatio(1, 1)
                .start(this);

    }*/

  /*  private void setUpVerificationCallback() {
        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(getApplicationContext(),
                                    "Invalid credentials: " + e.getLocalizedMessage()
                                    , Toast.LENGTH_LONG).show();
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            Toast.makeText(getApplicationContext(), "SMS Quota exceeded ",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {

                        phoneVerificationId = verificationId;
                        resendToken = token;
                    }
                };
    }*/