package com.erikzambeligmail.NewRachandoConta.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.erikzambeligmail.NewRachandoConta.R;
import com.erikzambeligmail.NewRachandoConta.modelo.Conta;
import com.erikzambeligmail.NewRachandoConta.persistencia.DataBaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Lista_Contas_Activity extends AppCompatActivity {

    public static final String CONTA    = "CONTA";
    public static final int RETORNO    = 1;
    public static final String NOME    = "NOME";

    private int opcao;
    private ArrayAdapter<Conta> listaAdapterB;

    private ConstraintLayout constraintLayout;
    ListView listView;
    //MeuAdapterContas listaAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista__contas_);
        constraintLayout = (ConstraintLayout) findViewById(R.id.layoutListaContas);
        listView = (ListView) findViewById(R.id.listViewContas);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Conta conta = (Conta) parent.getItemAtPosition(position);

                abreAcrtivity(conta);
            }
        });
        pegaDados();
        popularLista();
        registerForContextMenu(listView);
    }

    private void abreAcrtivity(Conta conta){
        Intent intent = new Intent(this,PrincipalActivity.class);
        intent.putExtra(CONTA, conta.getId());
        intent.putExtra(NOME,conta.getEstabelecimento());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void popularLista(){

        List<Conta> lista = null;

        try {
            DataBaseHelper conexao = DataBaseHelper.getInstance(this);

            lista = conexao.getContaDao()
                    .queryBuilder()
                    .orderBy(Conta.ESTABELECIMENTO, true)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(Conta aux : lista){
            if(aux.getSituacao()){
                if(aux.getEstabelecimento().contains("(FECHADA)")){
                   String[] parser = aux.getEstabelecimento().split("-");
                    aux.setEstabelecimento(parser[0]+"-(ABERTA)");
                } else {
                    String[] parser = aux.getEstabelecimento().split("-");
                    aux.setEstabelecimento(parser[0] + "-(ABERTA)");
                }
            } else{
                if(aux.getEstabelecimento().contains("(ABERTA)")){
                    String[] parser = aux.getEstabelecimento().split("-");
                    aux.setEstabelecimento(parser[0]+"-(FECHADA)");
                } else {
                    String[] parser = aux.getEstabelecimento().split("-");
                    aux.setEstabelecimento(parser[0] + "-(FECHADA)");
                }
            }
            try {
                DataBaseHelper conexao =
                        DataBaseHelper.getInstance(this);
                conexao.getContaDao().update(aux);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //listaAdapter = new MeuAdapterContas(this,lista);
        listaAdapterB = new ArrayAdapter<Conta>(this,android.R.layout.simple_list_item_1,lista);
        listView.setAdapter(listaAdapterB);
        //listaOpcional = null;
    }

    private void pegaDados() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        constraintLayout.setBackgroundColor(bundle.getInt(PrincipalActivity.MODO));
        opcao = bundle.getInt(PrincipalActivity.MODO);
        setTitle(getString(R.string.TituloLstaContas));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.menu_excluir, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info;

        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Conta pos = (Conta) listView.getItemAtPosition(info.position);
        switch(item.getItemId()) {

            case R.id.itemMenuExcluir:
                try {
                    DataBaseHelper conexao =
                            DataBaseHelper.getInstance(this);

                    conexao.getContaDao().delete(pos);
                    listaAdapterB.remove(pos);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                popularLista();
                return true;
            case R.id.itemMenuAbrir:
                try {
                    DataBaseHelper conexao =
                            DataBaseHelper.getInstance(this);
                    pos.setSituacao(true);
                    conexao.getContaDao().update(pos);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                listaAdapterB.notifyDataSetChanged();
                popularLista();
                return true;
            case R.id.itemMenuFechar:
                try {
                    DataBaseHelper conexao =
                            DataBaseHelper.getInstance(this);
                    pos.setSituacao(false);
                    conexao.getContaDao().update(pos);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                listaAdapterB.notifyDataSetChanged();
                popularLista();
                return true;
            default:
                popularLista();
                return super.onContextItemSelected(item);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {
            setResult(Activity.RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
