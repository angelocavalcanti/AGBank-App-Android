package br.ufpe.cin.residencia.banco.cliente;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.ufpe.cin.residencia.banco.cliente.Cliente;

@Dao
public interface ClienteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adicionar(Cliente c);

    @Update
    void atualizar(Cliente c);
    @Delete
    void remover(Cliente c);

    @Query("SELECT * FROM clientes ORDER BY nome ASC")
    LiveData<List<Cliente>> clientes();

    @Query("SELECT * FROM clientes WHERE cpf = :cpfProcurado")
    Cliente buscarPeloCPF(String cpfProcurado);

    @Query("SELECT * FROM clientes WHERE nome = :nomeProcurado")
    List<Cliente> buscarPeloNome(String nomeProcurado);
}
