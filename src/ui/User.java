package ui;

/**
 * Created by Duane on 07/08/2016.
 */
public class User {
    private String username;

    private User(){

    }

    private static class SingletonHolder {
        private static final User INSTANCE = new User();
    }

    public static User getInstance(){
        return User.SingletonHolder.INSTANCE;
    }

    public synchronized void setUsername(String s){
        this.username = s;
    }

    public synchronized String getUsername(){
        return this.username;
    }

}
