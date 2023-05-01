package br.ufpe.cin.residencia.banco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.residencia.banco.conta.Conta;
import br.ufpe.cin.residencia.banco.conta.ContaAdapter;

//Ver anotações TODO no código
public class PesquisarActivity extends AppCompatActivity {
    BancoViewModel viewModel;
    ContaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar);
        viewModel = new ViewModelProvider(this).get(BancoViewModel.class);
        EditText aPesquisar = findViewById(R.id.pesquisa);
        Button btnPesquisar = findViewById(R.id.btn_Pesquisar);
        RadioGroup tipoPesquisa = findViewById(R.id.tipoPesquisa);
        RecyclerView rvResultado = findViewById(R.id.rvResultado);
        adapter = new ContaAdapter(getLayoutInflater());
        rvResultado.setLayoutManager(new LinearLayoutManager(this));
        rvResultado.setAdapter(adapter);

        btnPesquisar.setOnClickListener(
                v -> {
                    String oQueFoiDigitado = aPesquisar.getText().toString();
                    //TODO implementar a busca de acordo com o tipo de busca escolhido pelo usuário
                    // ANGELO ABAIXO
                    if(oQueFoiDigitado.equals("")){ // verifica se o valor do campo de busca é vazio
                        Toast.makeText(this, "Digite algum termo para pesquisar", Toast.LENGTH_SHORT).show();
                    }else{
                        switch(tipoPesquisa.getCheckedRadioButtonId()) {
                            case R.id.peloNomeCliente: // caso seja escolhida a opção de pesquisa por nome
                                viewModel.buscarPeloNome(oQueFoiDigitado); // busca pelo nome
                                break;
                            case R.id.peloCPFcliente:// caso seja escolhida a opção de pesquisa por cpf
                                viewModel.buscarPeloCPF(oQueFoiDigitado); // busca pelo cpf
                                break;
                            case R.id.peloNumeroConta:// caso seja escolhida a opção de pesquisa por número de conta
                                viewModel.buscarPeloNumero(oQueFoiDigitado); // busca pelo número da conta
                                break;
                        }
                    }
                    // ANGELO ACIMA
                }
        );
        //TODO atualizar o RecyclerView com resultados da busca na medida que encontrar
        // ANGELO ABAIXO
        // atualiza a lista de contas de acordo com o resultado obtido na busca
        viewModel.contas.observe(this, novaListaContas -> {
            // verifica se o resultado não é vazio (encontrou conta com a busca realizada):
            if(!novaListaContas.isEmpty()){
                rvResultado.setVisibility(View.VISIBLE); // mostra o resultado na tela
            }else{ // se o resultado for vazio:
                rvResultado.setVisibility(View.INVISIBLE); // não mostra nada
                // mostra uma mensagem na tela informando que não encontrou nada na busca:
                Toast.makeText(this, "Nenhuma conta encontrada", Toast.LENGTH_SHORT).show();
            }
            adapter.submitList(novaListaContas); // atualiza o recycler view com a lista de contas
        });

        // ANGELO ACIMA
    }
    protected void onStart (){ // criado para que caso o usuário edite alguma conta encontrada na pesquisa, ao voltar, a tela seja atualizada e mostre o resultado da pesquisa com os dados mais atuais da conta, sem precisar clicar no botão "pesquisar".
        super.onStart();
        RecyclerView rvResultado = findViewById(R.id.rvResultado);
        EditText aPesquisar = findViewById(R.id.pesquisa);
        RadioGroup tipoPesquisa = findViewById(R.id.tipoPesquisa);
        String oQueFoiDigitado = aPesquisar.getText().toString();

        if(oQueFoiDigitado.equals("")){
            Toast.makeText(this, "Digite algum termo para pesquisar", Toast.LENGTH_SHORT).show();
        }else{
            switch(tipoPesquisa.getCheckedRadioButtonId()) {
                case R.id.peloNomeCliente:
                    viewModel.buscarPeloNome(oQueFoiDigitado);
                    break;
                case R.id.peloCPFcliente:
                    viewModel.buscarPeloCPF(oQueFoiDigitado);
                    break;
                case R.id.peloNumeroConta:
                    viewModel.buscarPeloNumero(oQueFoiDigitado);
                    break;
            }
        }
        viewModel.contas.observe(this, novaListaContas -> {
            if(!novaListaContas.isEmpty()){
                rvResultado.setVisibility(View.VISIBLE);
            }else{
                rvResultado.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Nenhuma conta encontrada", Toast.LENGTH_SHORT).show();
            }
            adapter.submitList(novaListaContas);
        });
    }
}