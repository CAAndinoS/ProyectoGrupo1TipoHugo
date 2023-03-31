package com.example.proyectogrupo1tipohugo.clientepanel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1tipohugo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ClienteHomeFragment extends Fragment {

    Button añadir;
    FirebaseFirestore mFireStore;
    RecyclerView recyclerView;
    ArrayList<Platillo> orderArrayList;
    OrderListAdapterCliente myAdapter;
    TextView txtP, txtE, estado, pedidos;
    Button btnpendiente, btnentregado;
    private String usuario;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_cliente_home, container, false);

        usuario = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        añadir = root.findViewById(R.id.btnañadirorden);
        pedidos = root.findViewById(R.id.textView11);
        estado = root.findViewById(R.id.txtDescription);
        mFireStore = FirebaseFirestore.getInstance();
        recyclerView = root.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderArrayList = new ArrayList<Platillo>();
        myAdapter = new OrderListAdapterCliente(getContext(), orderArrayList);
        recyclerView.setAdapter(myAdapter);
        EventChangeListener();

        recyclerView.setAlpha(0);
        recyclerView.setTranslationY(300);
        recyclerView.animate().alpha(1).translationY(0).setDuration(1000).setStartDelay(500);

        btnpendiente = root.findViewById(R.id.btnpendiente);
        btnentregado = root.findViewById(R.id.btnentregado);
        txtE = root.findViewById(R.id.textE);
        txtP = root.findViewById(R.id.textP);



        pedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderArrayList.clear();
                myAdapter.notifyDataSetChanged();
                EventChangeListener();
                recyclerView.setAlpha(0);
                recyclerView.setTranslationY(300);
                recyclerView.animate().alpha(1).translationY(0).setDuration(1000).setStartDelay(500);
            }
        });


        return root;

    }



    private void EventChangeListener() {
        mFireStore.collection("platillo").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null){
                    Log.e("FireStore error:",error.getMessage());
                    return;
                }
                if (error == null){
                }

                for (DocumentChange dc : value.getDocumentChanges()){
                    if (dc.getType() == DocumentChange.Type.ADDED){
                        orderArrayList.add(dc.getDocument().toObject(Platillo.class));
                        Collections.sort(orderArrayList, new Comparator<Platillo>() {
                            @Override
                            public int compare(Platillo platillo, Platillo t1) {
                                return t1.getTitulo().compareTo(platillo.getTitulo());
                            }
                        });
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }
        });
    }

}
