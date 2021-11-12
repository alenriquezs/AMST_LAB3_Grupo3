package com.example.grupo3_ta3_iniciosesionfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PerfilUsuario extends AppCompatActivity {
    TextView txt_id, txt_name, txt_email, txt_phone;
    EditText edTweet, edFecha;
    ImageView imv_photo;
    DatabaseReference db_reference;
    HashMap<String, String> info_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        Intent intent = getIntent();
        info_user = (HashMap<String, String>)intent.getSerializableExtra("info_user");

        txt_id = findViewById(R.id.txt_userId);
        txt_name = findViewById(R.id.txt_nombre);
        txt_email = findViewById(R.id.txt_correo);
        txt_phone = findViewById(R.id.txt_phone);
        imv_photo = findViewById(R.id.imv_foto);

        txt_id.setText("ID: "+info_user.get("user_id"));
        txt_name.setText(info_user.get("user_name"));
        txt_email.setText(info_user.get("user_email"));
        txt_phone.setText(info_user.get("user_phoneNumber"));
        String photo = info_user.get("user_photo");
        Picasso.with(getApplicationContext()).load(photo).into(imv_photo);

        edTweet = (EditText)findViewById(R.id.userTweet);
        edFecha = (EditText)findViewById(R.id.fechaTweet);

        iniciarBaseDeDatos();
        leerTweets();
    }

    public void cerrarSesion(View view){
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("msg", "cerrarSesion");
        startActivity(intent);
    }

    public void iniciarBaseDeDatos(){
        db_reference = FirebaseDatabase.getInstance().getReference().child("Grupos");
    }

    public void leerTweets(){
        db_reference.child("Grupo3").child("tweets").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                System.out.println(snapshot);
            }
        }

        @Override
        public void onCancelled(DatabaseError error) {
            System.out.println(error.toException());
        }
        });
    }

    public void pushTweet(View view){
        String user = info_user.get("user_name");
        String tweet = edTweet.getText().toString();
        String fecha = edFecha.getText().toString();
        DatabaseReference tweets = db_reference.child("Grupo3").child("tweets").child(tweet);
        tweets.child("autor").setValue(user);
        tweets.child("fecha").setValue(fecha);
        Toast.makeText(getApplicationContext(), "El tweet fue publicado", Toast.LENGTH_SHORT).show();
    }

}