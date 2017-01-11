package pl.domino.dominiktomczykprojekt;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SendPicture extends AppCompatActivity {

    final static private String APP_KEY = "h3na9dmb3d1bbhi";
    final static private String APP_SECRET = "bdhz2r116je3dc6";
    AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
    final static private Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;
    private DropboxAPI<AndroidAuthSession> mDBApi;
    final String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";

    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_picture);






        File newdir = new File(directory);
        newdir.mkdirs();

        Button capture = (Button) findViewById(R.id.btnCapture);
        capture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                count++;
                String file = directory + count + ".jpg";
                File newfile = new File(file);
                try {
                    newfile.createNewFile();
                } catch (IOException e) {
                }

                Uri outputFileUri = Uri.fromFile(newfile);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        });




        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);



        Button sendButton = (Button) findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {



                mDBApi.getSession().startOAuth2Authentication(SendPicture.this);

                if (mDBApi.getSession().authenticationSuccessful()) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                String fileString = directory + count + ".jpg";
                                File file2 = new File(fileString);
                                FileInputStream inputStream = new FileInputStream(file2);
                                DropboxAPI.Entry response = mDBApi.putFileOverwrite(String.valueOf(count) + ".jpg", inputStream, file2.length(), null);
                                Log.i("DbExampleLog", "The uploaded file's rev is: " + response.rev);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    thread.start();
                }




            }
        });
    }

    protected void onResume() {
        super.onResume();

        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                mDBApi.getSession().finishAuthentication();
                String accessToken = mDBApi.getSession().getOAuth2AccessToken();
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }


}
