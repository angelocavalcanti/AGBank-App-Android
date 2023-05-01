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
import br.ufpe.cin.residencia.banco.conta.Conta;
import br.ufpe.cin.residencia.banco.conta.ContaViewModel;

public class EditarClienteActivity extends AppCompatActivity {

    public static final String KEY_CPF_CLIENTE = "cpfDoCliente";
    ClienteViewModel viewModel;
    ContaViewModel viewModelConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_cliente);
        viewModel = new ViewModelProvider(this).get(ClienteViewModel.class);
        viewModelConta = new ViewModelProvider(this).get(ContaViewModel.class);

        Button btnAtualizar = findViewById(R.id.btnAtualizar);
        Button btnRemover = findViewById(R.id.btnRemover);
        EditText campoNome = findViewById(R.id.nome);
        EditText campoCPF = findViewById(R.id.cpf);
        campoCPF.setEnabled(false); // não habilitado pois o cpf não pode ser alterado

        Intent i = getIntent();
        // usa o cpf do cliente passado via Intent para recuperar informações do cliente:
        String cpf = i.getStringExtra(KEY_CPF_CLIENTE);
        viewModel.buscarPeloCPF(cpf);
        viewModel.clienteAtual.observe(this, cliente -> {
                // preenche os campos da tela com as informações do cliente:
                campoCPF.setText(cliente.cpf);
                campoNome.setText(cliente.nome);
        });

        btnAtualizar.setText("Atualizar");
        btnAtualizar.setOnClickListener(v -> {
                String nomeCliente = campoNome.getText().toString();

                String msg="";
                // verifica se o nome tem 5 ou mais caracteres:
                if(nomeCliente.length() >= 5) {
                    Cliente c = new Cliente(cpf, nomeCliente);
                    // cria um alerta na tela ao clicar no botão para atualizar o cliente:
                    AlertDialog.Builder confirmaAtualizacao = new AlertDialog.Builder(EditarClienteActivity.this);
                    // título do alerta:
                    confirmaAtualizacao.setTitle("ATUALIZAR CLIENTE");
                    // mensagem do alerta exibido:
                    confirmaAtualizacao.setMessage("Tem certeza que deseja atualizar o cliente " + c.cpf + " ?");
                    // não fecha o alerta ao clicar fora dele (é preciso escolher uma opção do alerta):
                    confirmaAtualizacao.setCancelable(false);
                    // ao clicar no botão negativo do alerta, é cancelada a atualização e retorna para a tela de edição de cliente:
                    confirmaAtualizacao.setNegativeButton("CANCELAR", null);
                    // ao clicar no botão positivo do alerta, o cliente é atualizado:
                    confirmaAtualizacao.setPositiveButton("SIM, ATUALIZAR CLIENTE", (dialogInterface, i1) -> {
                        viewModel.atualizar(c);

                        // ANGELO VERIFICAR ABAIXO

                            viewModelConta.contas.observe(this, listaContas -> {
                                if(listaContas != null){
                                for(Conta conta : listaContas){
                                    if(conta.cpfCliente.equals(cpf)) {
                                        viewModelConta.contaAtual.observe(this, contaCliente -> {
                                                contaCliente.nomeCliente = nomeCliente;
                                        });
                                    }
                                }
                                    listaContas = listaContas;
                                }
                            });


                        // ANGELO VERIFICAR ACIMA

                        finish();
                        Toast.makeText(EditarClienteActivity.this, "O Cliente " + c.cpf + " foi atualizado com sucesso", Toast.LENGTH_LONG).show();
                    });
                    // mostra o alerta na tela:
                    confirmaAtualizacao.create().show();
                }else {
                    msg = "Digite um nome com no mínimo 5 caracteres";
                }
                if(!msg.equals("")){
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }
        });

        btnRemover.setOnClickListener(v -> {
            Cliente c = viewModel.clienteAtual.getValue();
            // cria um alerta na tela ao clicar no botão para excluir o cliente:
            AlertDialog.Builder confirmaExclusao = new AlertDialog.Builder(EditarClienteActivity.this);
            // título do alerta:
            confirmaExclusao.setTitle("EXCLUIR CLIENTE");
            // mensagem do alerta exibido:
            confirmaExclusao.setMessage("Tem certeza que deseja excluir o(a) cliente " + c.cpf + " ?\n\nObs.: Todas as contas associadas a este(a) cliente também serão excluídas");
            // não fecha o alerta ao clicar fora dele (é preciso escolher uma opção do alerta):
            confirmaExclusao.setCancelable(false);
            // ao clicar no botão negativo do alerta, é cancelada a exclusão e retorna para a tela de edição de cliente:
            confirmaExclusao.setNegativeButton("CANCELAR", null);
            // ao clicar no botão positivo do alerta, o cliente é excluída:
            confirmaExclusao.setPositiveButton("SIM, EXCLUIR CLIENTE", (dialogInterface, i1) -> {
                // exclui o cliente do banco de dados
                viewModel.remover(c);
                finish();
                Toast.makeText(EditarClienteActivity.this, "Cliente " + c.cpf + " excluído com sucesso", Toast.LENGTH_SHORT).show();
                });
            // mostra o alerta na tela:
            confirmaExclusao.create().show();
        });
    }
}