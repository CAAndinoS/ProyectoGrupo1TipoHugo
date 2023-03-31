package com.example.proyectogrupo1tipohugo.deliverypanel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1tipohugo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class DeliveryOrdenFragment extends Fragment {

    FirebaseFirestore mFireStore;
    RecyclerView recyclerView;
    ArrayList<OrdenesDelivery> orderArrayList;
    OrderListAdapterDelivery myAdapter;
    TextView Ordenes;
    Button detalle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Ordenes Pendientes");
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_deliveryordenes, container, false);

        mFireStore = FirebaseFirestore.getInstance();
        recyclerView = root.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);

        detalle = root.findViewById(R.id.btndetalle);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderArrayList = new ArrayList<OrdenesDelivery>();
        myAdapter = new OrderListAdapterDelivery(getContext(), orderArrayList, getFragmentManager());
        recyclerView.setAdapter(myAdapter);
        EventChangeListener();

        recyclerView.setAlpha(0);
        recyclerView.setTranslationY(300);
        recyclerView.animate().alpha(1).translationY(0).setDuration(1000).setStartDelay(500);



        return root;

    }

    private void EventChangeListener() {

        ArrayList<String> estados = new ArrayList<>();
        estados.add("pendiente");
        estados.add("entrega");

        String curUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        mFireStore.collection("pedidos")
                .whereIn("estado", Arrays.asList(estados.toArray()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (
                                        (doc.getString("estado").equals("entrega") && curUser.equals(doc.getString("repartidor")))
                                        || (doc.getString("estado").equals("pendiente"))
                                ) {
                                    orderArrayList.add(doc.toObject(OrdenesDelivery.class));
                                    Collections.sort(orderArrayList, new Comparator<OrdenesDelivery>() {
                                        @Override
                                        public int compare(OrdenesDelivery ordenesDelivery, OrdenesDelivery t1) {
                                            return t1.getTitulo().compareTo(ordenesDelivery.getTitulo());
                                        }
                                    });
                                    myAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void EventChangeListenerD() {
        mFireStore.collection("ordenes_delivery").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        System.out.println(dc.getDocument().getTimestamp("fecha").toString());
                        orderArrayList.add(dc.getDocument().toObject(OrdenesDelivery.class));
                        Collections.sort(orderArrayList, new Comparator<OrdenesDelivery>() {
                            @Override
                            public int compare(OrdenesDelivery ordenesDelivery, OrdenesDelivery t1) {
                                return t1.getTitulo().compareTo(ordenesDelivery.getTitulo());
                            }
                        });
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}