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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.ufpe.cin.residencia.banco.R;

//Ver anotações TODO no código
public class EditarContaActivity extends AppCompatActivity {

    public static final String KEY_NUMERO_CONTA = "numeroDaConta";
    ContaViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_conta);
        viewModel = new ViewModelProvider(this).get(ContaViewModel.class);

        Button btnAtualizar = findViewById(R.id.btnAtualizar);
        Button btnRemover = findViewById(R.id.btnRemover);
        EditText campoNome = findViewById(R.id.nome);
        EditText campoNumero = findViewById(R.id.numero);
        EditText campoCPF = findViewById(R.id.cpf);
        EditText campoSaldo = findViewById(R.id.saldo);
        campoNumero.setEnabled(false);

        Intent i = getIntent();
        String numeroConta = i.getStringExtra(KEY_NUMERO_CONTA);
        //TODO usar o número da conta passado via Intent para recuperar informações da conta
        // ANGELO ABAIXO
        viewModel.buscarPeloNumero(numeroConta);
        viewModel.contaAtual.observe(this, conta -> {
                campoCPF.setText(conta.cpfCliente);
                campoNumero.setText(conta.numero);
                campoSaldo.setText(String.format("%.2f", conta.saldo).replace(",","."));
                campoNome.setText(conta.nomeCliente);
        });

        // ANGELO ACIMA
        btnAtualizar.setText("Atualizar");
        btnAtualizar.setOnClickListener(
                v -> {
                    String nomeCliente = campoNome.getText().toString();
                    String cpfCliente = campoCPF.getText().toString();
                    String saldoConta = campoSaldo.getText().toString();
                    //TODO: Incluir validações aqui, antes de criar um objeto Conta. Se todas as validações passarem, aí sim monta um objeto Conta.

                    // ANGELO ABAIXO
                    String msg="";
                    if(nomeCliente.length() >= 5) {
                        if (validarCPF(cpfCliente)) {
                            if(!saldoConta.isEmpty() && saldoConta.matches(regexSaldo)) {
                                Conta c = new Conta(numeroConta, Double.valueOf(saldoConta), nomeCliente, cpfCliente );
                                AlertDialog.Builder confirmaAtualizacao = new AlertDialog.Builder(EditarContaActivity.this); // cria um alerta na tela ao clicar no botão para atualizar a conta
                                confirmaAtualizacao.setTitle("ATUALIZAR CONTA"); // título do alerta
                                confirmaAtualizacao.setMessage("Tem certeza que deseja atualizar a conta " + c.numero + " ?"); // mensagem do alerta exibido
                                confirmaAtualizacao.setCancelable(false);
                                confirmaAtualizacao.setNegativeButton("CANCELAR", null); // ao clicar no botão negativo do alerta, é cancelada a atualização e retorna para a tela de edição de conta
                                // ao clicar no botão positivo do alerta, a conta é atualizada
                                confirmaAtualizacao.setPositiveButton("SIM, ATUALIZAR CONTA", (dialogInterface, i1) -> {
                                    viewModel.atualizar(c);
                                    finish();
                                    Toast.makeText(EditarContaActivity.this, "A Conta " + c.numero + " foi atualizada com sucesso", Toast.LENGTH_LONG).show();
                                });
                                confirmaAtualizacao.create().show(); // mostra o alerta na tela
                            }else{
                                msg="Digite um saldo com números";
                            }
                        }else {
                            msg = "Digite um CPF válido";
                        }
                    }else {
                        msg = "Digite um nome com no mínimo 5 caracteres";
                    }
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
            AlertDialog.Builder confirmaExclusao = new AlertDialog.Builder(EditarContaActivity.this); // cria um alerta na tela ao clicar no botão para excluir a conta
            confirmaExclusao.setTitle("EXCLUIR CONTA"); // título do alerta
            confirmaExclusao.setMessage("Tem certeza que deseja excluir a conta " + c.numero + " ?"); // mensagem do alerta exibido
            confirmaExclusao.setCancelable(false);
            confirmaExclusao.setNegativeButton("CANCELAR", null); // ao clicar no botão negativo do alerta, é cancelada a exclusão e retorna para a tela de edição de conta
            // ao clicar no botão positivo do alerta, a conta é excluída
            confirmaExclusao.setPositiveButton("SIM, EXCLUIR CONTA", (dialogInterface, i1) -> {
                    viewModel.remover(c); // exclui a conta do banco de dados
                    finish();
                    Toast.makeText(EditarContaActivity.this, "A Conta " + c.numero + " foi excluída com sucesso", Toast.LENGTH_LONG).show();
                });
            confirmaExclusao.create().show(); // mostra o alerta na tela
            // ANGELO ACIMA
        });
    }
}