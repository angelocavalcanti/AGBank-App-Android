package br.ufpe.cin.residencia.banco.conta;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

//Ver anotações TODO no código
@Dao
public interface ContaDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adicionar(Conta c);

    //TODO incluir métodos para atualizar conta e remover conta

    // ANGELO ABAIXO
    @Update
    void atualizar(Conta c);
    @Delete
    void remover(Conta c);
    // ANGELO ACIMA

    @Query("SELECT * FROM contas ORDER BY numero ASC")
    LiveData<List<Conta>> contas();

    //TODO incluir métodos para buscar pelo (1) número da conta, (2) pelo nome e (3) pelo CPF do Cliente

    // ANGELO ABAIXO
    @Query("SELECT * FROM contas WHERE numero = :numeroProcurado")
    Conta buscarPeloNumero(String numeroProcurado);
    @Query("SELECT * FROM contas WHERE nomeCliente LIKE :nomeProcurado")
    List<Conta> buscarPeloNome(String nomeProcurado);
    @Query("SELECT * FROM contas WHERE cpfCliente = :cpfProcurado")
    List<Conta> buscarPeloCPF(String cpfProcurado);
    // ANGELO ACIMA
}
