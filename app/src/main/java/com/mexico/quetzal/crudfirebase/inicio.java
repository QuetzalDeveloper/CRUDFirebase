package com.mexico.quetzal.crudfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mexico.quetzal.crudfirebase.Modelos.Persona;
import java.util.ArrayList;
import java.util.List;

public class inicio extends AppCompatActivity {

    private ListView lista;
    private List<Persona> personaList = new ArrayList<Persona>();
    ArrayAdapter<Persona> personaArrayAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        lista = findViewById(R.id.lista);

        InicializarFirebase();
        listarDatos();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i= new Intent(getApplicationContext(), Actualizar.class);
                i.putExtra("uid", personaList.get(position).getUid());
                i.putExtra("correo", personaList.get(position).getCorreo());
                i.putExtra("nombre", personaList.get(position).getNombre());
                i.putExtra("apellidos", personaList.get(position).getApellidos());
                i.putExtra("password", personaList.get(position).getPassword());
                startActivity(i);
            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Eliminar(position);
                return true;
            }
        });
    }

    private void Eliminar(int position){
        final AlertDialog.Builder d = new AlertDialog.Builder(inicio.this);
        d.setTitle("Eliminar");
        d.setMessage("Se eliminara a "+personaList.get(position).toString()+" de la base de datos");
        d.setCancelable(false);
        d.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.child("Persona").child(personaList.get(position).getUid()).removeValue();
            }
        });
        d.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        d.show();
    }

    private void listarDatos(){
        databaseReference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                personaList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Persona p = dataSnapshot.getValue(Persona.class);
                    personaList.add(p);
                    personaArrayAdapter = new ArrayAdapter<Persona>(getApplicationContext(), android.R.layout.simple_list_item_1, personaList);
                    lista.setAdapter(personaArrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void InicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.manu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.add:
                Intent i= new Intent(getApplicationContext(), insertar.class);
                startActivity(i);
                break;
            default: break;
        }
        return true;
    }



    @Override
    public void onResume(){
        super.onResume();
        listarDatos();
    }
}
