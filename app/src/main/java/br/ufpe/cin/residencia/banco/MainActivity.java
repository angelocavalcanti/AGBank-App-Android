package br.ufpe.cin.residencia.banco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.residencia.banco.cliente.ClientesActivity;
import br.ufpe.cin.residencia.banco.conta.Conta;
import br.ufpe.cin.residencia.banco.conta.ContaRepository;
import br.ufpe.cin.residencia.banco.conta.ContasActivity;

//Ver anotações TODO no código
public class MainActivity extends AppCompatActivity {
    BancoViewModel viewModel;
// ANGELO ABAIXO
    private ContaRepository repository;
// ANGELO ACIMA
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(BancoViewModel.class);

        Button contas = findViewById(R.id.btnContas);
        Button clientes = findViewById(R.id.btnClientes);
        Button transferir = findViewById(R.id.btnTransferir);
        Button debitar = findViewById(R.id.btnDebitar);
        Button creditar = findViewById(R.id.btnCreditar);
        Button pesquisar = findViewById(R.id.btnPesquisar);
        TextView totalBanco = findViewById(R.id.totalDinheiroBanco);

        //Remover a linha abaixo se for implementar a parte de Clientes
//        clientes.setEnabled(false);

        contas.setOnClickListener(
                v -> startActivity(new Intent(this, ContasActivity.class))
        );
        clientes.setOnClickListener(
                v -> startActivity(new Intent(this, ClientesActivity.class))
        );
        transferir.setOnClickListener(
                v -> startActivity(new Intent(this, TransferirActivity.class))
        );
        creditar.setOnClickListener(
                v -> startActivity(new Intent(this, CreditarActivity.class))
        );
        debitar.setOnClickListener(
                v -> startActivity(new Intent(this, DebitarActivity.class))
        );
        pesquisar.setOnClickListener(
                v -> startActivity(new Intent(this, PesquisarActivity.class))
        );

        // ANGELO ABAIXO
        // mostra na tela o valor total atualizado automaticamente de acordo com o saldo de todas as contas
        viewModel.contasSaldo.observe(this, todasContas -> {
            totalBanco.setText(NumberFormat.getCurrencyInstance().format(viewModel.saldoTotalBanco()));
        });
        // ANGELO ACIMA
    }
    //TODO Neste arquivo ainda falta a atualização automática do valor total de dinheiro armazenado no banco
}