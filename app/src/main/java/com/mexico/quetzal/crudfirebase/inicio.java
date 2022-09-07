package com.mexico.quetzal.crudfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
}
