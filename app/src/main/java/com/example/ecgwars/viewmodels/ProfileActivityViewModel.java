package com.example.ecgwars.viewmodels;

import android.util.Log;

import com.example.ecgwars.model.Top;
import com.example.ecgwars.model.User;
import com.example.ecgwars.model.UserFriendsList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileActivityViewModel extends ViewModel {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private FirebaseDatabase userDatabase;
    private DatabaseReference userDatabaseReference;
    private DatabaseReference userFriendsReference;


    private MutableLiveData<String> avatarUrl = new MutableLiveData<>();
    private MutableLiveData<String> firstName = new MutableLiveData<>();
    private MutableLiveData<String> secondName = new MutableLiveData<>();
    private MutableLiveData<String> score = new MutableLiveData<>();


    private MutableLiveData<ArrayList<String>> scoresM = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> firstNameTopM = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> secondNameTopM = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> imageUrlTopM = new MutableLiveData<>();


    public LiveData<LinkedHashSet> getTopScores() {
        return topScores;
    }

    private MutableLiveData<LinkedHashSet> topScores = new MutableLiveData<>();
    public MutableLiveData<LinkedHashSet> friends = new MutableLiveData<>();
    public MutableLiveData<LinkedHashSet> search = new MutableLiveData<>();

    private ArrayList<String> firstNameTop = new ArrayList<>();
    private ArrayList<String> secondNameTop = new ArrayList<>();
    private ArrayList<String> imageUrlTop = new ArrayList<>();

    private ArrayList<String> friendsId = new ArrayList<>();
    private ArrayList<String> scoreFriends = new ArrayList<>();
    private ArrayList<String> firstNameFriends = new ArrayList<>();
    private ArrayList<String> secondNameFriends = new ArrayList<>();
    private ArrayList<String> imageUrlFriends = new ArrayList<>();

    private ArrayList<String> idSearch = new ArrayList<>();
    private ArrayList<String> firstNameSearch = new ArrayList<>();
    private ArrayList<String> secondNameSearch = new ArrayList<>();
    private ArrayList<String> imageUrlSearch = new ArrayList<>();

    public LiveData<LinkedHashSet> getSearch() {
        return search;
    }

    public LiveData<LinkedHashSet> getFriends() {
        return friends;
    }

    public MutableLiveData<String> getScore() {
        return score;
    }

    public LiveData<String> getAvatarUrl() {
        return avatarUrl;
    }

    public LiveData<String> getFirstName() {
        return firstName;
    }

    public LiveData<String> getSecondName() {
        return secondName;
    }


    public LiveData<ArrayList<String>> getScoresM() {
        return scoresM;
    }

    public LiveData<ArrayList<String>> getFirstNameTopM() {
        return firstNameTopM;
    }

    public LiveData<ArrayList<String>> getSecondNameTopM() {
        return secondNameTopM;
    }

    public LiveData<ArrayList<String>> getImageUrlTopM() {
        return imageUrlTopM;
    }

    public void getUserInfo() {
        userDatabaseReference = FirebaseDatabase.getInstance().getReference();
        userDatabaseReference.child("users").child(mAuth.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        avatarUrl.setValue(user.getAvatar());//Обработать NPE
                        firstName.setValue(user.getFirstName());
                        secondName.setValue(user.getLastName());
                        Log.d("SecondName", "Stcond name is: " + secondName.getValue());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
        userDatabaseReference.child("usersScores").child(mAuth.getUid()).child("scores");
        userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = dataSnapshot.child("usersScores").child(mAuth.getUid()).child("scores").getValue(Integer.class);
                Log.d("TestActivity", "Current score" + i);
                score.setValue(i + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getScores() {
        ArrayList<String> scores = new ArrayList<>();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference();
        userDatabaseReference.child("usersScores").orderByChild("scores").limitToLast(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    scores.add(data.child("scores").getValue().toString());

                    getTopInfo(data.getKey());
                }
                scoresM.setValue(scores);
                firstNameTopM.setValue(firstNameTop);
                secondNameTopM.setValue(secondNameTop);
                imageUrlTopM.setValue(imageUrlTop);

                Log.d("Top", "size " + scoresM.getValue().size() + firstNameTopM.getValue().size() + secondNameTopM.getValue().size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d("Top", "size1123 " + scores.size() + firstNameTop.size() + secondNameTop.size());
    }

    private void getTopInfo(String key) {
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        userDatabaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                firstNameTopM.getValue().add(user.getFirstName());
                secondNameTop.add(user.getLastName());
                imageUrlTop.add(user.getAvatar());
                Log.d("Top", "size2 " + scoresM.getValue().size() + firstNameTopM.getValue().size() + secondNameTopM.getValue().size());
                top();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void top() {
        LinkedHashSet top = new LinkedHashSet();
        Log.d("Top", "score size " + getScoresM().getValue().size());
        Log.d("Top", "f size " + getFirstNameTopM().getValue().size());
        Log.d("Top", "s size " + getSecondNameTopM().getValue().size());
        Log.d("Top", "i size " + getImageUrlTopM().getValue().size());
        if (getFirstNameTopM().getValue().size() == getScoresM().getValue().size()) {
            int j = 0;
            for (int i = getScoresM().getValue().size() - 1; i > -1; i--) {
                j++;
                String score = getScoresM().getValue().get(i);
                String firstName = getFirstNameTopM().getValue().get(i);
                String secondName = getSecondNameTopM().getValue().get(i);
                String imageURL = getImageUrlTopM().getValue().get(i);
                Log.d("Top", "score " + score + " first " + firstName + " last " + secondName + " image " + imageURL);
                top.add(new Top(j + "", firstName, secondName, imageURL, score));
            }
            topScores.setValue(top);
        }
    }

    public void getFriendsId() {

//.child("usersFriends").child(mAuth.getUid()).child("friendsCount")
        userFriendsReference = FirebaseDatabase.getInstance().getReference();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference();
        userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //friendsList.getFriendsCount()
                int count = dataSnapshot.child("usersFriends").child(mAuth.getUid()).child("friendsCount").getValue(Integer.class);
                if (count > 0) {
                    for (int i = 0; i < count; i++) {

                        userFriendsReference.child("usersFriends").child(mAuth.getUid()).child(i + "")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        friendsId.add(dataSnapshot.getValue(String.class));

                                        Log.d("Friends", "FriendsId size: " + friendsId.size());
                                        if (friendsId.size() == count) {
                                            Log.d("Friends", "FriendsId: " + friendsId.get(0));
                                            getFriendsInfo(friendsId);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getFriendsInfo(ArrayList<String> friendsId) {
        Log.d("Friends", "FriendsId size1: " + friendsId.size());
        for (int i = 0; i < friendsId.size(); i++) {
            userDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
            userDatabaseReference.child(friendsId.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    firstNameFriends.add(user.getFirstName());
                    secondNameFriends.add(user.getLastName());
                    imageUrlFriends.add(user.getAvatar());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            int j = i;
            userFriendsReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    scoreFriends.add(dataSnapshot.child("usersScores").child(friendsId.get(j)).child("scores").getValue(Integer.class).toString());
                    if (friendsId.size() == scoreFriends.size()) {
                        friends();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }

    private void friends() {
        LinkedHashSet friends = new LinkedHashSet();
        int j = 0;
        for (int i = scoreFriends.size() - 1; i > -1; i--) {
            j++;
            String score = scoreFriends.get(i);
            String firstName = firstNameFriends.get(i);
            String secondName = secondNameFriends.get(i);
            String imageURL = imageUrlFriends.get(i);
            Log.d("Friends", "score " + score + " first " + firstName + " last " + secondName + " image " + imageURL);
            friends.add(new Top(j + "", firstName, secondName, imageURL, score));
        }
        scoreFriends.clear();
        firstNameFriends.clear();
        secondNameFriends.clear();
        imageUrlFriends.clear();
        this.friends.setValue(friends);
    }


    public void searchFriends(String searchText) {

        DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = mFirebaseDatabaseReference.child("users").orderByChild("firstName")
                .startAt(searchText).endAt(searchText + "\uf8ff");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Log.d("Search", "Id: " + postSnapshot.getKey());

                    firstNameSearch.add(postSnapshot.child("firstName").getValue(String.class));
                    secondNameSearch.add(postSnapshot.child("lastName").getValue(String.class));
                    imageUrlSearch.add(postSnapshot.child("avatar").getValue(String.class));
                    idSearch.add(postSnapshot.getKey());
                    Log.d("Search", "SIZE: " + firstNameSearch.size());
                    if (dataSnapshot.getChildrenCount() == firstNameSearch.size()) {
                        search();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        query.addValueEventListener(valueEventListener);
    }

    private void search() {
        LinkedHashSet search = new LinkedHashSet();

        for (int i = 0; i < idSearch.size(); i++) {
            String id = idSearch.get(i);
            String firstName = firstNameSearch.get(i);
            String secondName = secondNameSearch.get(i);
            String imageURL = imageUrlSearch.get(i);
            Log.d("Search", " first " + firstName + " last " + secondName + " image " + imageURL);
            search.add(new User(id, firstName, secondName, imageURL));
        }
        idSearch.clear();
        firstNameSearch.clear();
        secondNameSearch.clear();
        imageUrlSearch.clear();
        this.search.setValue(search);
    }

public void frinedsIdClear(){
        friendsId.clear();
    idSearch.clear();
    firstNameSearch.clear();
    secondNameSearch.clear();
    imageUrlSearch.clear();
}
    public void addFriend(String friendId) {
        if (friendsId.contains(friendId)) {
            Log.d("Add Friend", "Friend already added");

        } else {

            userFriendsReference = FirebaseDatabase.getInstance().getReference();
            userDatabaseReference = FirebaseDatabase.getInstance().getReference();


            userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count = dataSnapshot.child("usersFriends").child(mAuth.getUid()).child("friendsCount").getValue(Integer.class);
                    Log.d("Add friend", "Count " + count);


                    userFriendsReference.child("usersFriends").child(mAuth.getUid()).child(count + "").setValue(friendId);

                    userFriendsReference.child("usersFriends").child(mAuth.getUid()).child(count + "")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Log.d("Add friend", "add id: " + dataSnapshot.getValue(String.class));

                                    userFriendsReference.child("usersFriends").child(mAuth.getUid()).child("friendsCount").setValue(count + 1);

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
    }


}
