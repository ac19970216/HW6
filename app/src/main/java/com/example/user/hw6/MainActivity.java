package com.example.user.hw6;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editname,editlocal,editnumber;
    TextView texname,texlocal,texno,texnumber;
    Button add,edit,delete,query;
    SQLiteDatabase dbrw,dbBuy,dbProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editname =(EditText) findViewById(R.id.editname);
        editlocal =(EditText) findViewById(R.id.editlocal);
        editnumber =(EditText) findViewById(R.id.editnumber);

        texno = (TextView)findViewById(R.id.texno);
        texname=(TextView)findViewById(R.id.texname);
        texlocal=(TextView)findViewById(R.id.texlocal);
        texnumber=(TextView)findViewById(R.id.texnumber);

        add = (Button)findViewById(R.id.add);
        edit=(Button)findViewById(R.id.edit);
        delete=(Button)findViewById(R.id.delete);
        query=(Button)findViewById(R.id.query);


        MyDBHelper dbHelper = new MyDBHelper(this);
        dbrw = dbHelper.getWritableDatabase();
        MyProductDB Productdb = new MyProductDB(this);
        dbProduct = Productdb.getWritableDatabase();

        showallShop();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newShop();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                renewShop();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteShop();
            }
        });

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryShop();
            }
        });

    }

    public void newShop(){
        String[] colum={"local"};

        Cursor c;
        c=dbrw.query("myTable",colum,"title="+"'"+editname.getText().toString()+"'",null,null,null,null);
        if((editname.getText().toString().equals("")) || (editlocal.getText().toString().equals("")) || (editnumber.getText().toString().equals("")))
        {
            Toast.makeText(this, "輸入資料不完全", Toast.LENGTH_SHORT).show();
        }
        else if(c.getCount()>0)
            Toast.makeText(this,"已經有這家商店",Toast.LENGTH_SHORT).show();
        else{
            String strnumber = editnumber.getText().toString();
            ContentValues cv  = new ContentValues();
            cv.put("title",editname.getText().toString());
            cv.put("local",editlocal.getText().toString());
            cv.put("number",strnumber);
            dbrw.insert("myTable",null,cv);

            Toast.makeText(this,"新增店名:"+editname.getText().toString()+"地址"+editlocal.getText().toString()+"電話"+strnumber,Toast.LENGTH_SHORT).show();

            editname.setText("");
            editlocal.setText("");
            editnumber.setText("");
        }
        showallShop();
    }
    public  void renewShop(){
        String[] colum={"title"};

        Cursor c;
        c=dbrw.query("myTable",colum,"title="+"'"+editname.getText().toString()+"'",null,null,null,null);
        if(c.getCount()==0)
            Toast.makeText(this,"查無此商店",Toast.LENGTH_SHORT).show();
        else if((editname.getText().toString().equals("")) || ((editlocal.getText().toString().equals("")) && (editnumber.getText().toString().equals(""))))
            Toast.makeText(this,"沒有輸入更新值",Toast.LENGTH_SHORT).show();
        else{
            if(editlocal.getText().toString().equals(""))
            {
                ContentValues cv = new ContentValues();
                cv.put("number",editnumber.getText().toString());

                dbrw.update("myTable",cv,"title="+"'"+editname.getText().toString()+"'",null);
                Toast.makeText(this,"成功",Toast.LENGTH_SHORT).show();

                editname.setText("");
                editnumber.setText("");
            }
            else if(editnumber.getText().toString().equals(""))
            {
                ContentValues cv = new ContentValues();
                cv.put("local",editlocal.getText().toString());

                dbrw.update("myTable",cv,"title="+"'"+editname.getText().toString()+"'",null);
                Toast.makeText(this,"成功",Toast.LENGTH_SHORT).show();

                editname.setText("");
                editlocal.setText("");
            }
            else
            {
                ContentValues cv = new ContentValues();
                cv.put("number",editnumber.getText().toString());
                cv.put("local",editlocal.getText().toString());

                dbrw.update("myTable",cv,"title="+"'"+editname.getText().toString()+"'",null);
                Toast.makeText(this,"成功",Toast.LENGTH_SHORT).show();

                editname.setText("");
                editlocal.setText("");
                editnumber.setText("");
            }

        }
        showallShop();
    }
    public void deleteShop(){
        String[] colum={"title"};

        Cursor c;
        c=dbrw.query("myTable",colum,"title="+"'"+editname.getText().toString()+"'",null,null,null,null);
        if(editname.getText().toString().equals(""))
            Toast.makeText(this,"請輸入要刪除之值",Toast.LENGTH_SHORT).show();
        else if(c.getCount()==0)
            Toast.makeText(this,"查無此商店",Toast.LENGTH_SHORT).show();
        else{
            dbrw.delete("myTable","title="+"'"+editname.getText().toString()+"'",null);
            dbProduct.delete("myTable","shop="+"'"+editname.getText().toString()+"'",null);
            dbBuy.delete("myTable","shop="+"'"+editname.getText().toString()+"'",null);
            Toast.makeText(this,"刪除成功",Toast.LENGTH_SHORT).show();

            editname.setText("");
        }
        showallShop();
    }
    public void showallShop(){
        String index = "順序\n",title="店名\n",local="地址\n",number="電話\n";
        String[] colum={"title","local","number"};
        Cursor c;

        c=dbrw.query("myTable",colum,null,null,null,null,null);

        if(c.getCount()>0) {
            c.moveToFirst();

            for (int i = 0; i < c.getCount(); i++) {
                index += (i + 1) + "\n";
                title += c.getString(0) + "\n";
                local += c.getString(1) + "\n";
                number += c.getString(2) + "\n";
                c.moveToNext();
            }
            texno.setText(index);
            texname.setText(title);
            texlocal.setText(local);
            texnumber.setText(number);
            Toast.makeText(this, "共有" + c.getCount() + "筆紀錄", Toast.LENGTH_SHORT).show();
        }
        else{
            texno.setText(index);
            texname.setText(title);
            texlocal.setText(local);
            texnumber.setText(number);
        }
    }


    public void queryShop(){

        String[] colum={"title"};

        Cursor c;
        c=dbrw.query("myTable",colum,"title="+"'"+editname.getText().toString()+"'",null,null,null,null);
        if(editname.getText().toString().equals(""))
            Toast.makeText(this,"請輸入店名",Toast.LENGTH_SHORT).show();
        else if(c.getCount()==0)
            Toast.makeText(this,"查無此商店",Toast.LENGTH_SHORT).show();
        else{
            final String[] list_item = {"1.Google Map位址","2.商品目錄管理","3.下單管理","4.銷售紀錄"};
            AlertDialog.Builder dialog_list = new AlertDialog.Builder(MainActivity.this);
            dialog_list.setTitle("請選擇要哪種服務");
            dialog_list.setItems(list_item, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(i==0){
                        String[] colum={"local"};
                        Cursor c;
                        c=dbrw.query("myTable",colum,"title="+"'"+editname.getText().toString()+"'",null,null,null,null);
                        c.moveToFirst();
                        String Local = c.getString(0);

                        Intent intent = new Intent(MainActivity.this,Main4Activity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("name",editname.getText().toString());
                        intent.putExtras(bundle);

                        Bundle bundle1 = new Bundle();
                        bundle1.putString("local",Local);
                        intent.putExtras(bundle1);
                        startActivity(intent);
                    }
                    if(i==1){
                        Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("name",editname.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                    if(i==2){


                    }
                    if(i==3){

                    }

                }
            });
            dialog_list.show();
            showallShop();
        }

        /*String index = "順序\n",title="店名\n",local="地址\n",number="電話\n";
        String[] colum={"title","local","number"};

        Cursor c;
        if(editname.getText().toString().equals(""))
            c=dbrw.query("myTable",colum,null,null,null,null,null);
       else
            c=dbrw.query("myTable",colum,"title="+"'"+editname.getText().toString()+"'",null,null,null,null);

        if(c.getCount()>0){
            c.moveToFirst();

            for(int i =0;i<c.getCount();i++){
                index += (i+1)+"\n";
                title += c.getString(0)+"\n";
                local += c.getString(1)+"\n";
                number += c.getString(2)+"\n";
                c.moveToNext();
            }
            texno.setText(index);
            texname.setText(title);
            texlocal.setText(local);
            texnumber.setText(number);
            Toast.makeText(this,"共有"+c.getCount()+"筆紀錄",Toast.LENGTH_SHORT).show();
        }*/
    }

}
