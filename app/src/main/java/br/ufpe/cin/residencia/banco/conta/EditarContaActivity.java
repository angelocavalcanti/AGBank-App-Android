package br.ufpe.cin.residencia.banco.conta;

import static br.ufpe.cin.residencia.banco.conta.AdicionarContaActivity.validarCPF;
import static br.ufpe.cin.residencia.banco.conta.AdicionarContaActivity.regexSaldo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.ufpe.cin.residencia.banco.R;
import br.ufpe.cin.residencia.banco.cliente.ClienteViewModel;

//Ver anotações TODO no código
public class EditarContaActivity extends AppCompatActivity {

    public static final String KEY_NUMERO_CONTA = "numeroDaConta";
    ContaViewModel viewModel;
    ClienteViewModel viewModelCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_conta);
        viewModel = new ViewModelProvider(this).get(ContaViewModel.class);
        viewModelCliente = new ViewModelProvider(this).get(ClienteViewModel.class);

        Button btnAtualizar = findViewById(R.id.btnAtualizar);
        Button btnRemover = findViewById(R.id.btnRemover);
//        EditText campoNome = findViewById(R.id.nome);
        EditText campoNumero = findViewById(R.id.numero);
        EditText campoCPF = findViewById(R.id.cpf);
        EditText campoSaldo = findViewById(R.id.saldo);
        campoNumero.setEnabled(false);
//        TextView labelNome = findViewById(R.id.labelNome);

//        labelNome.setVisibility(View.GONE);
//        campoNome.setVisibility(View.GONE);

        Intent i = getIntent();
        String numeroConta = i.getStringExtra(KEY_NUMERO_CONTA);
        //TODO usar o número da conta passado via Intent para recuperar informações da conta
        // ANGELO ABAIXO
        viewModel.buscarPeloNumero(numeroConta);
        viewModel.contaAtual.observe(this, conta -> {
                // preenche os campos da tela com as informações da conta
                campoCPF.setText(conta.cpfCliente);
                campoNumero.setText(conta.numero);
                campoSaldo.setText(String.format("%.2f", conta.saldo).replace(",","."));
                // campoNome.setText(conta.nomeCliente); // não será mais preciso pois para editar o nome deve ser usada a tela de edição de cliente.
        });
        // ANGELO ACIMA
        btnAtualizar.setText("Atualizar");
        btnAtualizar.setOnClickListener(
                v -> {
//                    String nomeCliente = campoNome.getText().toString();
                    String cpfCliente = campoCPF.getText().toString();
                    String saldoConta = campoSaldo.getText().toString();
                    //TODO: Incluir validações aqui, antes de criar um objeto Conta. Se todas as validações passarem, aí sim monta um objeto Conta.

                    // ANGELO ABAIXO
                    String msg="";
//                    if(nomeCliente.length() >= 5) {
                    // verifica se o cpf é válido:
                    if(validarCPF(cpfCliente)) {
                            // busca pelo cpf, digitado no campo de cpf, no BD:
                            viewModelCliente.buscarPeloCPF(cpfCliente);
                            viewModelCliente.clienteAtual.observe(this, cliente -> {
                                // verifica se o viewModelCliente não retorna nulo (não encontra o cpf buscado):
                                if(cliente != null){
                                    // verifica se o saldo não está em branco e se segue a expressão regular:
                                    if(!saldoConta.isEmpty() && saldoConta.matches(regexSaldo)) {
                                        // cria um objeto conta com os dados passados:
                                        Conta c = new Conta(numeroConta, Double.valueOf(saldoConta), cliente.nome, cliente.cpf);
                                        // cria um alerta na tela ao clicar no botão para atualizar a conta
                                        AlertDialog.Builder confirmaAtualizacao = new AlertDialog.Builder(EditarContaActivity.this);
                                        // título do alerta
                                        confirmaAtualizacao.setTitle("ATUALIZAR CONTA");
                                        // mensagem do alerta exibido:
                                        confirmaAtualizacao.setMessage("Tem certeza que deseja atualizar a conta " + c.numero + " ?");
                                        // não fecha o alerta ao clicar fora dele (é preciso escolher uma opção do alerta):
                                        confirmaAtualizacao.setCancelable(false);
                                        // ao clicar no botão negativo do alerta, é cancelada a atualização e retorna para a tela de edição de conta:
                                        confirmaAtualizacao.setNegativeButton("CANCELAR", null);
                                        // ao clicar no botão positivo do alerta, a conta é atualizada:
                                        confirmaAtualizacao.setPositiveButton("SIM, ATUALIZAR CONTA", (dialogInterface, i1) -> {
                                            // atualiza a conta no BD com os novos dados:
                                            viewModel.atualizar(c);
                                            finish();
                                            Toast.makeText(EditarContaActivity.this, "A Conta " + c.numero + " foi atualizada com sucesso", Toast.LENGTH_SHORT).show();
                                        });
                                        // mostra o alerta na tela:
                                        confirmaAtualizacao.create().show();
                                    }else{
                                        Toast.makeText(EditarContaActivity.this, "Digite um saldo com números", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(EditarContaActivity.this, "É necessário informar um CPF de um cliente cadastrado", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            msg = "Digite um CPF válido";
                        }
//                    }else {
//                        msg = "Digite um nome com no mínimo 5 caracteres";
//                    }
                    if(!msg.equals("")){
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    }
                    // ANGELO ACIMA
                    //TODO: chamar o método que vai atualizar a conta no Banco de Dados
                }
        );

        btnRemover.setOnClickListener(v -> {
            //TODO implementar remoção da conta
            // ANGELO ABAIXO
            Conta c = viewModel.contaAtual.getValue();
            // cria um alerta na tela ao clicar no botão para excluir a conta:
            AlertDialog.Builder confirmaExclusao = new AlertDialog.Builder(EditarContaActivity.this);
            // título do alerta:
            confirmaExclusao.setTitle("EXCLUIR CONTA");
            // mensagem do alerta exibido:
            confirmaExclusao.setMessage("Tem certeza que deseja excluir a conta " + c.numero + " ?");
            // não fecha o alerta ao clicar fora dele (é preciso escolher uma opção do alerta):
            confirmaExclusao.setCancelable(false);
            // ao clicar no botão negativo do alerta, é cancelada a exclusão e retorna para a tela de edição de conta:
            confirmaExclusao.setNegativeButton("CANCELAR", null);
            // ao clicar no botão positivo do alerta, a conta é excluída:
            confirmaExclusao.setPositiveButton("SIM, EXCLUIR CONTA", (dialogInterface, i1) -> {
                // exclui a conta do banco de dados
                viewModel.remover(c);
                finish();
                Toast.makeText(EditarContaActivity.this, "A Conta " + c.numero + " foi excluída com sucesso", Toast.LENGTH_SHORT).show();
            });
            // mostra o alerta na tela:
            confirmaExclusao.create().show();
            // ANGELO ACIMA
        });
    }
}