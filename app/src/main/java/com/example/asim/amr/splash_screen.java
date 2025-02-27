package com.example.asim.amr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class splash_screen extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_screen);

        String uri = "@mipmap/logo_small";  // where myresource (without the extension) is the file
        String strVariant=getString(R.string.app_name);
        if (strVariant.equals("DP"))
            uri = "@mipmap/logo_small";
        else if (strVariant.equals("Cafe"))
            uri = "@mipmap/cafe_logo";
        else if (strVariant.equals("EventHall"))
            uri = "@mipmap/event_hall";

        int imageResource = getResources().getIdentifier(uri, null, getPackageName());

        ImageView imageview= (ImageView)findViewById(R.id.splashscreen);
        Drawable res = getResources().getDrawable(imageResource);
        imageview.setImageDrawable(res);


        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                //Intent mainIntent = new Intent(splash_screen.this,MainActivity.class);
                Intent mainIntent = new Intent(splash_screen.this,pos.class);
                splash_screen.this.startActivity(mainIntent);
                splash_screen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}