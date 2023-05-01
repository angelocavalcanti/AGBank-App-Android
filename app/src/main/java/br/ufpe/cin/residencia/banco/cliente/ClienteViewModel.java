package br.ufpe.cin.residencia.banco.cliente;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Map;

import br.ufpe.cin.residencia.banco.BancoDB;
import br.ufpe.cin.residencia.banco.cliente.Cliente;
import br.ufpe.cin.residencia.banco.cliente.ClienteRepository;
import br.ufpe.cin.residencia.banco.conta.Conta;

public class ClienteViewModel extends AndroidViewModel {
    private ClienteRepository repository;
    public LiveData<List<Cliente>> clientes;
    private MutableLiveData<Cliente> _clienteAtual = new MutableLiveData<>();
    public LiveData<Cliente> clienteAtual = _clienteAtual;

    private MutableLiveData<Map<Cliente, List<Conta>>> _clienteMap = new MutableLiveData<>();
    public LiveData<Map<Cliente, List<Conta>>> clienteMap = _clienteMap;

    public ClienteViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ClienteRepository(BancoDB.getDB(application).clienteDAO());
        this.clientes = repository.getClientes();
    }

    void inserir(Cliente c) { new Thread(() -> repository.inserir(c)).start(); }

    void atualizar(Cliente c) { new Thread(() -> this.repository.atualizar(c)).start(); }

    void remover(Cliente c) { new Thread(() -> this.repository.remover(c)).start(); }

    public void buscarPeloCPF(String cpfCliente) {
        new Thread(() -> {
            Cliente c = this.repository.buscarPeloCPF(cpfCliente);
            _clienteAtual.postValue(c);
        }).start();
    }

    public void carregarClienteEContas(){
        new Thread(() -> {
            Map<Cliente, List<Conta>> m = this.repository.carregarClienteEContas();
            _clienteMap.postValue(m);
        }).start();
    }
}
