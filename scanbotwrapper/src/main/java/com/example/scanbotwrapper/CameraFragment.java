package com.example.scanbotwrapper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.doo.snap.ScanbotSDK;
import net.doo.snap.camera.AutoSnappingController;
import net.doo.snap.camera.ContourDetectorFrameHandler;
import net.doo.snap.camera.PictureCallback;
import net.doo.snap.camera.ScanbotCameraView;
import net.doo.snap.lib.detector.ContourDetector;
import net.doo.snap.lib.detector.DetectionResult;
import net.doo.snap.ui.PolygonView;

import java.util.List;

import static com.example.scanbotwrapper.R.id.result;
import static net.doo.snap.lib.detector.ContourDetector.IMAGE_FILTER_COLOR_ENHANCED;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CameraFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment implements PictureCallback {

    private ScanbotCameraView cameraView;
    private ImageView resultView;
    private ScanbotSDK scanbotSDK;
    private static final String TAG =  CameraFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    public CameraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        scanbotSDK = new ScanbotSDK(getActivity());
        cameraView = (ScanbotCameraView) view.findViewById(R.id.camera);
        resultView = (ImageView) view.findViewById(result);

        Log.d(TAG, "init camera fragment");


        ContourDetectorFrameHandler contourDetectorFrameHandler = ContourDetectorFrameHandler.attach(cameraView);

        PolygonView polygonView = (PolygonView) view.findViewById(R.id.polygonView);
        contourDetectorFrameHandler.addResultHandler(polygonView);

        /*contourDetectorFrameHandler.addResultHandler(new ContourDetectorFrameHandler.ResultHandler() {

            @Override
            public boolean handleResult(ContourDetectorFrameHandler.DetectedFrame result) {
                // Handle result here
                Log.d(TAG,result.detectionResult.name());
                //TODO give feedback to user, similar way to ios version.
                return false; // typically you need to return false
            }

        });*/

        AutoSnappingController.attach(cameraView, contourDetectorFrameHandler);

        cameraView.addPictureCallback(this);

    }


    @Override
    public void onResume() {
        super.onResume();
        cameraView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraView.onResume();
    }

    @Override
    public void onPictureTaken(byte[] image, int imageOrientation) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        final Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length, options);

        ContourDetector detector = new ContourDetector();
        DetectionResult resultContourDetector = detector.detect(bitmap);  // use Bitmap or byte[]
        List<PointF> polygon = detector.getPolygonF();

        final Bitmap resultCropped = detector.processImageF(bitmap, polygon, IMAGE_FILTER_COLOR_ENHANCED);

        /*PageFactory pageFactory = scanbotSDK.pageFactory();
        try {
            Page page = pageFactory.buildPage(bitmap, 5, 5).page;
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        resultView.post(new Runnable() {
            @Override
            public void run() {
                resultView.setImageBitmap(resultCropped);
                mListener.getImage(resultCropped);
                cameraView.startPreview();
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void getImage(Bitmap imageTake);
    }

    public void onClickCameraButton() {
        cameraView.takePicture(false);
    }

    public void onClickFlashButton(boolean flashEnabled) {
        cameraView.useFlash(flashEnabled);
    }
}
