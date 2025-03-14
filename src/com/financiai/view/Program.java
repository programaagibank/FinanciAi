package com.financiai.view;

import com.financiai.dao.Conexao;
import com.financiai.services.Price;

import java.sql.Connection;

public class Program {
    public static void main(String[] args) {
        System.out.println("Testando conexão com o banco de dados...");
        try (Connection conexao = Conexao.conectar()) {
            if (conexao != null) {
                System.out.println("Conexão estabelecida com sucesso!");
            } else {
                System.out.println("Falha ao conectar ao banco de dados.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Chamando o método para exibir o logo
        exibirLogo();

    }

    public static void exibirLogo() {
        System.out.println();
        System.out.println("███████╗██╗███╗   ██╗ █████╗ ███╗   ██╗ ██████╗██╗ █████╗ ██╗");
        System.out.println("██╔════╝██║████╗  ██║██╔══██╗████╗  ██║██╔════╝██║██╔══██╗██║");
        System.out.println("█████╗  ██║██╔██╗ ██║███████║██╔██╗ ██║██║     ██║███████║██║");
        System.out.println("██╔══╝  ██║██║╚██╗██║██╔══██║██║╚██╗██║██║     ██║██╔══██║██║");
        System.out.println("██║     ██║██║ ╚████║██║  ██║██║ ╚████║╚██████╗██║██║  ██║██║");
        System.out.println("╚═╝     ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═══╝ ╚═════╝╚═╝╚═╝  ╚═╝╚═╝");
        System.out.println();
        System.out.println("           Sua solução financeira inteligente!");
        System.out.println();
    }
}

