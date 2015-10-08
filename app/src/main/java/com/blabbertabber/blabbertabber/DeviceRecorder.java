package com.blabbertabber.blabbertabber;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by cunnie on 10/3/15.
 * <p/>
 * Class that works with a REAL microphone (non-emulator)
 */
public class DeviceRecorder extends Recorder {
    private MediaRecorder mRecorder;

    public DeviceRecorder(Context context) {
        super(context);
    }

    @Override
    protected void startRecording() {
        Log.i(TAG, "startRecording()");
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(new BestMicrophone().getBestMicrophone());
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile("/dev/null");
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
        mRecorder.start();

    }

    @Override
    protected void stopRecording() {
        Log.i(TAG, "stopRecording()");

        try {
            mRecorder.stop();
        } catch (RuntimeException e) {
            Log.wtf(TAG, "MediaRecorder.stop() Runtime Exception");
            // TODO: (when we record to a file instead of /dev/null
            // "clean up the output file (delete the output file, for instance), since the output
            // file is not properly constructed when this happens."
        }
        mRecorder.release();
        mRecorder = null;
    }

    @Override
    public int getSpeakerVolume() {
        int volume = mRecorder.getMaxAmplitude();
        volume = volume * 100 / 32768;
        Log.i(TAG, "getSpeakerVolume() volume is " + volume);
        return volume;
    }
}
