package swe2022.team6.skkumap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import swe2022.team6.skkumap.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private FirebaseAuth mAuth;

    public String phoneNum = null;
    public String userInputCode = null;
    private String validCode = null;
    PhoneAuthProvider.ForceResendingToken resendToken;

    private final Pattern phoneNumPattern = Pattern.compile("^010-(\\d{4})-(\\d{4})$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent gotoMain = new Intent(this, MainActivity.class);
        //startActivity(gotoMain);

        binding.tv2.setVisibility(View.INVISIBLE);
        binding.etUserTypedCode.setVisibility(View.INVISIBLE);
        binding.btnEnterCode.setVisibility(View.INVISIBLE);

        binding.etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String userTypedNumber = binding.etPhoneNumber.getText().toString();
                Matcher matcher = phoneNumPattern.matcher(userTypedNumber);
                if (matcher.find()) {
                    binding.tv1.setText("Well Done.");
                    validPhoneNumberTyped();
                }
                else {
                    binding.tv1.setText("Enter Phone number in valid format!! (010-0000-0000)");
                }
            }
        });

        binding.btnEnterCode.setOnClickListener(v -> {
            userInputCode = binding.etUserTypedCode.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(validCode, userInputCode);
            signInWithPhoneAuthCredential(credential);
            Toast.makeText(LoginActivity.this, userInputCode, Toast.LENGTH_SHORT).show();
        });

    }

    private void validPhoneNumberTyped() {
        binding.tv2.setVisibility(View.VISIBLE);
        binding.etUserTypedCode.setVisibility(View.VISIBLE);
        binding.btnEnterCode.setVisibility(View.VISIBLE);

        String userTypedPhoneNumber = binding.etPhoneNumber.getText().toString();
        phoneNum = "+8210" + userTypedPhoneNumber.substring(4, 8) + userTypedPhoneNumber.substring(9);
        Toast.makeText(LoginActivity.this, phoneNum, Toast.LENGTH_LONG).show();
        startVerification();
    }

    private void startVerification() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("kr");
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNum)
                .setTimeout(20L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        validCode = verificationId;
                        resendToken = forceResendingToken;
                        Toast.makeText(LoginActivity.this, "SMS was sent", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Toast.makeText(LoginActivity.this, "SMS sending succeed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(LoginActivity.this, "SMS sending failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String verificationId) {
                        Toast.makeText(LoginActivity.this, "Time Out. " + verificationId, Toast.LENGTH_LONG).show();
                    }
                }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Yes!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("loggedUserPhoneNumber", phoneNum);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "No!", Toast.LENGTH_SHORT).show();

                            binding.tv2.setVisibility(View.INVISIBLE);
                            binding.etUserTypedCode.setVisibility(View.INVISIBLE);
                            binding.btnEnterCode.setVisibility(View.INVISIBLE);

                            binding.etPhoneNumber.setText("");
                            binding.tv1.setText("try it again.");
                        }
                    }
                });
    }
}