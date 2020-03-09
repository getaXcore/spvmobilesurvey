package com.olympindo.spvolympindo.modal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Vigaz on 8/30/2018.
 */

public class DatabaseManager {
    private static final String NAMA_DB = "spv_jtodis";
    private static final int DB_VERSION = 1;

    //start variable login
    private static final String ROW_ID_USER_SQLITE          = "user_id_user_sqlite";
    private static final String ROW_USER_ID_USER            = "user_id_user";
    private static final String ROW_USER_NAMALENGKAP        = "user_namalengkap";
    private static final String ROW_USER_USERNAME           = "user_username";
    private static final String ROW_USER_ALAMAT             = "user_alamat";
    private static final String ROW_USER_JK                 = "user_jk";
    private static final String ROW_USER_TELP               = "user_telp";
    private static final String ROW_USER_KATEGORI_ADMIN     = "user_kategori_admin";
    private static final String ROW_USER_ID_KOTA            = "user_id_kota";
    private static final String NAMA_TABEL_USER             = "tb_user";
    private static final String CREATE_TABLE_USER           = "create table "
            + NAMA_TABEL_USER           +" ("
            + ROW_ID_USER_SQLITE        + " integer PRIMARY KEY autoincrement,"
            + ROW_USER_ID_USER          + " text,"
            + ROW_USER_NAMALENGKAP      + " text,"
            + ROW_USER_USERNAME         + " text,"
            + ROW_USER_ALAMAT           + " text,"
            + ROW_USER_JK               + " text,"
            + ROW_USER_TELP             + " text,"
            + ROW_USER_KATEGORI_ADMIN   + " text,"
            + ROW_USER_ID_KOTA          + " text)";
    //end variable login


    //variable simpan json
    private static final String ROW_ID_JSON = "json_id";
    private static final String ROW_JSON_PARAM1 = "json_param1";
    private static final String ROW_JSON_PARAM2 = "json_param2";
    private static final String ROW_JSON_PARAM3 = "json_param3";
    private static final String ROW_JSON_PARAM4 = "json_param4";
    private static final String ROW_JSON_PARAM5 = "json_param5";
    private static final String ROW_JSON_PARAM6 = "json_param6";
    private static final String ROW_JSON_PARAM7 = "json_param7";
    private static final String ROW_JSON_PARAM8 = "json_param8";
    private static final String ROW_JSON_PARAM9 = "json_param9";
    private static final String ROW_JSON_PARAM10 = "json_param10";
    private static final String ROW_JSON_HASIL = "json_hasil";
    private static final String ROW_JSON_NAMA = "json_nama";
    private static final String ROW_JSON_TANGGAL = "json_tanggal";
    private static final String NAMA_TABEL_JSON = "tb_json";
    private static final String CREATE_TABLE_JSON = "create table "
            + NAMA_TABEL_JSON           +" ("
            + ROW_ID_JSON               + " integer PRIMARY KEY autoincrement,"
            + ROW_JSON_PARAM1           + " text,"
            + ROW_JSON_PARAM2           + " text,"
            + ROW_JSON_PARAM3           + " text,"
            + ROW_JSON_PARAM4           + " text,"
            + ROW_JSON_PARAM5           + " text,"
            + ROW_JSON_PARAM6           + " text,"
            + ROW_JSON_PARAM7           + " text,"
            + ROW_JSON_PARAM8           + " text,"
            + ROW_JSON_PARAM9           + " text,"
            + ROW_JSON_PARAM10          + " text,"
            + ROW_JSON_HASIL            + " text,"
            + ROW_JSON_NAMA             + " text,"
            + ROW_JSON_TANGGAL          + " text)";
    //end variable simpan json



