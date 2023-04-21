package br.ufpe.cin.residencia.banco.conta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.ufpe.cin.residencia.banco.R;

//Ver anotações TODO no código
public class AdicionarContaActivity extends AppCompatActivity {

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

        btnAtualizar.setText("Inserir");
        btnRemover.setVisibility(View.GONE);

        btnAtualizar.setOnClickListener(
                v -> {
                    String nomeCliente = campoNome.getText().toString();
                    String cpfCliente = campoCPF.getText().toString();
                    String numeroConta = campoNumero.getText().toString();
                    String saldoConta = campoSaldo.getText().toString();
                    //TODO: Incluir validações aqui, antes de criar um objeto Conta (por exemplo, verificar que digitou um nome com pelo menos 5 caracteres, que o campo de saldo tem de fato um número, assim por diante). Se todas as validações passarem, aí sim cria a Conta conforme linha abaixo.

                    // ANGELO ABAIXO
                    String msg="";
                    if(nomeCliente != null && nomeCliente.length() >= 5) {
                        if (validarCPF(cpfCliente)) {
                            if(!numeroConta.isEmpty() && numeroConta != null) {
                                if(!saldoConta.isEmpty() && saldoConta != null && saldoConta.matches(
                                        "[0-9]+")) {
                                    Conta c = new Conta(numeroConta, Double.valueOf(saldoConta),
                                            nomeCliente, cpfCliente);
                                    viewModel.inserir(c);
                                    finish();
                                }else{
                                    msg="Digite um saldo com números.";
                                }
                            }else {
                                msg = "Digite um número da conta.";
                            }
                        }else {
                            msg = "Digite um CPF válido.";
                        }
                    }else {
                        msg = "Digite um nome com no mínimo 5 caracteres.";
                    }
                    if (!msg.equals("")) {
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    }
                    // ANGELO ACIMA
                    //TODO: chamar o método que vai salvar a conta no Banco de Dados
                }
        );

    }
    public static boolean validarCPF(String cpf) {
        cpf = cpf.replace(".","").replace("-","").trim();
        if (cpf == null || cpf.length() != 11)
            return false;

        try {
            Long.parseLong(cpf);
        } catch (NumberFormatException e) { // CPF não possui somente números
            return false;
        }

        return calcDigVerif(cpf.substring(0, 9)).equals(cpf.substring(9, 11));
    }
    private static String calcDigVerif(String num) {
        Integer primDig, segDig;
        int soma = 0, peso = 10;
        for (int i = 0; i < num.length(); i++)
            soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;

        if (soma % 11 == 0 | soma % 11 == 1)
            primDig = new Integer(0);
        else
            primDig = new Integer(11 - (soma % 11));

        soma = 0;
        peso = 11;
        for (int i = 0; i < num.length(); i++)
            soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;

        soma += primDig.intValue() * 2;
        if (soma % 11 == 0 | soma % 11 == 1)
            segDig = new Integer(0);
        else
            segDig = new Integer(11 - (soma % 11));

        return primDig.toString() + segDig.toString();
    }
}