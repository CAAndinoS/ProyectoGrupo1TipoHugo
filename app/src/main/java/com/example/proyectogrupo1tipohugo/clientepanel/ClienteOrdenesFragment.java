package com.example.proyectogrupo1tipohugo.clientepanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectogrupo1tipohugo.R;

public class ClienteOrdenesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_clienteorden,null);
        getActivity().setTitle("Ordenes");
        return v;
    }
}
