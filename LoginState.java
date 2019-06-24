package cn.edu.swufe.eatingbar;

public class LoginState {
    public static String username = null;
    public LoginState() {
        super();
        // TODO Auto-generated constructor stub
    }
    public LoginState( String username){
        super();
        username = this.username;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        LoginState.username = username;
    }
}
