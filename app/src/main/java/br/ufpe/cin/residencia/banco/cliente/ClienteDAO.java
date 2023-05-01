package br.ufpe.cin.residencia.banco.cliente;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.Map;

import br.ufpe.cin.residencia.banco.conta.Conta;

@Dao
public interface ClienteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adicionar(Cliente c);

    @Update
    void atualizar(Cliente c);

    @Delete
    void remover(Cliente c);

    // lista os clientes por ordem alfabética de nome
    @Query("SELECT * FROM clientes ORDER BY nome ASC")
    LiveData<List<Cliente>> clientes();

    @Query("SELECT * FROM clientes WHERE cpf = :cpfProcurado")
    Cliente buscarPeloCPF(String cpfProcurado);

    @Query("SELECT * FROM clientes WHERE nome = :nomeProcurado")
    List<Cliente> buscarPeloNome(String nomeProcurado);

    // https://developer.android.com/training/data-storage/room/accessing-data?hl=pt-br#multimap

    // é possível consultar colunas de várias tabelas sem definir uma classe de dados extra, criando métodos de consulta que retornem um multimapa:
    @Query("SELECT * FROM clientes JOIN contas ON clientes.cpf = contas.cpfCliente")
    Map<Cliente, List<Conta>> carregarClienteEContas();
}
