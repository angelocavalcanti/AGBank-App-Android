package br.ufpe.cin.residencia.banco.cliente;

import static br.ufpe.cin.residencia.banco.conta.AdicionarContaActivity.validarCPF;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import br.ufpe.cin.residencia.banco.R;
import br.ufpe.cin.residencia.banco.cliente.Cliente;
import br.ufpe.cin.residencia.banco.cliente.ClienteViewModel;

public class EditarClienteActivity extends AppCompatActivity {

    public static final String KEY_CPF_CLIENTE = "cpfDoCliente";
    ClienteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_cliente);
        viewModel = new ViewModelProvider(this).get(ClienteViewModel.class);

        Button btnAtualizar = findViewById(R.id.btnAtualizar);
        Button btnRemover = findViewById(R.id.btnRemover);
        EditText campoNome = findViewById(R.id.nome);
        EditText campoCPF = findViewById(R.id.cpf);
        campoCPF.setEnabled(false);

        Intent i = getIntent();
        String cpf = i.getStringExtra(KEY_CPF_CLIENTE);
        viewModel.buscarPeloCPF(cpf);
        viewModel.clienteAtual.observe(this, cliente -> {
                campoCPF.setText(cliente.cpf);
                campoNome.setText(cliente.nome);
        });

        btnAtualizar.setText("Atualizar");
        btnAtualizar.setOnClickListener(v -> {
                String nomeCliente = campoNome.getText().toString();
                String cpfCliente = campoCPF.getText().toString();

                String msg="";
                if(nomeCliente.length() >= 5) {
                    if (validarCPF(cpfCliente)) {
                        Cliente c = new Cliente(cpfCliente, nomeCliente);
                        AlertDialog.Builder confirmaAtualizacao = new AlertDialog.Builder(EditarClienteActivity.this); // cria um alerta na tela ao clicar no botão para atualizar o cliente
                        confirmaAtualizacao.setTitle("ATUALIZAR CLIENTE"); // título do alerta
                        confirmaAtualizacao.setMessage("Tem certeza que deseja atualizar o cliente " + c.cpf + " ?"); // mensagem do alerta exibido
                        confirmaAtualizacao.setCancelable(false);
                        confirmaAtualizacao.setNegativeButton("CANCELAR", null); // ao clicar no botão negativo do alerta, é cancelada a atualização e retorna para a tela de edição de cliente
                        // ao clicar no botão positivo do alerta, o cliente é atualizado
                        confirmaAtualizacao.setPositiveButton("SIM, ATUALIZAR CLIENTE", (dialogInterface, i1) -> {
                            viewModel.atualizar(c);
                            finish();
                            Toast.makeText(EditarClienteActivity.this, "O Cliente " + c.cpf + " foi atualizado com sucesso", Toast.LENGTH_LONG).show();
                        });
                        confirmaAtualizacao.create().show(); // mostra o alerta na tela
                    }else {
                        msg = "Digite um CPF válido";
                    }
                }else {
                    msg = "Digite um nome com no mínimo 5 caracteres";
                }
                if(!msg.equals("")){
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }
        });

        btnRemover.setOnClickListener(v -> {
            Cliente c = viewModel.clienteAtual.getValue();
            AlertDialog.Builder confirmaExclusao = new AlertDialog.Builder(EditarClienteActivity.this); // cria um alerta na tela ao clicar no botão para excluir o cliente
            confirmaExclusao.setTitle("EXCLUIR CLIENTE"); // título do alerta
            confirmaExclusao.setMessage("Tem certeza que deseja excluir o cliente " + c.cpf + " ?"); // mensagem do alerta exibido
            confirmaExclusao.setCancelable(false);
            confirmaExclusao.setNegativeButton("CANCELAR", null); // ao clicar no botão negativo do alerta, é cancelada a exclusão e retorna para a tela de edição de cliente
            // ao clicar no botão positivo do alerta, o cliente é excluída
            confirmaExclusao.setPositiveButton("SIM, EXCLUIR CLIENTE", (dialogInterface, i1) -> {
                    viewModel.remover(c); // exclui o cliente do banco de dados
                    finish();
                    Toast.makeText(EditarClienteActivity.this, "O Cliente " + c.cpf + " foi excluído com sucesso", Toast.LENGTH_LONG).show();
                });
            confirmaExclusao.create().show(); // mostra o alerta na tela
        });
    }
}