    //variable simpan notif tugas
    private static final String ROW_ID_NOTIF_TUGAS          = "notif_tugas_id";
    private static final String ROW_NOTIF_TUGAS_ID_ORDER    = "notif_id_order";
    private static final String ROW_NOTIF_TUGAS_NAMA        = "notif_nama";
    private static final String ROW_NOTIF_TUGAS_ALAMAT      = "notif_alamat";
    private static final String ROW_NOTIF_TUGAS_STATUS      = "notif_status";
    private static final String NAMA_TABEL_NOTIF_TUGAS      = "simpannotif";
    private static final String CREATE_TABLE_NOTIF_TUGAS    = "create table "
            + NAMA_TABEL_NOTIF_TUGAS    +" ("
            + ROW_ID_NOTIF_TUGAS        + " integer PRIMARY KEY autoincrement,"
            + ROW_NOTIF_TUGAS_ID_ORDER  + " text,"
            + ROW_NOTIF_TUGAS_NAMA      + " text,"
            + ROW_NOTIF_TUGAS_ALAMAT    + " text,"
            + ROW_NOTIF_TUGAS_STATUS    + " text)";
    //end variable simpan notif tugas



    private final Context context;
    private DatabaseOpenHelper dbHelper;
    private SQLiteDatabase db;
    private static DatabaseOpenHelper sInstace;

    public DatabaseManager(Context ctx) {
        this.context = ctx;
        dbHelper = new DatabaseOpenHelper(ctx);
        db = dbHelper.getWritableDatabase();
    }

