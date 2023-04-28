package br.ufpe.cin.residencia.banco.cliente;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import java.util.List;

import br.ufpe.cin.residencia.banco.cliente.Cliente;
import br.ufpe.cin.residencia.banco.cliente.Cliente;
import br.ufpe.cin.residencia.banco.cliente.ClienteDAO;

public class ClienteRepository {
    private ClienteDAO dao;
    private LiveData<List<Cliente>> clientes;

    public ClienteRepository(ClienteDAO dao) {
        this.dao = dao;
        this.clientes = dao.clientes();
    }

    public LiveData<List<Cliente>> getClientes() {
        return clientes;
    }

    @WorkerThread
    public void inserir(Cliente c) {
        dao.adicionar(c);
    }

    @WorkerThread
    public void atualizar(Cliente c) {
        dao.atualizar(c);
    }

    @WorkerThread
    public void remover(Cliente c) {
        dao.remover(c);
    }

    @WorkerThread
    public List<Cliente> buscarPeloNome(String nome) {
        return dao.buscarPeloNome(nome);
    }

    @WorkerThread
    public Cliente buscarPeloCPF(String cpf) {
        return dao.buscarPeloCPF(cpf);
    }
}
