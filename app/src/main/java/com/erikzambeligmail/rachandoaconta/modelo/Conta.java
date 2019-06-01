package com.erikzambeligmail.rachandoaconta.modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by erik_ on 13/11/2017.
 */

@DatabaseTable(tableName = "conta")
public class Conta {

    public static final String ID = "id";
    public static final String ESTABELECIMENTO = "estabelecimento";
    public static final String NUMPESSOAS = "numPessoas";
    public static final String SITUACAO = "situacao";
    public static final String ADICIONAL = "adicional";
    public static final String VALORFINAL = "valorfinal";

    @DatabaseField(generatedId = true, columnName = ID)
    private int id;

    @DatabaseField(canBeNull = false, columnName = ESTABELECIMENTO)
    private String estabelecimento;

    @DatabaseField(columnName = NUMPESSOAS)
    private int numPessoas;

    @DatabaseField(columnName = SITUACAO)
    private boolean situacao;

    @DatabaseField(columnName = ADICIONAL)
    private boolean adicional;

    @DatabaseField(columnName = VALORFINAL)
    private float valorFinal;

    public Conta(){}

    public int getId() {
        return id;
    }

    public String getEstabelecimento() {
        return estabelecimento;
    }

    public int getNumPessoas() {
        return numPessoas;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEstabelecimento(String estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public void setAdicional(boolean adicional) {
        this.adicional = adicional;
    }

    public boolean getSituacao(){return this.situacao;}

    public boolean getAdicional(){return this.adicional;}


    public void setNumPessoas(int numPessoas) {
        this.numPessoas = numPessoas;
    }

    public void setSituacao(boolean situacao) {
        this.situacao = situacao;
    }

    public float getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(float valorFinal) {
        this.valorFinal = valorFinal;
    }

    @Override
    public String toString(){
        return getEstabelecimento();
    }
}
