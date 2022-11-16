package swe2022.team6.skkumap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Signin_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_page);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}