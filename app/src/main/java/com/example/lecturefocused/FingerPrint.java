package com.example.lecturefocused;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class FingerPrint extends AppCompatActivity {
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    FrameLayout mScannerLayout;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);
        mScannerLayout=findViewById(R.id.Scanners);

        BiometricManager biometricManager =BiometricManager.from(this);
        switch (biometricManager.canAuthenticate())
        {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getApplicationContext(),"Device Does not have fineger print", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getApplicationContext(),"Finger print not woking", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(getApplicationContext(),"Fineger print not Assigned", Toast.LENGTH_SHORT).show();
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(this);

        biometricPrompt =new BiometricPrompt(FingerPrint.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                Toast.makeText(getApplicationContext(),"Finger print login Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FingerPrint.this, Scanner.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(),"Finger print Login Failed", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo =new BiometricPrompt.PromptInfo.Builder().setTitle("Lecture Focused")
                .setDescription("Use FingerPrint Authentication to verify your identity")
                .setDeviceCredentialAllowed(true).build();
        biometricPrompt.authenticate(promptInfo);
    }
}