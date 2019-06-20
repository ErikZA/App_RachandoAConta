package com.erikzambeligmail.NewRachandoConta.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.erikzambeligmail.NewRachandoConta.R;
import com.erikzambeligmail.NewRachandoConta.modelo.Conta;

import java.util.List;

/**
 * Created by erik_ on 19/11/2017.
 */

    public class MeuAdapterContas extends ArrayAdapter<Conta> {

        private Context context;
        private List<Conta> lista;
        private String Posicao;
        private Conta posicao;


        public MeuAdapterContas(Context context, List<Conta> lista){
            super(context,0,lista);
            this.context = context;
            this.lista = lista;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            posicao = this.lista.get(position);
            convertView = LayoutInflater.from(this.context).inflate(R.layout.infla_lista_contas,null);
            TextView text = (TextView) convertView.findViewById(R.id.textViewNomeEstabelicimento);
            text.setText(posicao.getEstabelecimento());
            Spinner situacao = (Spinner) convertView.findViewById(R.id.spinner3);
            if(posicao.getSituacao())
                situacao.setSelection(0);
            else
                situacao.setSelection(1);
            situacao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Posicao = parent.getItemAtPosition(position).toString();
                    if (Posicao.equalsIgnoreCase("Aberta")|| Posicao.equalsIgnoreCase("open") ) {
                        posicao.setSituacao(true);
                    } else if (Posicao.equalsIgnoreCase("Fechada")||Posicao.equalsIgnoreCase("closed")) {
                        posicao.setSituacao(false);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            return convertView;
        }



}
