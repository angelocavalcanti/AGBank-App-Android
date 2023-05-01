package br.ufpe.cin.residencia.banco.cliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.residencia.banco.R;
import br.ufpe.cin.residencia.banco.cliente.AdicionarClienteActivity;
import br.ufpe.cin.residencia.banco.cliente.Cliente;
import br.ufpe.cin.residencia.banco.cliente.ClienteAdapter;

public class ClientesActivity extends AppCompatActivity {

    ClienteViewModel viewModel;
    ClienteAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        viewModel = new ViewModelProvider(this).get(ClienteViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.rvClientes);
        adapter = new ClienteAdapter(getLayoutInflater());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // carrega os clientes no viewmodel:
        viewModel.clientes.observe(
                this,
                novaListaClientes -> {
                    List<Cliente> novaLista = new ArrayList<>(novaListaClientes);
                    adapter.submitList(novaLista); // atualiza a lista de clientes automaticamente na tela
                });

        Button adicionarCliente = findViewById(R.id.btn_Adiciona);
        adicionarCliente.setOnClickListener(
                v -> startActivity(new Intent(this, AdicionarClienteActivity.class))
        );
    }
}