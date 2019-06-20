package com.erikzambeligmail.NewRachandoConta.modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by erik_ on 13/11/2017.
 */

@DatabaseTable(tableName = "item")
public class Item {

    public static final String ID = "id";
    public static final String DESCRICAO = "descricao";
    public static final String VALOR = "valor";
    public static final String QUANTIDADE = "quantidade";
    public static final String CONTA_ID = "conta_id";
    public static final String TOTALITEM = "TOTALITEM";


    @DatabaseField(generatedId = true, columnName = ID)
    private int id;

    @DatabaseField(canBeNull = false, columnName = DESCRICAO)
    private String descricao;

    @DatabaseField(columnName = VALOR)
    private float valor;

    @DatabaseField(columnName = TOTALITEM)
    private float valorTotalItem;

    @DatabaseField(columnName = QUANTIDADE)
    private int quantidade;

    @DatabaseField(foreign = true)
    private Conta conta;

    public Item(){}

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public float getValor() {
        return valor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public Conta getConta() {
        return this.conta;
    }

    public void setValorTotalItem(float valorTotalItem) {
        this.valorTotalItem = valorTotalItem;
    }

    public float getValorTotalItem() {
        return valorTotalItem;
    }

    @Override
    public String toString(){return getDescricao();}
}

