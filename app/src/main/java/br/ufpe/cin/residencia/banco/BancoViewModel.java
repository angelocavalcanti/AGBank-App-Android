package br.ufpe.cin.residencia.banco;

import static br.ufpe.cin.residencia.banco.conta.EditarContaActivity.KEY_NUMERO_CONTA;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.ufpe.cin.residencia.banco.conta.Conta;
import br.ufpe.cin.residencia.banco.conta.ContaRepository;

//Ver anotações TODO no código
public class BancoViewModel extends AndroidViewModel {
    private ContaRepository repository;
// ANGELO ABAIXO
    private MutableLiveData<Conta> _contaAtual = new MutableLiveData<>();
    public LiveData<Conta> contaAtual = _contaAtual;
    private MutableLiveData<List<Conta>> _contas = new MutableLiveData<>();
    public LiveData<List<Conta>> contas = _contas;
    private MutableLiveData<String> _msg = new MutableLiveData<>();
    public LiveData<String> mensagem = _msg;
    public LiveData<List<Conta>> contasSaldo;
//ANGELO ACIMA
    public BancoViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ContaRepository(BancoDB.getDB(application).contaDAO());
        this.contasSaldo = repository.getContas(); // para pegar todas as contas no banco de dados
    }

    void transferir(String numeroContaOrigem, String numeroContaDestino, double valor) {
        //TODO implementar transferência entre contas (lembrar de salvar no BD os objetos Conta modificados)
        // ANGELO ABAIXO
        new Thread(() -> {
            Conta origem = repository.buscarPeloNumero(numeroContaOrigem);
            Conta destino = repository.buscarPeloNumero(numeroContaDestino);
            String msg = "";
            if(origem != null){ // verifica se a conta de origem informada existe
                if(destino != null){ // se a conta de origem for encontrada, verifica se a conta de destino informada existe
                    if(valor > 0) { // se as contas existirem, verifica se o valor informado é maior que zero
                        if (origem.saldo >= valor) { // verifica se há saldo suficiente na conta de origem para realizar a transferência
                            origem.transferir(destino, valor); // realiza a transferência
                            repository.atualizar(origem); // atualiza a conta de origem (com novo saldo) no banco de dados
                            repository.atualizar(destino); // atualiza a conta de destino (com novo saldo) no banco de dados
                            msg = "Transferência realizada com sucesso!";
                        }else{
                            msg = "Saldo insuficiente";
                        }
                    }else{
                        msg = "Digite um valor maior que zero";
                    }
                }else{
                    msg = "Conta de destino não encontrada";
                }
            }else {
                msg = "Conta de origem não encontrada";
            }
            _msg.postValue(msg);
        }).start();
        // ANGELO ACIMA
    }

    void creditar(String numeroConta, double valor) {
        //TODO implementar creditar em conta (lembrar de salvar no BD o objeto Conta modificado)
        // ANGELO ABAIXO
        new Thread(() -> {
            Conta conta = repository.buscarPeloNumero(numeroConta);
            String msg = "";
            if (conta != null) { // verifica se a conta informada existe
                if (valor > 0) { // se a conta existir, verifica se o valor informado para crédito é maior que zero
                    conta.creditar(valor); // credita o valor informado na conta informada
                    repository.atualizar(conta); //atualiza a conta (com novo salo) no banco de dados
                    msg = "Crédito realizado com sucesso!";
                } else {
                    msg = "Digite um valor maior que zero";
                }
            } else {
                msg = "Conta não encontrada";
            }
            _msg.postValue(msg);
        }).start();
        // ANGELO ACIMA
    }

    void debitar(String numeroConta, double valor) {
        //TODO implementar debitar em conta (lembrar de salvar no BD o objeto Conta modificado)
        // ANGELO ABAIXO
        new Thread(() -> {
            Conta conta = repository.buscarPeloNumero(numeroConta);
            String msg = "";
            if(conta != null) { // verifica se a conta informada existe
                if(valor > 0){ // se a conta existir, verifica se o valor informado para débito é maior que zero
                    if(conta.saldo >= valor){  // verifica se há saldo suficiente na conta para realizar o débito
                        conta.debitar(valor); // debita o valor informado na conta informada
                        repository.atualizar(conta); //atualiza a conta (com novo salo) no banco de dados
                        msg = "Débito realizado com sucesso!";
                    }else{
                        msg = "Saldo insuficiente";
                    }
                }else{
                    msg = "Digite um valor maior que zero";
                }
            }else{
                msg = "Conta não encontrada";
            }
            _msg.postValue(msg);
        }).start();
        // ANGELO ACIMA
    }

    void buscarPeloNome(String nomeCliente) {
        //TODO implementar busca pelo nome do Cliente
        // ANGELO ABAIXO
        new Thread(() -> {
            List<Conta> c = this.repository.buscarPeloNome(nomeCliente);
            _contas.postValue(c);
        }).start();
        // ANGELO ACIMA
    }

    void buscarPeloCPF(String cpfCliente) {
        //TODO implementar busca pelo CPF do Cliente
        // ANGELO ABAIXO
        new Thread(() -> {
            List<Conta> c = this.repository.buscarPeloCPF(cpfCliente);
            _contas.postValue(c);
        }).start();
        // ANGELO ACIMA
    }

    void buscarPeloNumero(String numeroConta) {
        //TODO implementar busca pelo número da Conta
        // ANGELO ABAIXO
        new Thread(() -> {
            Conta c = this.repository.buscarPeloNumero(numeroConta);
            _contaAtual.postValue(c);
            if(c != null){ // verifica se a conta foi encontrada no banco de dados
                List<Conta> lc = new ArrayList<>(); // lista criada para ser usada na pesquisa (PesquisarActivity) que mostra um tipo Lista de Contas na tela e não um tipo Conta.
                lc.add(c); // adiciona a conta encontrada em uma lista de contas que conterá somente a conta encontrada
                _contas.postValue(lc); // atualiza a lista de contas
            }else{
                _contas.postValue(new ArrayList<>()); // caso não seja encontrada uma conta pelo número, atualiza a lista como vazia
            }
        }).start();
        // ANGELO ACIMA
    }

    public double saldoTotalBanco(){
        double saldoTotal = 0;

        if(contasSaldo.getValue() != null){ // verifica se há alguma conta na lista de contas
            for(Conta conta:contasSaldo.getValue()){ // se houver, soma todos os saldos das contas na lista de contas
                saldoTotal = saldoTotal + conta.saldo;
            }
        }
        return saldoTotal; // retorna o valor total. Retorna 0 caso não haja conta na lista de contas
    }
}
