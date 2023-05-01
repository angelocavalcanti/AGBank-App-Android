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

        btnAtualizar.setText(R.string.text_btn_atualizar);
        btnAtualizar.setOnClickListener(v -> {
                String nomeCliente = campoNome.getText().toString();

                String msg="";
                // verifica se o nome tem 5 ou mais caracteres:
                if(nomeCliente.length() >= 5) {
                    Cliente c = new Cliente(cpf, nomeCliente);
                    // cria um alerta na tela ao clicar no botão para atualizar o cliente:
                    AlertDialog.Builder confirmaAtualizacao = new AlertDialog.Builder(EditarClienteActivity.this);
                    // título do alerta:
                    confirmaAtualizacao.setTitle(R.string.dialog_titulo_atualizar_cliente);
                    // mensagem do alerta exibido:
                    confirmaAtualizacao.setMessage(getString(R.string.dialog_msg_atualizar_cliente_de_cpf, c.cpf));
                    // não fecha o alerta ao clicar fora dele (é preciso escolher uma opção do alerta):
                    confirmaAtualizacao.setCancelable(false);
                    // ao clicar no botão negativo do alerta, é cancelada a atualização e retorna para a tela de edição de cliente:
                    confirmaAtualizacao.setNegativeButton(R.string.dialog_btn_negativo_cancelar, null);
                    // ao clicar no botão positivo do alerta, o cliente é atualizado:
                    confirmaAtualizacao.setPositiveButton(R.string.dialog_btn_positivo_atualizar_cliente, (dialogInterface, i1) -> {
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
                        Toast.makeText(EditarClienteActivity.this, getString(R.string.msg_cliente_de_cpf_x_atualizado_sucesso, c.cpf), Toast.LENGTH_LONG).show();
                    });
                    // mostra o alerta na tela:
                    confirmaAtualizacao.create().show();
                }else {
                    msg = getString(R.string.msg_minimo_caracteres, 5);
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
            confirmaExclusao.setTitle(R.string.dialog_titulo_excluir_cliente);
            // mensagem do alerta exibido:
            confirmaExclusao.setMessage(getString(R.string.dialog_msg_excluir_cliente_de_cpf_x, c.cpf) + getString(R.string.dialog_obs_contas_serao_excluidas));
            // não fecha o alerta ao clicar fora dele (é preciso escolher uma opção do alerta):
            confirmaExclusao.setCancelable(false);
            // ao clicar no botão negativo do alerta, é cancelada a exclusão e retorna para a tela de edição de cliente:
            confirmaExclusao.setNegativeButton(R.string.dialog_btn_negativo_cancelar, null);
            // ao clicar no botão positivo do alerta, o cliente é excluída:
            confirmaExclusao.setPositiveButton(R.string.dialog_msg_positiva_sim_excluir_cliente, (dialogInterface, i1) -> {
                // exclui o cliente do banco de dados
                viewModel.remover(c);
                finish();
                Toast.makeText(EditarClienteActivity.this, getString(R.string.msg_cliente_de_cpf_x_excluido_sucesso, c.cpf), Toast.LENGTH_SHORT).show();
                });
            // mostra o alerta na tela:
            confirmaExclusao.create().show();
        });
    }
}