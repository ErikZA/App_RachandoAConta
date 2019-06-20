package com.erikzambeligmail.rachandoaconta.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.erikzambeligmail.rachandoaconta.R;
import com.erikzambeligmail.rachandoaconta.modelo.Conta;
import com.erikzambeligmail.rachandoaconta.modelo.Item;
import com.erikzambeligmail.rachandoaconta.persistencia.DataBaseHelper;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Lista_Itens_Activity extends AppCompatActivity {

    public static final String CONTA    = "CONTA";
    public static final int RETORNO    = 1;
    public static final String NOME    = "NOME";
    private ConstraintLayout constraintLayout;
    private int ID;
    private List<Item> list = null;
    private String itemNome= "", itemValor ="";
    private ListView listView;
    public MeuAdapter meuAdapter;
    private Conta conta;
    private TextView textValorDividido, textoValoIteiro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista__itens_);
        constraintLayout = (ConstraintLayout) findViewById(R.id.listaActivityID);
        textValorDividido = (TextView) findViewById(R.id.textViewValorDividido);
                textoValoIteiro = (TextView) findViewById(R.id.textViewValorTotal);
        pegaDados();
        recuperaConta();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        populaLista();
        meuAdapter = new MeuAdapter(this,list,this.conta);
        listView = (ListView) findViewById(R.id.listaItens);
        listView.setAdapter(meuAdapter);
        atualizaValores();

    }

    public void fechaConta(View view){
        if(!this.list.isEmpty()) {
            try {
                conta.setSituacao(false);
                DataBaseHelper conexao = DataBaseHelper.getInstance(this);
                conexao.getContaDao().update(conta);

            } catch (Exception e) {
                e.printStackTrace();
            }
            setResult(Activity.RESULT_CANCELED);
            finish();
        } else{
            Toast.makeText(this, getString(R.string.erro3),  Toast.LENGTH_SHORT).show();
        }
    }

    public void atualizaValores(){
        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("pt","BR"));
        numberFormat.setMaximumFractionDigits(2);
        conta.setValorFinal(0);
        textoValoIteiro.setText(getString(R.string.textoValorTotal));
        textValorDividido.setText(getString(R.string.valorDividido));
        for (int i = 0 ; i< list.size();i++)
            conta.setValorFinal( conta.getValorFinal()+list.get(i).getValorTotalItem());

        try {
            DataBaseHelper conexao = DataBaseHelper.getInstance(this);
            conexao.getContaDao().update(conta);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(conta.getAdicional()) {
            textoValoIteiro.setText(textoValoIteiro.getText().toString() + "" + numberFormat.format(conta.getValorFinal() + (conta.getValorFinal() * 10 / 100)));
            conta.setValorFinal(conta.getValorFinal() + (conta.getValorFinal() * 10 / 100));
            textValorDividido.setText(textValorDividido.getText()+" "+conta.getNumPessoas()+" : "+numberFormat.format(conta.getValorFinal()/conta.getNumPessoas()));
        } else {
            textValorDividido.setText(textValorDividido.getText()+" "+conta.getNumPessoas()+" : "+numberFormat.format(conta.getValorFinal()/conta.getNumPessoas()));
            textoValoIteiro.setText(textoValoIteiro.getText().toString()+" "+numberFormat.format(conta.getValorFinal()));
            conta.setValorFinal(conta.getValorFinal() + (conta.getValorFinal() * 10 / 100));
        }
    }
    private void pegaDados() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        constraintLayout.setBackgroundColor(bundle.getInt(PrincipalActivity.MODO));
        ID = bundle.getInt(PrincipalActivity.ID);
    }

    public void recuperaConta(){
        try{
        DataBaseHelper conexao = DataBaseHelper.getInstance(this);
        conta = conexao.getContaDao().queryForId(ID);
        } catch (SQLException e) {
           Toast.makeText(this, getString(R.string.erro) + getString(R.string.erro2)+e.getMessage(), Toast.LENGTH_SHORT).show();
         e.printStackTrace();
        }
        if(conta.getSituacao())
        setTitle(conta.getEstabelecimento()+" ("+getString(R.string.Aberta)+")");
        else
        setTitle(conta.getEstabelecimento()+" ("+getString(R.string.Fechada)+")");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.novo_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(conta.getSituacao()) {
            final EditText nomeItem = new EditText(this),
                    valorItem = new EditText(this);
            DialogInterface.OnClickListener listener =
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:

                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:

                                    itemNome = nomeItem.getText().toString().trim();
                                    itemValor = valorItem.getText().toString().trim();
                                    salvarItem();
                                    break;
                            }
                        }
                    };
            switch (item.getItemId()) {
                case R.id.menuItemNovoItem:
                    showInputDialogValor(this, valorItem, listener);
                    showInputDialog(this, nomeItem, listener);

                    return true;
                default:
                    if (item != null) {





                        Toast.makeText(this, R.string.titulo_confirma, Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_CANCELED);
                        finish();
                        return true;
                    }
                    return super.onOptionsItemSelected(item);
            }
        }else{
            item.setEnabled(false);
            return false;
        }
    }


    public static void showInputDialog(final Context context, EditText edt,DialogInterface.OnClickListener listener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(false);
        alertDialog.setTitle(R.string.tituloinformacao);
        alertDialog.setMessage(R.string.NomeItem);
        alertDialog.setView(edt);
        alertDialog.setPositiveButton("OK",listener);
        alertDialog.show();
    }

    public static void showInputDialogValor(final Context context, EditText edt,DialogInterface.OnClickListener listener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(false);
        alertDialog.setTitle(R.string.tituloinformacao);
        alertDialog.setMessage(R.string.ValorItem);
        alertDialog.setView(edt);
        alertDialog.setNegativeButton("OK",listener);
        alertDialog.show();
    }


    public void salvarItem(){
        if(itemNome.equalsIgnoreCase(" ")||
                itemNome.equalsIgnoreCase("")||
                itemNome == null) {
            Toast.makeText(this, getString(R.string.erro) + getString(R.string.erro2), Toast.LENGTH_SHORT).show();
        } else {
            try {
                DataBaseHelper conexao = DataBaseHelper.getInstance(this);
                float valorConvertido;
                valorConvertido = Float.parseFloat(itemValor);
                if(valorConvertido <= 0 || valorConvertido >= 99999)
                    throw new NumberFormatException();
                Item item = new Item();
                item.setDescricao(itemNome);
                item.setValor(valorConvertido);
                item.setValorTotalItem(0);
                item.setQuantidade(0);
                item.setConta(conta);
                conexao.getItemDao().create(item);
                list.add(item);
           } catch (SQLException e) {
                Toast.makeText(this, getString(R.string.erro)  + getString(R.string.erro2)+e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (NumberFormatException ex) {
                Toast.makeText(this, getString(R.string.erro)  + getString(R.string.erro2), Toast.LENGTH_SHORT).show();
            }
        }
        itemNome = "";
        itemValor = "";
        Intent intent = new Intent(this,PrincipalActivity.class);
        intent.putExtra(CONTA, conta.getId());
        intent.putExtra(NOME,conta.getEstabelecimento());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void populaLista(){

        try {
            DataBaseHelper conexao = DataBaseHelper.getInstance(this);

            list = conexao.getItemDao()
                    .queryBuilder()
                    .where().eq(Item.CONTA_ID, ID)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

