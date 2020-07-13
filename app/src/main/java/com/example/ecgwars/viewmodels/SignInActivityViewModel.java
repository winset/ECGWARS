package com.example.ecgwars.viewmodels;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;


import com.example.ecgwars.model.Scores;
import com.example.ecgwars.model.User;
import com.example.ecgwars.model.UserFriendsList;
import com.example.ecgwars.model.UserTestList;
import com.example.ecgwars.views.SignUp.RegistrationUserFragment;
import com.example.ecgwars.views.SignUp.SignInActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static android.app.Activity.RESULT_OK;
import static com.example.ecgwars.views.SignUp.SignInActivity.GOOGLE_SIGN;

public class SignInActivityViewModel extends ViewModel {

    public static final String PHONE_REG = "PHONE_REGESTRATION";
    public static final String GOOGLE_REG = "GOOGLE_REGESTRATION";

    private MutableLiveData<User> user;
    private MutableLiveData<String> fistName = new MutableLiveData<String>();
    private MutableLiveData<String> lastName = new MutableLiveData<String>();
    private MutableLiveData<Uri> avatarImage = new MutableLiveData<Uri>();
    private MutableLiveData<String> avatarURL = new MutableLiveData<String>();
    private MutableLiveData<PhoneAuthCredential> credentialMutableLiveData = new MutableLiveData<PhoneAuthCredential>();
    private MutableLiveData<GoogleSignInAccount> googleSignInAccountMutableLiveData = new MutableLiveData<GoogleSignInAccount>();


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    //phone sing up or log in
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private String phoneVerificationId;
    public String number;


    private FirebaseDatabase userDatabase;
    private DatabaseReference userDatabaseReference;
    private DatabaseReference userScoresReference;
    private DatabaseReference userTestReference;
    private DatabaseReference userFriendsReference;

    private StorageReference profileStorageRef = FirebaseStorage.getInstance().getReference("user profile images");

    private RegistrationUserFragment registrationUserFragment = RegistrationUserFragment.newInstance();



    public void setFistName(String string) {
        fistName.setValue(string);
    }

    public void setLastName(String string) {
        lastName.setValue(string);
    }

    public void setAvatarImage(Uri uri) {
        avatarImage.setValue(uri);
    }

    public LiveData<Uri> getAvatarImage() {
        return avatarImage;
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<String> getFirstName() {
        return fistName;
    }

    public LiveData<String> getLastName() {
        return lastName;
    }

    public LiveData<PhoneAuthCredential> getCredentialMutableLiveData() {
        return credentialMutableLiveData;
    }

    public LiveData<GoogleSignInAccount> getGoogleSignInAccountMutableLiveData() {
        return googleSignInAccountMutableLiveData;
    }

    public LiveData<String> getAvatarURL() {
        return avatarURL;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //CROP PROFILE IMAGE
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                if (registrationUserFragment != null) {
                    Log.d("ImageAvatar", "Uri: " + resultUri);
                    registrationUserFragment.setAvatarImage(resultUri);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        //GOOGLE SIGN UN
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                googleSignInAccountMutableLiveData.setValue(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(SignInActivity.TAG, "Google sign in failed", e);
          //      Toast.makeText(getApplication().getApplicationContext(), "Google sign in failed " + e.getLocalizedMessage(),
                 //       Toast.LENGTH_LONG).show();
                // ...
            }
        }
    }

    /*
     * SET AND UPLOAD AVATAR IMAGE
     * PLEASE REFAKTOR ALLL THIS SHIITT CODEEEEEEEEEE
     * */
    public void setUserAvatarImage(Activity activity) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setAspectRatio(1, 1)
                .start(activity);
    }

