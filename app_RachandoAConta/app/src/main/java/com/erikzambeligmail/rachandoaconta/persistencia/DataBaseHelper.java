package com.erikzambeligmail.rachandoaconta.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.erikzambeligmail.rachandoaconta.modelo.Conta;
import com.erikzambeligmail.rachandoaconta.modelo.Item;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by erik_ on 13/11/2017.
 */

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME    = "RachandoAConta.db";
    private static final int    DB_VERSION = 6;

    private static DataBaseHelper instance;

    private Context context;
    private Dao<Conta, Integer>  contaDao;
    private Dao<Item, Integer> itemDao;

    public static DataBaseHelper getInstance(Context contexto){

        if (instance == null){
            instance = new DataBaseHelper(contexto);
        }

        return instance;
    }

    private DataBaseHelper(Context contexto) {
        super(contexto, DB_NAME, null, DB_VERSION);
        context = contexto;
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Conta.class);
            TableUtils.createTable(connectionSource, Item.class);
        } catch (SQLException e) {
            Log.e(DataBaseHelper.class.getName(), "onCreate", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {

            TableUtils.dropTable(connectionSource, Item.class, true);
            TableUtils.dropTable(connectionSource, Conta.class, true);
            onCreate(database, connectionSource);

        } catch (SQLException e) {
            Log.e(DataBaseHelper.class.getName(), "onUpgrade", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<Conta, Integer> getContaDao() throws SQLException {

        if (contaDao == null) {
            contaDao = getDao(Conta.class);
        }

        return contaDao;
    }

    public Dao<Item, Integer> getItemDao() throws SQLException {

        if (itemDao == null) {
            itemDao = getDao(Item.class);
        }

        return itemDao;
    }
}
