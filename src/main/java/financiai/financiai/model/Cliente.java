package financiai.financiai.model;

public class Cliente {

    private String nome;
    private Double renda_mensal;
    private String cpf;

    public Cliente() {
    }

    public Cliente(String nome, String cpf, double renda_mensal) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser nulo ou vazio");
        }
        if (!validarCPF(cpf)) {
            throw new IllegalArgumentException("CPF inválido");
        }
        this.nome = nome;
        this.cpf = cpf.replaceAll("[^0-9]", ""); // Remove formatação
        this.renda_mensal = renda_mensal;
    }

    private boolean validarCPF(String cpf) {
        // Implementação da validação de CPF
        cpf = cpf.replaceAll("[^0-9]", "");
        return cpf.length() == 11; // Validação básica
    }
    // Getters e Setters


    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getRenda_mensal() {
        return renda_mensal;
    }

    public void setRenda_mensal(Double renda_mensal) {
        this.renda_mensal = renda_mensal;
    }
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}