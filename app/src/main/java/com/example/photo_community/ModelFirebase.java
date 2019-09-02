package com.example.photo_community;


import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.LinkedList;

import javax.annotation.Nullable;

public class ModelFirebase {
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    public ModelFirebase(){
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false).build();
        db.setFirestoreSettings(settings);
        mAuth = FirebaseAuth.getInstance();
    }

    public void uploadImage(Bitmap imageBmp , final Model.SaveImageListener listener)
    {
        if (imageBmp==null)
        {
            listener.complete(null);
        }
        else
        {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            Date d = new Date();
            StorageReference imagesRef = storage.getReference().child("images").child("image_" + d.getTime() + "jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = imagesRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception exception) {
                    listener.fail(exception);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            listener.complete(uri.toString());

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.fail(e);
                        }
                    });

                }
            });
        }

    }

    //users!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public String getCurrentUserid(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
            return currentUser.getUid();
        else
            return null;
    }
    public void register(String email, String password, Model.basicListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                listener.onSuccess(mAuth.getUid());
            }
        });

    }
    public void login(String email, String password, Model.basicListener listener){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if(authResult != null) {
                            listener.onSuccess(authResult.getUser().getUid());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure(e.getMessage().replace("[","").replace("]",""));
            }
        });

    }
    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }


    public void getUserById(String id, Model.getUserListener listener)
    {
        db.collection("Users").document(id).get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onError(e);
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                listener.onComplete(documentSnapshot.toObject(User.class));
            }
        });
    }
    public void addUser(String id, String email, String password, String uri, Model.addUserListener listener) {
        User user = new User(id, uri, email, password);


        db.collection("Users").document(id).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    listener.onComplete(user);

                } else {
                    Log.e("TAG", task.getResult().toString());

                }
            }
        });
    }









    //Posts!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    public void getAllPosts(Model.GetAllPostsListener listener ) {
        //if no conection
            //onOfline();
        db.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                LinkedList<Post> data = new LinkedList<>();
                if (e != null) {
                    listener.onComplete(data);
                    return;
                }
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Post post = doc.toObject(Post.class);
                    data.add(post);
                }
                listener.onComplete(data);
            }
        });

    }
    public void getAllPostsByUserId(Model.GetAllPostsListener listener) {
        db.collection("Posts").whereEqualTo("userId", MyApp.getCurrentUserId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                LinkedList<Post> data = new LinkedList<>();
                if (e != null) {
                    listener.onComplete(data);
                    return;
                }
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Post post = doc.toObject(Post.class);
                    data.add(post);
                }
                listener.onComplete(data);
            }
        });
    }

    public void addPost(Post post, Model.addPostListener listener) {
        String key = db.collection("Comments").document().getId();
        post.setPostKey(key);
        db.collection("Posts").document(post.getPostKey()).set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onComplete(post);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onError(e);
            }
        });
    }
    public void deletePost(Post post) {
        db.collection("Posts").document(post.getPostKey()).set(post);

    }

    public void updatePost(Post post, Model.basiconCompleteListener listener) {
        db.collection("Posts").document(post.getPostKey()).set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onComplete(true);
            }
        });
    }

    //comments!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public String addComment(Comment comment, Model.StoreToLocalListener listener){
        String postKey = comment.getPostKey();
        String key = db.collection("Comments").document().getId();
        comment.setCommentId(key);
        db.collection("Comments").document(key).set(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onComplete(key);
            }
        });
        return key;
    }

    public void getAllcommentsOfPosts(String postKey, Model.GetAllCommentsOfPostListener listener ) {
        db.collection("Comments").whereEqualTo("postKey", postKey).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                LinkedList<Comment> data = new LinkedList<>();
                if (e != null) {
                    listener.onComplete(data);
                    return;
                }
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Comment comment = doc.toObject(Comment.class);
                    data.add(comment);
                }
                listener.onComplete(data);
            }
        });

    }

    public void deleteAllCommentsByPostKey(String postKey, Model.basiconCompleteListener listener) {
        db.collection("Comments").document(postKey).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onComplete(true);
            }
        });
    }

}
