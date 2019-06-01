package com.erikzambeligmail.rachandoaconta.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.erikzambeligmail.rachandoaconta.R;

public class PrincipalActivity extends AppCompatActivity {

    public static final String MODO = "MODO";
    public static final String ID = "ID";
    public static final String ALTERARNOME = "ALTERARNOME";

    private ConstraintLayout constraintLayout;
    private int opcao = Color.GRAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);


        constraintLayout = (ConstraintLayout) findViewById(R.id.principalActivity);

        lerPreferenciaCor();
    }

    private void mudarCorFundo() {
        constraintLayout.setBackgroundColor(opcao);
    }


    private void lerPreferenciaCor() {
        SharedPreferences sharedPref =
                getSharedPreferences(getString(R.string.prefencias_cores),
                        Context.MODE_PRIVATE);

        opcao = sharedPref.getInt(getString(R.string.cor_fundo), opcao);

        mudarCorFundo();
    }

    private void salvarPreferenciaCor(int novoValor) {
        SharedPreferences sharedPref =
                getSharedPreferences(getString(R.string.prefencias_cores),
                        Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(getString(R.string.cor_fundo), novoValor);

        editor.commit();

        opcao = novoValor;

        mudarCorFundo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_app, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        switch (opcao) {
            case Color.LTGRAY:
                menu.getItem(0).setChecked(true);
                menu.getItem(2).setChecked(false);
                return true;
            case Color.GRAY:
                menu.getItem(2).setChecked(true);
                menu.getItem(0).setChecked(false);
                return true;
            default:
                return false;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.menuItemInformacao:
                irParaInfo();
                return true;
            case R.id.itemMenuTema1:
                salvarPreferenciaCor(Color.LTGRAY);
                return true;
            case R.id.itemMenuTema2:
                salvarPreferenciaCor(Color.GRAY);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void irParaInfo() {
        Intent intent = new Intent(this, InfoActivity.class);

        intent.putExtra(MODO, opcao);

        startActivity(intent);
    }

    public void irParaListaContas(View view) {
        Intent intent = new Intent(this, Lista_Contas_Activity.class);

        intent.putExtra(MODO, opcao);

        startActivityForResult(intent,ContaActivity.RETORNO);
    }

    public void novaConta(View view) {
        Intent intent = new Intent(this, ContaActivity.class);
        intent.putExtra(MODO, opcao);
        startActivityForResult(intent, ContaActivity.RETORNO);
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode,
                resultCode,
                data);

        if (requestCode == ContaActivity.RETORNO &&
                resultCode == Activity.RESULT_OK) {

            Bundle bundle = data.getExtras();

            int contaID = bundle.getInt(ContaActivity.CONTA);
            String nome = bundle.getString(ContaActivity.NOME);
            Intent intent = new Intent(this, Lista_Itens_Activity.class);
            intent.putExtra(MODO, opcao);
            intent.putExtra(ID, contaID);
            intent.putExtra(ALTERARNOME,nome);
            startActivityForResult(intent,ContaActivity.RETORNO);

        }

        if (requestCode == Lista_Contas_Activity.RETORNO &&
                resultCode == Activity.RESULT_OK) {

            Bundle bundle = data.getExtras();

            int contaID = bundle.getInt(Lista_Contas_Activity.CONTA);
            String nome = bundle.getString(Lista_Contas_Activity.NOME);
            Intent intent = new Intent(this, Lista_Itens_Activity.class);
            intent.putExtra(MODO, opcao);
            intent.putExtra(ID, contaID);
            intent.putExtra(ALTERARNOME,nome);
            startActivityForResult(intent,ContaActivity.RETORNO);

        }
    }


}
