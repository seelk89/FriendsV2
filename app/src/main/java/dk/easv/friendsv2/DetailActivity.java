package dk.easv.friendsv2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    String TAG = MainActivity.TAG;

    EditText etName;
    EditText etPhone;
    CheckBox cbFavorite;

    int position;

    // Used for SMS, and Calling
    String phoneNumber = "";
    static int PERMISSION_REQUEST_CODE = 1;

    // Used for the BEFriends image
    File mFile;
    ImageView mImage;
    TextView mFilename;
    // cameraManager and cameraFacing is probably not needed
    CameraManager cameraManager;
    int cameraFacing;
    private final static String LOGTAG = "CamTag";
    private static int CAMERA_REQUEST_CODE = 4;
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "Detail Activity started");

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        cbFavorite = findViewById(R.id.cbFavorite);

        Bundle extras = getIntent().getExtras();
        position = (int) extras.get("position");
        setGUI(extras);

        //First row of buttons
        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCancel();
            }
        });
        findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickOK();
            }
        });

        //Second Row of buttons
        findViewById(R.id.btnUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBrowser();
            }
        });
        findViewById(R.id.btnPhone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCall();
            }
        });
        findViewById(R.id.btnSms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showYesNoDialog();
            }
        });
        findViewById(R.id.btnEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });

        // Image of the BEFriend
        mImage = findViewById(R.id.imgView);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openCameraIntent();
                onClickTakePics();
            }
        });

        mFilename = findViewById(R.id.txtFileName);
        mFilename.setBackgroundColor(Color.LTGRAY);

        setup();
    }

    private void setGUI(Bundle data) {
        etName.setText(data.get("name").toString());
        etPhone.setText(data.get("phone").toString());
        phoneNumber = data.get("phone").toString();
        cbFavorite.setChecked((boolean) data.get("favorite"));
        // mFile = new File(data.get("imageStorageLocation").toString());
    }

    /*
    private void getImageFromStorage() {
        File imgFile = new  File("/sdcard/Images/test_image.jpg");

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);

            myImage.setImageBitmap(myBitmap);
        }
    }
    */

    private void onClickCancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void onClickOK() {
        Intent data = new Intent();
        data.putExtra("position", position);
        data.putExtra("newName", etName.getText().toString());
        data.putExtra("newPhoneNumber", etPhone.getText().toString());
        // data.putExtra("newImageStorageLocation", mFile.getAbsolutePath());
        setResult(RESULT_OK, data);
        finish();
    }

    private void startBrowser() {
        String url = "http://www.google.com";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void makeCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    private void startSMSActivity() {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:" + phoneNumber));
        sendIntent.putExtra("sms_body", "Hi, it goes well on the android course...");
        startActivity(sendIntent);
    }

    private void showYesNoDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("SMS Handling");

        alertDialogBuilder
                .setMessage("Click Direct if SMS should be send directly. Click Start to start SMS app...")
                .setCancelable(true)
                .setPositiveButton("Direct", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendSMS();
                    }
                })
                .setNegativeButton("Start", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startSMSActivity();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    private void sendSMS() {
        Toast.makeText(this, "An sms will be send", Toast.LENGTH_LONG).show();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d(TAG, "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                return;

            } else
                Log.d(TAG, "permission to SEND_SMS granted!");
        }

        SmsManager m = SmsManager.getDefault();
        String text = "Hi, it goes well on the android course...";
        m.sendTextMessage(phoneNumber, null, text, null, null);
    }

    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        String[] receivers = {"seelk.89@gmail.com"};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, receivers);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Test");
        emailIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey, Hope that it is ok, Best Regards android...");
        startActivity(emailIntent);
    }

    // Not used anymore
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent,
                    CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE &&
                resultCode == RESULT_OK) {
            showPictureTaken(mFile);
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Canceled...", Toast.LENGTH_LONG).show();
            return;

        } else
            Toast.makeText(this, "Picture NOT taken - unknown error...", Toast.LENGTH_LONG).show();
        /*
        if (data != null && data.getExtras() != null) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            mImage.setImageBitmap(imageBitmap);

            mFile = getOutputMediaFile();

            if (mFile == null)
            {
                Toast.makeText(this, "Could not create file...", Toast.LENGTH_LONG).show();
                return;
            }
        }
        */
    }

    private void setup() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        cameraFacing = CameraCharacteristics.LENS_FACING_BACK;
    }

    private void showPictureTaken(File f) {
        mImage.setImageURI(Uri.fromFile(f));
        mImage.setBackgroundColor(Color.RED);
        mFilename.setText(f.getAbsolutePath());
    }

    private void onClickTakePics() {
        mFile = getOutputMediaFile(); // create a file to save the image
        if (mFile == null) {
            Toast.makeText(this, "Could not create file...", Toast.LENGTH_LONG).show();
            return;
        }
        // create Intent to take a picture
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));

        Log.d(LOGTAG, "file uri = " + Uri.fromFile(mFile).toString());

        if (intent.resolveActivity(getPackageManager()) != null) {
            Log.d(LOGTAG, "camera app will be started");
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } else
            Log.d(LOGTAG, "camera app could NOT be started");

    }

    private File getOutputMediaFile() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "images");
            directory.mkdirs();


            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), getResources().getString(R.string.app_name));


            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Toast.makeText(this, "Could not create directory...", Toast.LENGTH_LONG).show();
                    Log.d(LOGTAG, "failed to create directory");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String postfix = "jpg";
            String prefix = "IMG";

            File mediaFile = new File(mediaStorageDir.getPath() +
                    File.separator + prefix +
                    "_" + timeStamp + "." + postfix);

            return mediaFile;
        }

        Log.d(LOGTAG, "Permission for writing NOT granted");
        return null;
    }
}
