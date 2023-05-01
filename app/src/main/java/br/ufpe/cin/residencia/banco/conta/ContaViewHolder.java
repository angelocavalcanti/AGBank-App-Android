package br.ufpe.cin.residencia.banco.conta;

import static br.ufpe.cin.residencia.banco.conta.EditarContaActivity.KEY_NUMERO_CONTA;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;

import br.ufpe.cin.residencia.banco.R;

//Ver anotações TODO no código
public class ContaViewHolder  extends RecyclerView.ViewHolder {
    TextView nomeCliente = null;
    TextView infoConta = null;
    ImageView icone = null;

    public ContaViewHolder(@NonNull View linha) {
        super(linha);
        this.nomeCliente = linha.findViewById(R.id.nomeCliente);
        this.infoConta = linha.findViewById(R.id.infoConta);
        this.icone = linha.findViewById(R.id.icone);
    }

    void bindTo(Conta c) {
        this.nomeCliente.setText(c.nomeCliente);
        this.infoConta.setText(c.numero + " | " + "Saldo atual: " + NumberFormat.getCurrencyInstance().format(c.saldo));
        //TODO Falta atualizar a imagem de acordo com o valor do saldo atual
        // ANGELO ABAIXO
        // Agora está atualizando a imagem:
        if(c.saldo > 0){
            this.icone.setImageResource(R.drawable.ok); // se saldo for maior que zero, mostra essa imagem como ícone
        }else{
            this.icone.setImageResource(R.drawable.delete); // se saldo for zero ou negativo, mostra essa imagem como ícone
        }
        // ANGELO ACIMA

        this.addListener(c.numero);
    }

    public void addListener(String numeroConta) {
        this.itemView.setOnClickListener(
                v -> {
                    Context c = this.itemView.getContext();
                    Intent i = new Intent(c, EditarContaActivity.class);
                    //TODO Está especificando a Activity mas não está passando o número da conta pelo Intent
                    // ANGELO ABAIXO
                    // Passa o número de referência da conta pelo intent (para que a conta possa ser manipulada na tela de edição):
                    i.putExtra(KEY_NUMERO_CONTA, numeroConta);
                    // ANGELO ACIMA
                    c.startActivity(i);

                }
        );
    }
}
