package br.ufpe.cin.residencia.banco.cliente;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "clientes")
public class Cliente {
    @PrimaryKey
    @NonNull
    public String cpf;
    @NonNull
    public String nome;

    // ANGELO ABAIXO
    public Cliente(@NonNull String cpf, @NonNull String nome) {
        this.cpf = cpf;
        this.nome = nome;
    }
    //ANGELO ACIMA
}