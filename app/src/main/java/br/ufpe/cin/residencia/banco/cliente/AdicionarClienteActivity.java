package br.ufpe.cin.residencia.banco.cliente;

import static br.ufpe.cin.residencia.banco.conta.AdicionarContaActivity.validarCPF;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.ufpe.cin.residencia.banco.R;
import br.ufpe.cin.residencia.banco.cliente.Cliente;
import br.ufpe.cin.residencia.banco.cliente.ClienteViewModel;

public class AdicionarClienteActivity extends AppCompatActivity {

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

        btnAtualizar.setText(R.string.btn_inserir);
        btnRemover.setVisibility(View.GONE);

        btnAtualizar.setOnClickListener(
                v -> {
                    String nomeCliente = campoNome.getText().toString();
                    String cpfCliente = campoCPF.getText().toString();
                    String msg = "";
                    // verifica se o cpf não é vazio e é válido:
                    if(!cpfCliente.isEmpty() && validarCPF(cpfCliente)) {
                        // verifica se o nome tem 5 ou mais caracteres:
                        if(nomeCliente.length() >= 5) {
                            // cria um objeto cliente com os dados passados:
                            Cliente c = new Cliente(cpfCliente, nomeCliente);
                            // insere o cliente no banco de dados:
                            viewModel.inserir(c);
                            finish();
                            msg = getString(R.string.msg_cliente_adicionado);
                        }else {
                            msg = getString(R.string.msg_minimo_caracteres, 5);
                        }
                    }else {
                        msg = getString(R.string.msg_cpf_valido);
                    }
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }
        );
    }
}