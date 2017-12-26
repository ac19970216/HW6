package com.example.user.hw6;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main3Activity extends AppCompatActivity {
    EditText editnumber;
    TextView textShop,textprice,textdescription,textdata,textprice2,textnumber,textproduct;
    Button Order,CancelOrder;
    Spinner spinnerProduct,spinnerdata;
    SQLiteDatabase dbrw,dbBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        editnumber =(EditText) findViewById(R.id.editnumber);
        textdescription=(TextView) findViewById(R.id.textdescription);
        textprice =(TextView) findViewById(R.id.textprice);
        textdata =(TextView) findViewById(R.id.textdata);
        textprice2 =(TextView) findViewById(R.id.textprice2);
        textnumber =(TextView) findViewById(R.id.textnumber);
        textproduct =(TextView) findViewById(R.id.textproduct);
        Order = (Button)findViewById(R.id.Order);
        CancelOrder=(Button)findViewById(R.id.CancelOrder);
        spinnerProduct=(Spinner)findViewById(R.id.spinnerProduct);
        spinnerdata=(Spinner)findViewById(R.id.spinnerdata);
        textShop=(TextView)findViewById(R.id.textShop);


        MyProductDB Productdb = new MyProductDB(this);
        dbrw = Productdb.getWritableDatabase();
        MyBuyDB Buydb = new MyBuyDB(this);
        dbBuy = Buydb.getWritableDatabase();

        Bundle bundle = getIntent().getExtras();
        final String name = bundle.getString("name");
        int i;




        textShop.setText("商店："+name);
        String[] colum={"title","price","description"};

        Cursor c;
        c=dbrw.query("myTable",colum,"shop="+"'"+name+"'",null,null,null,null);

        final String[][] product=new String[3][c.getCount()];


        Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Buying(name);

            }
        });
        CancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBuy(name);

            }
        });
        if(c.getCount()>0) {
            c.moveToFirst();

            for ( i = 0; i < c.getCount(); i++) {

                product[0][i] = c.getString(0);
                product[1][i] = c.getString(1);
                product[2][i] = c.getString(2);
                c.moveToNext();
            }
        }

        ArrayAdapter<String> productList = new ArrayAdapter<>(Main3Activity.this,
                android.R.layout.simple_spinner_dropdown_item,
                product[0]);
        spinnerProduct.setAdapter(productList);
        spinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textprice.setText(product[1][position]);
                textdescription.setText(product[2][position]);
                ShowAllBuyProduct(name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }
    public void Buying(String name){
        Date dt=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String time=sdf.format(dt);

        try{
            if (editnumber.getText().toString().equals("")) {
                Toast.makeText(this, "沒有輸入數量", Toast.LENGTH_SHORT).show();
            } else {
                ContentValues cv = new ContentValues();
                cv.put("shop", name);
                cv.put("product", spinnerProduct.getSelectedItem().toString());
                cv.put("price", Integer.parseInt(textprice.getText().toString()) * Integer.parseInt(editnumber.getText().toString()));
                cv.put("data", time);
                cv.put("number", editnumber.getText().toString());

                dbBuy.insert("myTable", null, cv);

                Toast.makeText(this, "下了一個訂單 商品名稱:" + spinnerProduct.getSelectedItem().toString() + "價格" + textprice.getText().toString() + "數量" + editnumber.getText().toString(), Toast.LENGTH_SHORT).show();

                editnumber.setText("");
            }
            ShowAllBuyProduct(name);
        }
        catch (NullPointerException  e){
            Toast.makeText(this, "沒有商品可下單", Toast.LENGTH_SHORT).show();
        }
    }
    public void ShowAllBuyProduct(String name){
        String[] colum2={"data"};
        Cursor d;
        String product="商品名稱\n",price="應繳金額\n",number="數量\n",data="時間\n";
        String[] colum={"product","price","data","number"};
        Cursor c;

        c=dbBuy.query("myTable",colum,"shop="+"'"+name+"'",null,null,null,null,null);

        if(c.getCount()>0) {
            c.moveToFirst();

            for (int i = 0; i < c.getCount(); i++) {
                product += c.getString(0) + "\n";
                price += c.getString(1) + "\n";
                data += c.getString(2) + "\n";
                number += c.getString(3) + "\n";
                c.moveToNext();
            }
            textdata.setText(data);
            textproduct.setText(product);
            textprice2.setText(price);
            textnumber.setText(number);
            Toast.makeText(this, "共有" + c.getCount() + "筆紀錄", Toast.LENGTH_SHORT).show();
        }
        else{
            textdata.setText(data);
            textproduct.setText(product);
            textprice2.setText(price);
            textnumber.setText(number);
        }
        d=dbBuy.query("myTable",colum2,"shop=" + "'" + name+ "'"+"AND product="+"'"+spinnerProduct.getSelectedItem().toString()+"'",null,null,null,null);

        final String[] data2=new String[d.getCount()];
        if(d.getCount()>0) {
            d.moveToFirst();

            for (int i = 0; i < d.getCount(); i++) {

                data2[i]=d.getString(0);
                d.moveToNext();
            }
        }
        ArrayAdapter<String> dataList = new ArrayAdapter<>(Main3Activity.this,
                android.R.layout.simple_spinner_dropdown_item,
                data2);
        spinnerdata.setAdapter(dataList);
    }
    public void deleteBuy(String name){
        String[] colum={"product"};

        try {
            Cursor c;
            c = dbBuy.query("myTable", colum, "shop=" + "'" + name+ "'"+"AND product=" + "'" + spinnerProduct.getSelectedItem().toString() + "'" + "AND data=" + "'" + spinnerdata.getSelectedItem().toString() + "'", null, null, null, null);
            if (c.getCount() == 0)
                Toast.makeText(this, "沒有訂單", Toast.LENGTH_SHORT).show();

            else {
                dbBuy.delete("myTable", "shop=" + "'" + name + "'" + "AND product=" + "'" + spinnerProduct.getSelectedItem().toString() + "'" + "AND data=" + "'" + spinnerdata.getSelectedItem().toString() + "'", null);
                Toast.makeText(this, "刪除成功", Toast.LENGTH_SHORT).show();
            }
            ShowAllBuyProduct(name);
        }
        catch (NullPointerException e){
            Toast.makeText(this, "沒有訂單可取消", Toast.LENGTH_SHORT).show();
        }
    }
}

