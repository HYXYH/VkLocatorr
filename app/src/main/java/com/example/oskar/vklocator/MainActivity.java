package com.example.oskar.vklocator;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.oskar.vklocator.test.api.Api;
import com.example.oskar.vklocator.test.api.KException;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends Activity {


    private final int REQUEST_LOGIN = 1;
    private static final int REQUEST_CAMERA = 2;
    private static final int REQUEST_SELECT_PHOTO = 3;
    public static String API_ID="2904017";
    public static Account account = new Account();
    Api api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }



    public void logIn(View v) throws JSONException, IOException, KException {
        ArrayList<Long> test;
        account.restore(this);

        //Если сессия есть создаём API для обращения к серверу
        if( account.access_token != null ) {
            api = new Api(account.access_token, API_ID);
            startActivity(new Intent(this, MapPane.class));

            //postToWall();
            //Данные юзера есть, можно постить на стену
            //api.createWallPost(1L, "test", null, null, false, false, false, null, null, null, 0L, null, null);

        } else {
            Intent intent = new Intent();
            intent.setClass(this, LoginWebActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        }

    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch( requestCode ) {
            //Получили авторизацию от контакта
            case REQUEST_LOGIN:
                if( resultCode == RESULT_OK ) {
                    //авторизовались успешно
                    account.access_token = data.getStringExtra("token");
                    account.user_id = data.getLongExtra("user_id", 0);
                    account.save(MainActivity.this);
                    api = new Api(account.access_token, API_ID);
                    startActivity(new Intent(this, MapPane.class));
                }
                break;
            default:
                Log.d("rez code:"," rezult code" + resultCode);
        }
    }



    /** Called when the user touches the button */
    public void GoToMap(View view) {

        Animation shake = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_bottom);
        view.startAnimation(shake);
        try {
            logIn(view);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KException e) {
            e.printStackTrace();
        }



    }


}
