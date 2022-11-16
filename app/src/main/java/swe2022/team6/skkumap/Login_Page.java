package swe2022.team6.skkumap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login_Page extends AppCompatActivity {
    public Button Signin_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        Signin_button = (Button) findViewById(R.id.button_signin);

        Signin_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent( Login_Page.this, Signin_Page.class );
                startActivity(intent);
            }
        });
    }
}