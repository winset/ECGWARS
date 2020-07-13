package com.example.ecgwars.viewmodels;

import android.util.Log;

import com.example.ecgwars.model.Scores;
import com.example.ecgwars.views.Tests.TestActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TestActivityViewModel extends ViewModel {

    private MutableLiveData<String> testText = new MutableLiveData<>();



    private MutableLiveData<ArrayList<String>> answers = new MutableLiveData<>();
    private MutableLiveData<String> imageUrl = new MutableLiveData<>();
    private MutableLiveData<String> testIdText = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> answImagesUrl = new MutableLiveData<>();
    private MutableLiveData<String> answText = new MutableLiveData<>();
    private MutableLiveData<Integer> userAnswer = new MutableLiveData<>();
    private MutableLiveData<Integer> truAnswer = new MutableLiveData<>();

    public LiveData<Boolean> getPassAll() {
        return passAll;
    }

    public void setPassAll(MutableLiveData<Boolean> passAll) {
        this.passAll = passAll;
    }

    private MutableLiveData<Boolean> passAll = new MutableLiveData<>();

    private String textOfTest;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseDatabase userDatabase;


    private FirebaseDatabase testDatabase;
    private DatabaseReference userScoresReference;
    private DatabaseReference userTestReference;
    private DatabaseReference testReference;


    private DatabaseReference userDatabaseReference;


    private int[] excludeNumbers;

    public LiveData<Integer> getTruAnswer() {
        return truAnswer;
    }

    public void setUserAnswer(int userAnswer) {
        this.userAnswer.setValue(userAnswer);
    }

    public MutableLiveData<Integer> getUserAnswer() {
        return userAnswer;
    }


    public LiveData<String> getAnswText() {
        return answText;
    }

    public MutableLiveData<ArrayList<String>> getAnswImagesUrl() {
        return answImagesUrl;
    }

    public MutableLiveData<String> getTestId() {
        return testIdText;
    }

    public LiveData<String> getTestText() {
        return testText;
    }

    public LiveData<ArrayList<String>> getAnswers() {
        return answers;
    }
    public void clearAnswers() {
        ArrayList<String> emp = new ArrayList<>();
        emp.clear();
        answers.setValue(emp);
    }
    public LiveData<String> getImageUrl() {
        return imageUrl;
    }

    private int testId;

    public void getTest(String lvl,int count) {
        testDatabase = FirebaseDatabase.getInstance();
        switch (lvl) {
            case "basicLvL":

                testId = getRandomWithExclusion(1, count-1, excludeNumbers);
               /* if(testId==0){
                    testId++;//TODO fix that
                }else if(testId==count){
                    t
                }*/
                testIdText.setValue(testId + "");
                Log.d("TestActivity", "Test random " + testId);
                testReference = testDatabase.getReference().child("tests").child("easylvl").child(testId + "").child("test").child("text");
                testReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        textOfTest = dataSnapshot.getValue(String.class);

                        testText.setValue(textOfTest);
                        Log.d("TestActivity", "getTest text " + textOfTest);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                testReference = testDatabase.getReference().child("tests").child("easylvl").child(testId + "").child("test").child("answers");
                testReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    ArrayList<String> answersL = new ArrayList<>();

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("TestActivity", "Answer ");
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            answersL.add(data.getValue(String.class));
                        }
                        answers.setValue(answersL);
                        for (int i = 0; i < answersL.size(); i++) {
                            Log.d("TestActivity", "Answers list " + answersL.get(i));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                testReference = testDatabase.getReference().child("tests").child("easylvl").child(testId + "").child("test").child("imageUrl");
                testReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String url = dataSnapshot.getValue(String.class);
                        imageUrl.setValue(url);
                        Log.d("TestActivity", "Image Url " + url);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                testReference = testDatabase.getReference().child("tests").child("easylvl").child(testId + "").child("answer").child("images");
                testReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> s = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            s.add(data.getValue(String.class));
                        }
                        for (int i = 0; i < s.size(); i++) {
                            Log.d("TestActivity", "Answer Images Url " + s);
                        }

                        answImagesUrl.setValue(s);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                testReference = testDatabase.getReference().child("tests").child("easylvl").child(testId + "").child("answer").child("text");
                testReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        answText.setValue(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                testReference = testDatabase.getReference().child("tests").child("easylvl").child(testId + "").child("answer").child("truAnswer");
                testReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        truAnswer.setValue(dataSnapshot.getValue(Integer.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
            case "advancedLvL":
                break;
            case "profiLvL":

                break;
            default:
                break;
        }
    }

    public int getRandomWithExclusion(int start, int end, int... exclude) {


        Random rnd = new Random();
        int random = start + rnd.nextInt(end - start + 1 - exclude.length);
        for (int ex : exclude) {
            if (random < ex) {
                break;
            }
            random++;
        }
        return random;
    }

    public void getExcludeTest() {
        userDatabase = FirebaseDatabase.getInstance();
        userTestReference = userDatabase.getReference().child("usersTests").child(auth.getUid()).child("easylvl");
        userTestReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                excludeNumbers = new int[(int) dataSnapshot.getChildrenCount()];
                int i = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    //noinspection ConstantConditions
                    if(data.getValue(int.class).equals(0)){
                        data.getRef().setValue(null);
                    }
                    excludeNumbers[i] = data.getValue(int.class);
                    i++;
                }
                Arrays.sort(excludeNumbers);

                userTestReference = userDatabase.getReference().child("tests").child("easylvl");
                userTestReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(excludeNumbers.length != dataSnapshot.getChildrenCount()-1){
                            getTest("basicLvL", (int) dataSnapshot.getChildrenCount());
                            passAll.setValue(false);
                        }else {
                            passAll.setValue(true);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void increaseScore() {
        userDatabaseReference = FirebaseDatabase.getInstance().getReference();
        userDatabaseReference.child("usersScores").child(auth.getUid()).child("scores");
        userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = dataSnapshot.child("usersScores").child(auth.getUid()).child("scores").getValue(Integer.class);
                Log.d("TestActivity", "Current score" + i);
                // score.setValue(i+"");
                i+=50;
             userDatabaseReference.child("usersScores").child(auth.getUid()).child("scores").setValue(i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addUserTestList(){
        userDatabaseReference = FirebaseDatabase.getInstance().getReference();
        userDatabaseReference.child("usersTests").child(auth.getUid()).child("easylvl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount()+1;
                userDatabaseReference.child("usersTests").child(auth.getUid()).child("easylvl").child(count+"").setValue(testId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
