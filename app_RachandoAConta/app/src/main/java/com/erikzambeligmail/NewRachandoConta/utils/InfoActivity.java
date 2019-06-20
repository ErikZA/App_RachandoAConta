package com.erikzambeligmail.NewRachandoConta.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.erikzambeligmail.NewRachandoConta.R;

public class InfoActivity extends AppCompatActivity {

    private TextView mostrarTexto;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mostrarTexto = (TextView) findViewById(R.id.textViewInfo);
        constraintLayout = (ConstraintLayout) findViewById(R.id.activityInfoID);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        int meio = bundle.getInt(PrincipalActivity.MODO);
        mudarCorFundo(meio);
        mostrarTexto.setText(getString(R.string.info_iniciais));
    }

    private void mudarCorFundo(int cor) {
        constraintLayout.setBackgroundColor(cor);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {
            setResult(Activity.RESULT_OK);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
