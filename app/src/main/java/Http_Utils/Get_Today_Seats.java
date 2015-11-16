package Http_Utils;

import android.content.Context;

/**
 * Created by syd on 2015/11/13.
 */
public class Get_Today_Seats {
    Context context;
    int all_Seats[],current_Seats[];
    //构造函数
    public Get_Today_Seats(Context context,int Floors){
        all_Seats=new int[Floors];
        current_Seats=new int[Floors];
    }
        //获取各楼层所有座位总数函数
    public int[] get_Allseats(){
        //// TODO: 2015/11/13
        return all_Seats;
    }
    //获取个楼层现剩余的座位
    public int[]get_Remainseats(){
        //// TODO: 2015/11/13
        return current_Seats;
    }
}
