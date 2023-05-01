package br.ufpe.cin.residencia.banco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//Ver anotações TODO no código
public class DebitarActivity extends AppCompatActivity {
    BancoViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operacoes);
        viewModel = new ViewModelProvider(this).get(BancoViewModel.class);

        TextView tipoOperacao = findViewById(R.id.tipoOperacao);
        EditText numeroContaOrigem = findViewById(R.id.numeroContaOrigem);
        TextView labelContaDestino = findViewById(R.id.labelContaDestino);
        EditText numeroContaDestino = findViewById(R.id.numeroContaDestino);
        EditText valorOperacao = findViewById(R.id.valor);
        Button btnOperacao = findViewById(R.id.btnOperacao);

        labelContaDestino.setVisibility(View.GONE);
        numeroContaDestino.setVisibility(View.GONE);

        valorOperacao.setHint(valorOperacao.getHint() + " debitado");
        tipoOperacao.setText("DEBITAR");
        btnOperacao.setText("Debitar");

        btnOperacao.setOnClickListener(
                v -> {
                    //TODO lembrar de implementar validação do número da conta e do valor da operação, antes de efetuar a operação de débito.
                    // O método abaixo está sendo chamado, mas precisa ser implementado na classe BancoViewModel para funcionar.
                    String numOrigem = numeroContaOrigem.getText().toString();
                    double valor = 0;
                    if(!valorOperacao.getText().toString().equals("")){ // verifica se o valor informado não é vazio
                        valor = Double.valueOf(valorOperacao.getText().toString()); // se não for vazio, atribui à "valor"
                    }
                    viewModel.debitar(numOrigem, valor); // debita o valor na conta
                    finish();
                    // apresenta a mensagem toast na tela de acordo com o ocorrido na validação (vem de bancoViewModel):
                    viewModel.mensagem.observe(this, msg -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
                }
        );
    }
}