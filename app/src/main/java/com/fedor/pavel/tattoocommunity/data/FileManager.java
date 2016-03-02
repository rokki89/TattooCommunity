package com.fedor.pavel.tattoocommunity.data;



import android.os.Environment;



import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileManager {


    public static File createFile() throws IOException{

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES),"TattooCommunity");

        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");


        return mediaFile;
    }


}
