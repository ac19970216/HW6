package com.example.user.hw6;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;


public class Main5Activity extends AppCompatActivity {
    TextView textShop,textprice,textnumber,textproduct,textdata;
    SQLiteDatabase dbBuy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);


        textprice = (TextView) findViewById(R.id.textprice);
        textnumber = (TextView) findViewById(R.id.textnumber);
        textproduct = (TextView) findViewById(R.id.textproduct);
        textShop = (TextView) findViewById(R.id.textShop);
        textdata = (TextView) findViewById(R.id.textdata);
        MyBuyDB Buydb = new MyBuyDB(this);
        dbBuy = Buydb.getWritableDatabase();

        Bundle bundle = getIntent().getExtras();
        final String name = bundle.getString("name");
        textShop.setText("商店："+name);
        ShowAllBuyProduct(name);


    }

    public void ShowAllBuyProduct(String name){
        String product="商品名稱\n",price="營業額\n",number="銷售數量\n",data="時間\n";
        String[] colum={"product","price","data","number"};
        Cursor c;

        c=dbBuy.query("myTable",colum,"shop="+"'"+name+"'",null,null,null,null);

        if(c.getCount()>0) {
            c.moveToFirst();

            for (int i = 0; i < c.getCount(); i++) {
                product += c.getString(0) + "\n";
                price += c.getString(1) + "\n";
                data += c.getString(2) + "\n";
                number += c.getString(3) + "\n";
                c.moveToNext();
            }
            textproduct.setText(product);
            textprice.setText(price);
            textnumber.setText(number);
            textdata.setText(data);
            Toast.makeText(this, "共有" + c.getCount() + "筆紀錄", Toast.LENGTH_SHORT).show();
        }
        else{
            textproduct.setText(product);
            textprice.setText(price);
            textnumber.setText(number);
            textdata.setText(data);
        }
    }

}

