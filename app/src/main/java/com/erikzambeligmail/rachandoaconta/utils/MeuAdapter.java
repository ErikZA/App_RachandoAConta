package com.erikzambeligmail.rachandoaconta.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.erikzambeligmail.rachandoaconta.R;
import com.erikzambeligmail.rachandoaconta.modelo.Conta;
import com.erikzambeligmail.rachandoaconta.modelo.Item;
import com.erikzambeligmail.rachandoaconta.persistencia.DataBaseHelper;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by erik_ on 18/11/2017.
 */

public class MeuAdapter extends ArrayAdapter<Item> {

    private Lista_Itens_Activity context;
    private List<Item> lista;
    private Conta conta = null;

    public MeuAdapter(Lista_Itens_Activity context, List<Item> lista, Conta conta) {
        super(context, 0, lista);
        this.context = context;
        this.lista = lista;
        this.conta = conta;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Item posicao = this.lista.get(position);
        convertView = LayoutInflater.from(this.context).inflate(R.layout.infla_item, null);
        TextView text = (TextView) convertView.findViewById(R.id.textViewDescItem);
        text.setText(posicao.getDescricao());
        TextView text1 = (TextView) convertView.findViewById(R.id.textViewValorItem);
        text1.setText(posicao.getValor() + " X " + posicao.getQuantidade() + " = \t" + posicao.getValorTotalItem());
        Button maisBtn = (Button) convertView.findViewById(R.id.buttonMais);
        Button menosBtn = (Button) convertView.findViewById(R.id.buttonMenos);

        if (!conta.getSituacao()) {
            maisBtn.setClickable(false);
            menosBtn.setClickable(false);
            menosBtn.setEnabled(false);
            maisBtn.setEnabled(false);
        } else {

            menosBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (posicao.getQuantidade() == 0) {
                        lista.remove(posicao);
                        try {
                            DataBaseHelper conexao = DataBaseHelper.getInstance(context);

                            conexao.getItemDao().delete(posicao);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else
                        posicao.setQuantidade(posicao.getQuantidade() - 1);
                    posicao.setValorTotalItem(posicao.getValor() * posicao.getQuantidade());
                    try {
                        DataBaseHelper conexao = DataBaseHelper.getInstance(context);

                        conexao.getItemDao().update(posicao);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    context.atualizaValores();
                    notifyDataSetChanged();
                }
            });
            maisBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (posicao.getQuantidade() == 100) {
                    } else {
                        posicao.setQuantidade(posicao.getQuantidade() + 1);
                        posicao.setValorTotalItem(posicao.getValor() * posicao.getQuantidade());
                        try {
                            DataBaseHelper conexao = DataBaseHelper.getInstance(context);

                            conexao.getItemDao().update(posicao);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    context.atualizaValores();
                    notifyDataSetChanged();
                }
            });
        }
            return convertView;

    }

}

