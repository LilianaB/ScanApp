package com.example.txitax.scanbotwrapper;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.scanbotwrapper.CameraFragment;

public class MainActivity extends AppCompatActivity  implements CameraFragment.OnFragmentInteractionListener {

    private final String TAG = MainActivity.class.getSimpleName();
    private Bitmap mPictureTaken = null;
    private Button mCameraButton = null;
    private Button mFlashButton = null;
    private CameraFragment mCameraFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCameraFragment = (CameraFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentDemo);

        Log.d(TAG, (mCameraFragment == null) ? "true" : "false");
        mCameraButton = (Button)findViewById(R.id.snap);
        mFlashButton = (Button)findViewById(R.id.flash);

        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraFragment.onClickCameraButton();

            }
        });

        mFlashButton.setOnClickListener(new View.OnClickListener() {
            boolean flashEnabled = false;
            @Override
            public void onClick(View v) {
                mCameraFragment.onClickFlashButton(!flashEnabled);
                flashEnabled = !flashEnabled;
            }
        });
    }

    @Override
    public void getImage(Bitmap imageTaken) {
        Log.d(TAG, "Got image " + imageTaken.toString());
        mPictureTaken = imageTaken;

    }
}