    private static class DatabaseOpenHelper extends
            SQLiteOpenHelper {

        public DatabaseOpenHelper(Context context) {
            super(context, NAMA_DB, null, DB_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_TABLE_USER);
            db.execSQL(CREATE_TABLE_JSON);
            db.execSQL(CREATE_TABLE_NOTIF_TUGAS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVer, int
                newVer) {
            db.execSQL("DROP TABLE IF EXISTS " + NAMA_TABEL_USER);
            db.execSQL("DROP TABLE IF EXISTS " + NAMA_TABEL_JSON);
            db.execSQL("DROP TABLE IF EXISTS " + NAMA_TABEL_NOTIF_TUGAS);

            onCreate(db);
        }
    }

    public void close() {
        dbHelper.close();
    }




    //table login
    public void addRow(String user_id, String namalengkap, String username, String alamat, String jk,
                       String telp, String kategori_admin, String id_kota) {
        ContentValues values = new ContentValues();
        values.put(ROW_USER_ID_USER, user_id);
        values.put(ROW_USER_NAMALENGKAP, namalengkap);
        values.put(ROW_USER_USERNAME, username);
        values.put(ROW_USER_ALAMAT, alamat);
        values.put(ROW_USER_JK, jk);
        values.put(ROW_USER_TELP, telp);
        values.put(ROW_USER_KATEGORI_ADMIN, kategori_admin);
        values.put(ROW_USER_ID_KOTA, id_kota);

        try {
            db.insert(NAMA_TABEL_USER, null, values);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<Object>> ambilSemuaBaris() {
        ArrayList<ArrayList<Object>> dataArray = new
                ArrayList<ArrayList<Object>>();
        Cursor cur = null;
        try {


            cur = db.query(NAMA_TABEL_USER, new String[] {  ROW_USER_ID_USER,
                            ROW_USER_NAMALENGKAP,
                            ROW_USER_USERNAME,
                            ROW_USER_ALAMAT,
                            ROW_USER_JK,
                            ROW_USER_TELP,
                            ROW_USER_KATEGORI_ADMIN,
                            ROW_USER_ID_KOTA },
                    null, null, null, null, null);
            cur.moveToFirst();
            if (!cur.isAfterLast()) {
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cur.getString(0));
                    dataList.add(cur.getString(1));
                    dataList.add(cur.getString(2));
                    dataList.add(cur.getString(3));
                    dataList.add(cur.getString(4));
                    dataList.add(cur.getString(5));
                    dataList.add(cur.getString(6));
                    dataList.add(cur.getString(7));
                    dataArray.add(dataList);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DB ERROR ", "ambilSemuaBaris "+e.toString());
            //   Toast.makeText(context, "gagal ambil semua baris:" + e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            // this gets called even if there is an exception somewhere above
            if(cur != null)
                cur.close();
        }
        return dataArray;
    }


    public void deleteAll(){
        try {
            db.delete(NAMA_TABEL_USER, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", e.toString());
        }
    }
    //end table login



    //table simpan json
    public void addRowJson(String json_hasil,String json_nama) {
        ContentValues values = new ContentValues();
        values.put(ROW_JSON_HASIL, json_hasil);
        values.put(ROW_JSON_NAMA, json_nama);
        values.put(ROW_JSON_TANGGAL, "datetime()");

        try {
            db.insert(NAMA_TABEL_JSON, null, values);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public void deleteJsonAll(String json_name){
        try {
          db.delete(NAMA_TABEL_JSON, ROW_JSON_NAMA + "='" + json_name+"'", null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", e.toString());
        }
    }

    public ArrayList<ArrayList<Object>> ambilBarisJson(String json_name) {
        ArrayList<ArrayList<Object>> dataArray = new ArrayList<ArrayList<Object>>();
        Cursor cur = null;
        try {
            cur = db.query(NAMA_TABEL_JSON, new String[] {  ROW_JSON_HASIL,
                            ROW_JSON_NAMA,ROW_JSON_TANGGAL},
                    ROW_JSON_NAMA + " = '"+json_name+"'", null,
                    null, null, null);
            cur.moveToFirst();
            if (
                    !cur.isAfterLast()) {
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cur.getString(0));
                    dataList.add(cur.getString(1));
                    dataList.add(cur.getString(2));
                    dataArray.add(dataList);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DB ERROR ", "ambilBarisJsonPilih "+e.toString());
            //Toast.makeText(context, "gagal ambil semua baris:" + e.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            // this gets called even if there is an exception somewhere above
            if(cur != null)
                cur.close();

        }
        return dataArray;
    }


    public ArrayList<ArrayList<Object>> ambilBarisJsonPilihALL() {
        ArrayList<ArrayList<Object>> dataArray = new ArrayList<ArrayList<Object>>();
        Cursor cur = null;
        try {
            cur = db.query(NAMA_TABEL_JSON, new String[] {  ROW_JSON_HASIL,
                    ROW_JSON_NAMA,ROW_JSON_TANGGAL }, null, null, null, null, null);
            cur.moveToFirst();
            if (
                    !cur.isAfterLast()) {
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cur.getString(0));
                    dataList.add(cur.getString(1));
                    dataList.add(cur.getString(2));
                    dataArray.add(dataList);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DB ERROR ", "ambilBarisJsonPilihALL "+e.toString());
            //Toast.makeText(context, "gagal ambil semua baris:" + e.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            // this gets called even if there is an exception somewhere above
            if(cur != null)
                cur.close();
        }
        return dataArray;
    }


    public void addRowJson1Param(String json_hasil,String json_nama, String json_param1) {
        ContentValues values = new ContentValues();
        values.put(ROW_JSON_PARAM1, json_param1);
        values.put(ROW_JSON_HASIL, json_hasil);
        values.put(ROW_JSON_NAMA, json_nama);
        values.put(ROW_JSON_TANGGAL, "datetime()");

        try {
            db.insert(NAMA_TABEL_JSON, null, values);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public void deleteJson1ParamAll(String json_name, String json_param1){
        try {
            db.delete(NAMA_TABEL_JSON, ROW_JSON_NAMA + "= '"+json_name+"' AND "
                    +ROW_JSON_PARAM1+" = '"+json_param1+"'", null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", e.toString());
        }
    }

    public ArrayList<ArrayList<Object>> ambilBarisJson1Param(String json_name, String json_param1) {
        ArrayList<ArrayList<Object>> dataArray = new ArrayList<ArrayList<Object>>();
        Cursor cur = null;
        try {
            cur = db.query(NAMA_TABEL_JSON, new String[] {  ROW_JSON_HASIL,
                            ROW_JSON_NAMA,ROW_JSON_TANGGAL},
                    ROW_JSON_NAMA + " = '"+json_name+"' AND "
                            +ROW_JSON_PARAM1+" = '"+json_param1+"'", null,
                    null, null, null);
            cur.moveToFirst();
            if (
                    !cur.isAfterLast()) {
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cur.getString(0));
                    dataList.add(cur.getString(1));
                    dataList.add(cur.getString(2));
                    dataArray.add(dataList);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DB ERROR ", "ambilBarisJsonPilih "+e.toString());
            //Toast.makeText(context, "gagal ambil semua baris:" + e.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            // this gets called even if there is an exception somewhere above
            if(cur != null)
                cur.close();
        }
        return dataArray;
    }
    //end json pilih



    //simpan notif tugas
    public void addRowNotifTugas(String h_id_order,String h_nama,String h_alamat) {
        ContentValues values = new ContentValues();
        values.put(ROW_NOTIF_TUGAS_ID_ORDER, h_id_order);
        values.put(ROW_NOTIF_TUGAS_NAMA, h_nama);
        values.put(ROW_NOTIF_TUGAS_ALAMAT, h_alamat);
        values.put(ROW_NOTIF_TUGAS_STATUS, "0");

        try {
            db.insert(NAMA_TABEL_NOTIF_TUGAS, null, values);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<Object>> ambilBarisNotifTugas(String id_order) {
        ArrayList<ArrayList<Object>> dataArray = new ArrayList<ArrayList<Object>>();
        Cursor cur = null;
        try {
            cur = db.query(NAMA_TABEL_NOTIF_TUGAS, new String[] {  ROW_NOTIF_TUGAS_ID_ORDER,
                            ROW_NOTIF_TUGAS_NAMA,ROW_NOTIF_TUGAS_ALAMAT,ROW_NOTIF_TUGAS_STATUS },
                    ROW_NOTIF_TUGAS_ID_ORDER + "='" + id_order+"'", null, null, null, null);
            cur.moveToFirst();
            if (
                    !cur.isAfterLast()) {
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cur.getString(0));
                    dataList.add(cur.getString(1));
                    dataList.add(cur.getString(2));
                    dataList.add(cur.getString(3));
                    dataArray.add(dataList);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DEBE ERROR", e.toString());
            Toast.makeText(context, "gagal ambil semua baris:" + e.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            // this gets called even if there is an exception somewhere above
            if(cur != null)
                cur.close();
        }
        return dataArray;
    }


    public ArrayList<ArrayList<Object>> ambilBarisNotifTugasALL() {
        ArrayList<ArrayList<Object>> dataArray = new ArrayList<ArrayList<Object>>();
        Cursor cur = null;
        try {
            cur = db.query(NAMA_TABEL_NOTIF_TUGAS, new String[] {  ROW_NOTIF_TUGAS_ID_ORDER,
                    ROW_NOTIF_TUGAS_NAMA,ROW_NOTIF_TUGAS_ALAMAT,ROW_NOTIF_TUGAS_STATUS }, null, null, null, null, null);
            cur.moveToFirst();
            if (
                    !cur.isAfterLast()) {
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cur.getString(0));
                    dataList.add(cur.getString(1));
                    dataList.add(cur.getString(2));
                    dataList.add(cur.getString(3));
                    dataArray.add(dataList);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DEBE ERROR", e.toString());
            Toast.makeText(context, "gagal ambil semua baris:" + e.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            // this gets called even if there is an exception somewhere above
            if(cur != null)
                cur.close();
        }
        return dataArray;
    }


    public void updateBarisNotifTugas() {
        ContentValues cv = new ContentValues();
        cv.put(ROW_NOTIF_TUGAS_STATUS, "1");


        try {
            db.update(NAMA_TABEL_NOTIF_TUGAS, cv, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Db Error", e.toString());
        }
    }

}
