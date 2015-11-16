package Local_Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.syd.hand_shanwei_2.R;

/**
 * Created by syd on 2015/11/15.
 */
public class UserinfoUtils {
    public static final String BE_ON="be_on";
    public static final String USERID="userid";
    public static final String USERPASSWORD="userpassword";
    //本地信息存储
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public UserinfoUtils(Context context){
        sharedPreferences=context.getSharedPreferences(context.getResources().getString(R.string.app_name_eng), Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }
    //更新登陆状态
    public void refresh_Login_Status(boolean be_on){
        editor.putBoolean(BE_ON,be_on);
        editor.commit();
    }
    //获取登录状态
    public boolean get_Login_Status(){

        return sharedPreferences.getBoolean(BE_ON,false);

    }
    //存储当前用户名和密码
    public void save_CureentLogin_Info(String Id,String password){
        editor.putString(USERID,Id);
        editor.putString(USERPASSWORD,password);
        editor.commit();
    }
    //获取上一次登陆的用户账号
    public String get_LastId(){
        return sharedPreferences.getString(USERID,null);
    }
    //获取上次登录的用户密码
    public String get_LastPassword(){
        return sharedPreferences.getString(USERPASSWORD,null);
    }

}
