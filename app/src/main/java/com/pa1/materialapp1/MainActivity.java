package com.pa1.materialapp1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    ImageView ivPic;
    Button btnTakePicture;
    Bitmap photo;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ivPic = findViewById(R.id.ivPic);
        btnTakePicture = findViewById(R.id.btnTakePicture);


        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,100);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Clicked on share ", Snackbar.LENGTH_LONG).show();
                Drawable mdr = ivPic.getDrawable();
                Bitmap bitm= ((BitmapDrawable)mdr).getBitmap();
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitm, " Material App ka pic", null);
                Uri uri = Uri.parse(path);
                Intent share= new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.putExtra("text","Material ka pic");
                startActivity(Intent.createChooser(share, "Share Image Via"));
            }
        });
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==100){
                photo = (Bitmap) data.getExtras().get("data");
                ivPic.setImageBitmap(photo);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            String state= Environment.getExternalStorageState();
            if(Environment.MEDIA_MOUNTED.equalsIgnoreCase(state)) {

                File root = Environment.getExternalStorageDirectory();
                File dir = new File(root + "/MaterialApp1");

                if (!dir.exists())
                    dir.mkdir();

                Random r = new Random(12);
                int rn = r.nextInt() * 12526 / 23;
                String fname = rn + ".jpg";

                File file = new File(dir,fname);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedOutputStream br = new BufferedOutputStream(fos);
                    photo.compress(Bitmap.CompressFormat.PNG,10,br);
                    Snackbar.make(findViewById(android.R.id.content),"Saved",Snackbar.LENGTH_LONG).show();
                    br.flush();
                    br.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"file not found",Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"IO Exception",Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(getApplicationContext(),"External Stroge issue",Toast.LENGTH_LONG).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }
}
