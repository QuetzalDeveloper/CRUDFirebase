package com.mexico.quetzal.crudfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mexico.quetzal.crudfirebase.Modelos.Persona;

import java.util.UUID;

public class Actualizar extends AppCompatActivity {

    private TextInputEditText correo, nombre, apellidos, pass;
    private MaterialButton guardar;
    private String uid;
    //1. Declaramos variables de Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);

        correo = findViewById(R.id.correo);
        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        pass = findViewById(R.id.password);
        guardar = findViewById(R.id.actualizar);

        uid = getIntent().getStringExtra("uid");
        correo.setText(getIntent().getStringExtra("correo"));
        nombre.setText(getIntent().getStringExtra("nombre"));
        apellidos.setText(getIntent().getStringExtra("apellidos"));
        pass.setText(getIntent().getStringExtra("password"));

        //2. Inicializamos las variables de Firebase
        InicializarFirebase();

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Guardar();
            }
        });
    }

    private void InicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void Guardar(){
        String mail = correo.getText().toString();
        String name = nombre.getText().toString();
        String last = apellidos.getText().toString();
        String contra = pass.getText().toString();

        Persona p = new Persona();
        p.setUid(uid);
        p.setNombre(name);
        p.setApellidos(last);
        p.setCorreo(mail);
        p.setPassword(contra);

        databaseReference.child("Persona").child(p.getUid()).setValue(p);

        Toast.makeText(getApplicationContext(), "Persona actualizada", Toast.LENGTH_SHORT).show();
        this.finish();
    }
}