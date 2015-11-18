package com.example.syd.hand_shanwei_2.Local_Utils;

/**
 * Created by presisco on 2015/11/17.
 */
public class FloorName2ID {
    public static String getID(String floorName)
    {
        String id="000";
        switch(floorName)
        {
            case "三楼":id+="103";break;
            case "四楼":id+="104";break;
            case "五楼":id+="105";break;
            case "六楼":id+="106";break;
            case "七楼":id+="107";break;
            case "八楼":id+="108";break;
            case "九楼":id+="109";break;
            case "十楼":id+="110";break;
            case "十一楼":id+="111";break;
            case "十二楼":id+="112";break;
            case "图东环楼三楼":id+="203";break;
            case "图东环楼四楼":id+="204";break;
            default:id+="000";
        }
        return id;
    }
}
