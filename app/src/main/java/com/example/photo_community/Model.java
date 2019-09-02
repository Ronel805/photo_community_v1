package com.example.photo_community;

import android.graphics.Bitmap;

import java.util.List;

public class Model {
    final public static  Model instance = new Model();
    ModelSql modelSql;
    ModelFirebase modelFirebase;
    private  Model(){

        modelFirebase = new ModelFirebase();
        modelSql = new ModelSql();
    }

    interface SaveImageListener{
        public void fail(Exception e);
        public void complete(String uri);
    }
    interface addUserListener{
        public void onComplete(User user);
    }
    public interface addCommentListener{
        public void onComplete(Comment comment);
    }
    public interface addPostListener{
        void onComplete(Post post);
        void onError(Exception e);
    }




    //users!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public String getCurrentUserId(){
        return modelFirebase.getCurrentUserid();
    }

    public void register(String email, String password, basicListener listener){
        modelFirebase.register(email, password, listener);
    }
    public void addUser(final String id, final String email, final String password, Bitmap imageBitmap, final Model.basiconCompleteListener listener) {
        Model.instance.modelFirebase.uploadImage(imageBitmap, new SaveImageListener() {

            @Override
            public void fail(Exception e) {

            }

            @Override
            public void complete(String uri) {
                modelFirebase.addUser(id, email, password, uri, new addUserListener() {
                    @Override
                    public void onComplete(User user) {
                        UserAsyncDao.addUser(user, listener);

                    }
                });
            }
        });
    }

    public void getUserById(final String userId, final Model.getUserListener listener){
        modelFirebase.getUserById(userId, new getUserListener() {
            @Override
            public void onComplete(User u) {
                listener.onComplete(u);
            }

            @Override
            public void onError(Exception e) {

            }
        } );


    }

    public void login(String sEmail, String sPassword, basicListener listener) {
        modelFirebase.login(sEmail, sPassword, listener);
    }
    public void logout() {
        modelFirebase.logout();
    }

    public interface basicListener {
        public void onSuccess(String id);
        public void onFailure(String e);
    }

    //posts!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    public interface GetAllPostsListener{
        void onComplete(List<Post> data);
    }

    public interface GetAllCommentsOfPostListener{
        void onComplete(List<Comment> data);
    }

    public interface basiconCompleteListener{
        void onComplete(boolean done);
    }

    public interface StoreToLocalListener{
        void onComplete(String key);
    }

    //public boolean isOnline()
    //{
    //    ConnectivityManager
    //}

    public void getAllPosts(final GetAllPostsListener listener){
        PostAsyncDao.getAllPosts(new GetAllPostsListener() {
            @Override
            public void onComplete(List<Post> data) {
                listener.onComplete(data);
                modelFirebase.getAllPosts(new GetAllPostsListener() {
                    @Override
                    public void onComplete(List<Post> data) {
                        //listener.onComplete(data);
                        //PostAsynDao -> delete all
                        //PostAsyncDao - > insert all
                        PostAsyncDao.addPostsAndGetPostsList(data, new GetAllPostsListener() {
                            @Override
                            public void onComplete(List<Post> data) {
                                listener.onComplete(data);
                            }
                        });

                    }

                    //onOffline
                    //data FROMS SQl
                });

            }
        });
    }

    public void getAllPostsByUserId(final GetAllPostsListener listener) {
        PostAsyncDao.getAllPostsByUserId(MyApp.getCurrentUserId(), new GetAllPostsListener() {
            @Override
            public void onComplete(List<Post> data) {
                listener.onComplete(data);
                modelFirebase.getAllPostsByUserId(new GetAllPostsListener() {
                    @Override
                    public void onComplete(List<Post> data) {
                        PostAsyncDao.addPostsAndGetPostsListByUserId(data, new GetAllPostsListener() {
                            @Override
                            public void onComplete(List<Post> data) {
                                listener.onComplete(data);
                            }
                        });

                    }
                });

            }
        });
    }
    public  interface getUserListener
    {
        void onComplete(User u);
        void onError(Exception e);
    }
    public void addPost(final Post post, Bitmap imageBitmap, final addPostListener listener)
    {
        modelFirebase.uploadImage(imageBitmap, new SaveImageListener() {
            @Override
            public void fail(Exception e) {
                listener.onError(e);
            }

            @Override
            public void complete(final String uri) {
                modelFirebase.getUserById(post.getUserId(),new getUserListener() {
                    @Override
                    public void onComplete(User user) {
                        post.setUserPhoto(user.getProfileImage());
                        post.setPicture(uri);
                        post.setEmail(user.getEmail());
                        modelFirebase.addPost(post, new addPostListener() {
                            @Override
                            public void onComplete(Post post) {
                                PostAsyncDao.addPost(post, null);
                                listener.onComplete(post);
                            }

                            @Override
                            public void onError(Exception e) {
                                listener.onError(e);
                            }
                        });



                    }

                    @Override
                    public void onError(Exception e) {
                        listener.onError(e);
                    }
                });
            }
        });
    }
    public void deletePost(Post post) {
        post.setWasDeleted(true);
        modelFirebase.deletePost(post);

    }

    public void updatePost(final Post post, Bitmap imageBitmap, final basiconCompleteListener listener) {
        modelFirebase.uploadImage(imageBitmap, new SaveImageListener() {
            @Override
            public void fail(Exception e) {

            }

            @Override
            public void complete(final String uri) {
                modelFirebase.getUserById(post.getUserId(),new getUserListener() {
                    @Override
                    public void onComplete(User user) {
                        post.setUserPhoto(user.getProfileImage());
                        if (uri!=null)
                            post.setPicture(uri);
                        modelFirebase.updatePost(post, new basiconCompleteListener() {
                            @Override
                            public void onComplete(boolean done) {
                                if (done == true){
                                    PostAsyncDao.addPost(post, new basiconCompleteListener() {
                                        @Override
                                        public void onComplete(boolean done) {
                                            if (done == true){
                                                listener.onComplete(true);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        });

    }


    //comments!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void getAllcommentsOfPost(final String PostKey , final GetAllCommentsOfPostListener listener){


        CommentAsyncDao.getAllcommentsOfPost(PostKey, new GetAllCommentsOfPostListener() {
            @Override
            public void onComplete(List<Comment> data) {
                listener.onComplete(data);
                modelFirebase.getAllcommentsOfPosts(PostKey, new GetAllCommentsOfPostListener() {
                    @Override
                    public void onComplete(List<Comment> data) {
                        CommentAsyncDao.addCommentsAndGetCommentsaList(PostKey, data, new GetAllCommentsOfPostListener() {

                            @Override
                            public void onComplete(List<Comment> data) {
                                listener.onComplete(data);
                            }
                        });


                    }
                });

            }
        });
    }

    public void addComment(final Comment comment, final addCommentListener listener)
    {
        modelFirebase.getUserById(Model.instance.getCurrentUserId(), new getUserListener() {
            @Override
            public void onComplete(User u) {
                comment.setUimg(u.getProfileImage());
                comment.setUserEmail(u.getEmail());
                modelFirebase.addComment(comment, new StoreToLocalListener() {
                    @Override
                    public void onComplete(String key) {
                        comment.setCommentId(key);
                        CommentAsyncDao.addComment(comment);// maby add listener
                        listener.onComplete(comment);
                    }
                });
            }
            @Override
            public void onError(Exception e) {

            }
        });
    }

}