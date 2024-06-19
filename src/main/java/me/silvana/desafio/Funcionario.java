package me.silvana.desafio;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Funcionario extends Pessoa {


    private BigDecimal salario;
    private String funcao;

    public Funcionario(String nome, LocalDate dataNascimento, BigDecimal salario, String funcao) {
        super(nome, dataNascimento);
        this.salario = salario;
        this.funcao = funcao;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    @Override
    public String toString() {

        return "Funcionario { nome = '%s', data_nascimento = '%s', salario = '%s', funcao = '%s' }".formatted(
                getNome(),
                getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                BigDecimalConverter.convertToString(getSalario()),
                getFuncao()
        );
    }
}
