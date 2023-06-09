package br.ufpe.cin.residencia.banco.conta;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import br.ufpe.cin.residencia.banco.BancoDB;

//Ver métodos anotados com TODO
public class ContaViewModel extends AndroidViewModel {

    private ContaRepository repository;
    public LiveData<List<Conta>> contas;
    private MutableLiveData<Conta> _contaAtual = new MutableLiveData<>();
    public LiveData<Conta> contaAtual = _contaAtual;

    public ContaViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ContaRepository(BancoDB.getDB(application).contaDAO());
        this.contas = repository.getContas();
    }

    void inserir(Conta c) {
        new Thread(() -> repository.inserir(c)).start();
    }

    void atualizar(Conta c) {
        //TODO implementar
        // ANGELO ABAIXO
        new Thread(() -> this.repository.atualizar(c)).start();
        // ANGELO ACIMA
    }

    void remover(Conta c) {
        //TODO implementar
        // ANGELO ABAIXO
        new Thread(() -> this.repository.remover(c)).start();
        // ANGELO ACIMA
    }

    void buscarPeloNumero(String numeroConta) {
        //TODO implementar
        // ANGELO ABAIXO
        new Thread(() -> {
            Conta c = this.repository.buscarPeloNumero(numeroConta);
            _contaAtual.postValue(c);
        }).start();
        // ANGELO ACIMA
    }
}
