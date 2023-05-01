package br.ufpe.cin.residencia.banco.cliente;

import static br.ufpe.cin.residencia.banco.cliente.EditarClienteActivity.KEY_CPF_CLIENTE;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.ufpe.cin.residencia.banco.R;
import br.ufpe.cin.residencia.banco.cliente.Cliente;
import br.ufpe.cin.residencia.banco.cliente.EditarClienteActivity;

public class ClienteViewHolder extends RecyclerView.ViewHolder {
    TextView nomeCliente = null;
    TextView infoCliente = null;
    ImageView icone = null;

    public ClienteViewHolder(@NonNull View linha) {
        super(linha);
        this.nomeCliente = linha.findViewById(R.id.nome);
        this.infoCliente = linha.findViewById(R.id.infoCliente);
        this.icone = linha.findViewById(R.id.icone);
    }

    void bindTo(Cliente c) {
        this.nomeCliente.setText(c.nome);
        // apresenta o cpf no formato xxx.xxx.xxx-xx
        this.infoCliente.setText(this.itemView.getContext().getString(R.string.txt_viewHolder_cpf_cliente, (c.cpf.substring(0,3)+"."+c.cpf.substring(3,6)+"."+c.cpf.substring(6,9)+"-"+c.cpf.substring(9,11))));
        this.addListener(c.cpf);
    }

    public void addListener(String cpf) {
        this.itemView.setOnClickListener(
                v -> {
                    Context c = this.itemView.getContext();
                    Intent i = new Intent(c, EditarClienteActivity.class);
                    // Passa o número de referência do cliente pelo intent (para que o cliente possa ser manipulado na tela de edição):
                    i.putExtra(KEY_CPF_CLIENTE, cpf);
                    c.startActivity(i);
                }
        );
    }
}
