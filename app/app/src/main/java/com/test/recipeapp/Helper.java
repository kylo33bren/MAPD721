package com.test.recipeapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    private static ProgressDialog progressDialog;
    public static boolean isEmailValid(String email)
    {
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        final Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void showLoader(Context con, String msg)
    {
        progressDialog = new ProgressDialog(con);
        progressDialog.setMessage(msg);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    public static void stopLoader()
    {
        if (progressDialog != null)
        {

            progressDialog.cancel();
            progressDialog = null;

        }
    }

    public static void PutData(Context context, String key, String value)
    {
        try
        {
            SharedPreferences sharedpreferences = context.getSharedPreferences("shhh", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(key, value);
            editor.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String GetData(Context context, String key)
    {
        try
        {
            SharedPreferences sharedpreferences = context.getSharedPreferences("shhh", Context.MODE_PRIVATE);
            return sharedpreferences.getString(key, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }



}