    public void uploadAvatarImage(String s, GoogleSignInAccount account) {
        Log.d(SignInActivity.TAG, "Upload avatar image" + avatarURL.toString());
        if (avatarImage.getValue() != null) {

            StorageReference fileReference = profileStorageRef.child(System.currentTimeMillis()
                    + "/" + avatarImage.getValue());

            UploadTask uploadTask = fileReference.putFile(avatarImage.getValue());
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Log.d(SignInActivity.TAG, "Fail upload avatar image" + avatarURL.toString());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Log.d(SignInActivity.TAG, "Success upload avatar image" + avatarURL.toString());
                }
            });
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    Log.d(SignInActivity.TAG, "Try to get URL " + avatarURL.toString());
                    // Continue with the task to get the download URL
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        avatarURL.setValue(downloadUri.toString());
                        createUser(s, account);
                        Log.d(SignInActivity.TAG, "Success to get URL " + downloadUri);
                    } else {
                        // Handle failures
                        // ...
                        avatarURL.setValue("Profile url");//НАПИСАТЬ ПУТЬ К СТАНДАРТНОМУ ИЗОБРАЖЕНИЮ ПРОФИЛЯ
                        createUser(s, account);
                        Log.d(SignInActivity.TAG, "Fail to get URL ");
                    }
                }
            });
        } else {
            avatarURL.setValue("Profile url");//НАПИСАТЬ ПУТЬ К СТАНДАРТНОМУ ИЗОБРАЖЕНИЮ ПРОФИЛЯ
            createUser(s, account);
        }
    }
    /*
     * END OF SET AND UPLOAD PROFILE IMAGE
     *
     * */

    /*
     * User phone auth
     *
     * */
    public void userPhoneNumber(String number, Activity activity) {
        this.number = number;
        // if(mAuth.getU)
        setUpVerificationCallback();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number, //Phone number
                60, //Timeout duration
                TimeUnit.SECONDS, //Unit of time out
                activity,
                verificationCallbacks);
    }


    public void resendCode(String number, Activity activity) {
        setUpVerificationCallback();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number, //Phone number
                60, //Timeout duration
                TimeUnit.SECONDS, //Unit of time out
                activity,
                verificationCallbacks,
                resendToken);
    }


    public void verifyCode(String code) {
        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(phoneVerificationId, code);
        credentialMutableLiveData.setValue(credential);
    }

    private void setUpVerificationCallback() {
        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        credentialMutableLiveData.setValue(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        //    Toast.makeText(getApplication().getApplicationContext(),
                          //          "Invalid credentials: " + e.getLocalizedMessage()
                             //       , Toast.LENGTH_LONG).show();
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                         //   Toast.makeText(getApplication().getApplicationContext(), "SMS Quota exceeded ",
                           //         Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {
                        phoneVerificationId = verificationId;
                        resendToken = token;
                    }
                };
    }


    /*
     * End user phone auth
     */

    public void createUser(String switchReg, GoogleSignInAccount account) {
        userDatabase = FirebaseDatabase.getInstance();
        userDatabaseReference = userDatabase.getReference().child("users").child(mAuth.getUid());
        userScoresReference = userDatabase.getReference().child("usersScores").child(mAuth.getUid());
        userTestReference = userDatabase.getReference().child("usersTests").child(mAuth.getUid()).child("easylvl");
        userFriendsReference = userDatabase.getReference().child("usersFriends").child(mAuth.getUid());
        User user = new User();
        //create user
        switch (switchReg) {
            case PHONE_REG:
                user.setFirstName(getFirstName().getValue());
                user.setLastName(getLastName().getValue());
                user.setAvatar(getAvatarURL().getValue());
                user.setId(mAuth.getUid());
                break;
            case GOOGLE_REG:
                user.setFirstName(account.getGivenName());
                user.setLastName(account.getFamilyName());
                user.setId(mAuth.getUid());
                user.setAvatar(String.valueOf(account.getPhotoUrl()));
                break;
            default:
                break;
        }
        userDatabaseReference.setValue(user);
        Scores scores = new Scores(0);
        UserTestList testList = new UserTestList();
        UserFriendsList friendsList =new UserFriendsList(0);

        userTestReference.setValue(testList);
        userScoresReference.setValue(scores);
        userFriendsReference.setValue(friendsList);
    }
}
