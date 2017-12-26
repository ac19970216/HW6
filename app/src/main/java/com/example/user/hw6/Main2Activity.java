package com.example.user.hw6;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.jar.Attributes;

public class Main2Activity extends AppCompatActivity {
    EditText editname,editdescription,editprice;
    TextView texname,texdescription,texno,texprice,textShop;
    Button add,delete;
    SQLiteDatabase dbrw,dbBuy;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        editname =(EditText) findViewById(R.id.editname);
        editprice =(EditText) findViewById(R.id.editprice);
        editdescription =(EditText) findViewById(R.id.editdescription);

        texno = (TextView)findViewById(R.id.texno);
        texname=(TextView)findViewById(R.id.texname);
        texdescription=(TextView)findViewById(R.id.texdescription);
        texprice=(TextView)findViewById(R.id.texprice);
        textShop=(TextView)findViewById(R.id.textShop);

        add = (Button)findViewById(R.id.add);
        delete=(Button)findViewById(R.id.delete);


        MyProductDB productDB = new MyProductDB(this);
        dbrw = productDB.getWritableDatabase();


        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        textShop.setText("店名:"+name);
        showallProduct();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newProduct();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct();
            }
        });


    }
    public void newProduct(){
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String[] colum={"title"};
        Cursor c;
        c=dbrw.query("myTable",colum,"Shop="+"'"+name+"'"+"AND title="+"'"+editname.getText().toString()+"'",null,null,null,null);
        if((editname.getText().toString().equals("")) || (editdescription.getText().toString().equals("")) || (editprice.getText().toString().equals("")))
        {
            Toast.makeText(this, "輸入資料不完全", Toast.LENGTH_SHORT).show();
        }
        else if(c.getCount()>0)
            Toast.makeText(this,"已經有此商品",Toast.LENGTH_SHORT).show();
        else{
            Integer price = Integer.parseInt(editprice.getText().toString());

            ContentValues cv  = new ContentValues();
            cv.put("title",editname.getText().toString());
            cv.put("price",price);
            cv.put("description",editdescription.getText().toString());
            cv.put("shop",name);

            dbrw.insert("myTable",null,cv);

            Toast.makeText(this,"新增商品:"+editname.getText().toString()+"金額"+price+"描述"+editdescription.getText().toString(),Toast.LENGTH_SHORT).show();

            editname.setText("");
            editdescription.setText("");
            editprice.setText("");
        }
        showallProduct();
    }
    public void deleteProduct(){
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String[] colum={"title"};
        Cursor c;
        c=dbrw.query("myTable",colum,"shop="+"'"+name+"'"+"AND title="+"'"+editname.getText().toString()+"'",null,null,null,null);
        if(editname.getText().toString().equals(""))
            Toast.makeText(this,"請輸入要刪除之值",Toast.LENGTH_SHORT).show();
        else if(c.getCount()==0)
            Toast.makeText(this,"沒有此商品",Toast.LENGTH_SHORT).show();
        else{
            dbrw.delete("myTable","shop="+"'"+ name+"'"+"AND title="+"'"+editname.getText().toString()+"'",null);
            dbBuy.delete("myTable","shop="+"'"+name+"'"+"AND product="+"'"+editname.getText().toString()+"'",null);
            Toast.makeText(this,"刪除成功",Toast.LENGTH_SHORT).show();

            editname.setText("");
        }
        showallProduct();
    }
    public void showallProduct(){
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String index = "順序\n",title="商品名稱\n",price="金額\n",description="描述\n";
        String[] colum={"title","price","description"};
        Cursor c;
        c=dbrw.query("myTable",colum,"shop="+"'"+name+"'",null,null,null,null);

        if(c.getCount()>0) {
            c.moveToFirst();

            for (int i = 0; i < c.getCount(); i++) {
                index += (i + 1) + "\n";
                title += c.getString(0) + "\n";
                price += c.getString(1) + "\n";
                description += c.getString(2) + "\n";
                c.moveToNext();
            }
            texno.setText(index);
            texname.setText(title);
            texdescription.setText(description);
            texprice.setText(price);
            Toast.makeText(this, "共有" + c.getCount() + "筆紀錄", Toast.LENGTH_SHORT).show();
        }
    }
}
