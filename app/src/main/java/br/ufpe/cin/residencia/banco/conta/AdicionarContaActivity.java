package br.ufpe.cin.residencia.banco.conta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Closeable;
import java.util.List;

import br.ufpe.cin.residencia.banco.R;
import br.ufpe.cin.residencia.banco.cliente.Cliente;
import br.ufpe.cin.residencia.banco.cliente.ClienteViewModel;

//Ver anotações TODO no código
public class AdicionarContaActivity extends AppCompatActivity {

    ContaViewModel viewModel;
    ClienteViewModel viewModelCliente;

    public static final String regexSaldo = "-?\\d*+(\\.\\d*)?";
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
//        TextView labelNome = findViewById(R.id.labelNome);

        btnAtualizar.setText("Inserir");
        btnRemover.setVisibility(View.GONE);
//        labelNome.setVisibility(View.GONE); // ANGELO
//        campoNome.setVisibility(View.GONE); // ANGELO

        btnAtualizar.setOnClickListener(v -> {
//              String nomeCliente = campoNome.getText().toString(); // não será mais preciso pois o nome do cliente é cadastrado na adição de um cliente
                String cpfCliente = campoCPF.getText().toString();
                String numeroConta = campoNumero.getText().toString();
                String saldoConta = campoSaldo.getText().toString();
                //TODO: Incluir validações aqui, antes de criar um objeto Conta (por exemplo, verificar que digitou um nome com pelo menos 5 caracteres, que o campo de saldo tem de fato um número, assim por diante). Se todas as validações passarem, aí sim cria a Conta conforme linha abaixo.

                // ANGELO ABAIXO
                String msg = "";
                // verifica se o campo de número da conta não está vazio:
                if(!numeroConta.isEmpty()) {
                    // busca pelo número da conta:
                    viewModel.buscarPeloNumero(numeroConta);
                    viewModel.contaAtual.observe(this, contaExiste -> {
                        // verifica se já existe uma conta com o número de conta informado:
                        if (contaExiste == null) {
                            //if(nomeCliente.length() >= 5) {
                            // verifica se o cpf é válido:
                            if (validarCPF(cpfCliente)) {
                                // busca pelo cpf, digitado no campo de cpf, no BD:
                                viewModelCliente.buscarPeloCPF(cpfCliente);
                                viewModelCliente.clienteAtual.observe(this, cliente -> {
                                    // verifica se o viewModelCliente não retorna nulo (não encontra o cpf buscado):
                                    if (cliente != null) {
                                        // verifica se o saldo não está em branco e se segue a expressão regular:
                                        if (!saldoConta.isEmpty() && saldoConta.matches(regexSaldo)) {
                                            // cria um objeto conta com os dados passados:
                                            Conta c = new Conta(numeroConta, Double.valueOf(saldoConta), cliente.nome, cliente.cpf);
                                            // insere a conta no banco de dados:
                                            viewModel.inserir(c);
                                            finish();
                                            Toast.makeText(this, "Conta adicionada com sucesso", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(this, "Digite um saldo com números", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(this, "É necessário cadastrar um cliente com o CPF informado", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(this, "Digite um CPF válido", Toast.LENGTH_SHORT).show();
                            }
//                        }else {
//                            msg = "Digite um nome com no mínimo 5 caracteres";
//                        }
                        } else {
                            Toast.makeText(AdicionarContaActivity.this, "Conta já existe. Digite um novo número", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    msg = "Digite um número para a conta";
                }
                // se msg for diferente de vazio, entrou em algum else e recebeu uma mensagem para exibir como toast:
                if (!msg.equals("")) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }
                // ANGELO ACIMA
                //TODO: chamar o método que vai salvar a conta no Banco de Dados
            }
        );
    }

    // função para validação de cpf:
    public static boolean validarCPF(String cpf) {
        cpf = cpf.replace(".","").replace("-","").trim();
        if (cpf == null || cpf.length() != 11)
            return false;

        try {
            Long.parseLong(cpf);
        } catch (NumberFormatException e) { // CPF não possui somente números
            return false;
        }
        // retorna true se a string com os dois dígitos verificadores do cpf informado for igual à string retornada pela função calcDigVerif
        return calcDigVerif(cpf.substring(0, 9)).equals(cpf.substring(9, 11));
    }

    // função para validação de dígitos verificadores de cpf. Retorna os dois dígitos verificadores válidos de acordo com os 9 primeiros dígitos do cpf:
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