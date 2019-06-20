package com.erikzambeligmail.NewRachandoConta.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.erikzambeligmail.NewRachandoConta.R;
import com.erikzambeligmail.NewRachandoConta.modelo.Conta;
import com.erikzambeligmail.NewRachandoConta.persistencia.DataBaseHelper;

public class ContaActivity extends AppCompatActivity {

    public static final String CONTA    = "CONTA";
    public static final int RETORNO    = 0;
    public static final String NOME    = "NOME";



    private Spinner pessoas;
    private CheckBox adicional;
    private RadioGroup situacao;
    private EditText nome;
    private String Posicao;
    private Conta conta;
    private  int meio;
    private ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);
        setTitle(R.string.TituloNovaConta);
        constraintLayout = (ConstraintLayout) findViewById(R.id.contaActivityID);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        meio = bundle.getInt(PrincipalActivity.MODO);
        mudarCorFundo(meio);

        pessoas = (Spinner) findViewById(R.id.spinnerPessoas);
        adicional = (CheckBox) findViewById(R.id.checkBoxDezPorCento);
        situacao = (RadioGroup) findViewById(R.id.radioGroup);
        nome = (EditText) findViewById(R.id.editTextNome);

        pessoas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Posicao = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void mudarCorFundo(int cor) {
        constraintLayout.setBackgroundColor(cor);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {
            Toast.makeText(this, R.string.titulo_confirma, Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void cancelar(View view){
        Toast.makeText(this, R.string.titulo_confirma, Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void salvar(View view){

        if (nome == null || nome.getText().toString().trim().equalsIgnoreCase("") || nome.getText().toString().trim().equalsIgnoreCase(" ")) {
            Toast.makeText(this,R.string.CampoVazio,Toast.LENGTH_SHORT).show();
            nome.requestFocus();
            return;
        }

        if (verificaRaioButton() == 0){
            Toast.makeText(this,R.string.CampoVazioRadio,Toast.LENGTH_SHORT).show();
            return;
        }
        conta = new Conta();
        if(verificaRaioButton() == 1){
            conta.setSituacao(true);
        } else if(verificaRaioButton() == 2){
            conta.setSituacao(false);
        }
        conta.setEstabelecimento(nome.getText().toString().trim());
        conta.setNumPessoas(Integer.parseInt(Posicao));
        conta.setAdicional(verificaCheckBox());
        conta.setValorFinal(0);

        try {

            DataBaseHelper conexao = DataBaseHelper.getInstance(this);
            conexao.getContaDao().create(conta);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, PrincipalActivity.class);
        intent.putExtra(CONTA,conta.getId());
        intent.putExtra(NOME,conta.getEstabelecimento());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public int verificaRaioButton() {
        switch (situacao.getCheckedRadioButtonId()) {
            case R.id.radioButtonAberta:
                return 1;
            case R.id.radioButtonFechada:
                return 2;
        }
        return 0;
    }

    public boolean verificaCheckBox() {
        if (adicional.isChecked()) {
            return true;
        } else {
            return false;
        }
    }
}
