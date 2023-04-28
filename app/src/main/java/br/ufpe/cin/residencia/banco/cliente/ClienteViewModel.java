package br.ufpe.cin.residencia.banco.cliente;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import br.ufpe.cin.residencia.banco.BancoDB;
import br.ufpe.cin.residencia.banco.cliente.Cliente;
import br.ufpe.cin.residencia.banco.cliente.ClienteRepository;

public class ClienteViewModel extends AndroidViewModel {
    private ClienteRepository repository;
    public LiveData<List<Cliente>> clientes;
    private MutableLiveData<Cliente> _clienteAtual = new MutableLiveData<>();
    public LiveData<Cliente> clienteAtual = _clienteAtual;

    public ClienteViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ClienteRepository(BancoDB.getDB(application).clienteDAO());
        this.clientes = repository.getClientes();
    }

    void inserir(Cliente c) {
        new Thread(() -> repository.inserir(c)).start();
    }

    void atualizar(Cliente c) { new Thread(() -> this.repository.atualizar(c)).start(); }

    void remover(Cliente c) { new Thread(() -> this.repository.remover(c)).start(); }

    void buscarPeloCPF(String cpfCliente) {
        new Thread(() -> {
            Cliente c = this.repository.buscarPeloCPF(cpfCliente);
            _clienteAtual.postValue(c);
        }).start();
    }
}
