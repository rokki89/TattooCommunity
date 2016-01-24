package com.fedor.pavel.tattoocommunity.dialogs;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;


import com.fedor.pavel.tattoocommunity.R;



public class DialogManager {


    public static void showPhotoPikerDialog(Context context,DialogInterface.OnClickListener listener,String title){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);

        builder.setItems(context.getResources().getStringArray(R.array.photoPikerDialog),listener);

        AlertDialog dialog = builder.create();

        dialog.show();

    }


    public static void showChoiceDialog(Context context,String message,DialogInterface.OnClickListener listener){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(message);

        builder.setPositiveButton("Да", listener);

        builder.setNegativeButton("Нет",listener);

        AlertDialog dialog = builder.create();

        dialog.show();

    }


}
