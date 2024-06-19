package me.silvana.desafio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static me.silvana.desafio.BigDecimalConverter.BRAZIL_LOCALE;

public class Main {
    private static final Double MINIMAL_WAGE = 1212.0;

    public static void main(String[] args) {
        var funcionariosOptional = CSVLoader.loadFuncionarios();
        if (funcionariosOptional.isEmpty()) {
            System.err.println("Unable to load Funcionarios.");
            System.exit(1);
        }

        var funcionarios = funcionariosOptional.get();

        removeJoao(funcionarios);
        printAllFuncionarios(funcionarios);
        updateSalarios(funcionarios);

        var funcionariosAggregate = aggregateFuncionariosByFuncao(funcionarios);
        printAllFuncionariosByFuncao(funcionariosAggregate);

        printAllFuncionariosBornInMonth(funcionarios, 10);
        printAllFuncionariosBornInMonth(funcionarios, 12);

        printOldestFuncionario(funcionarios);
        printAllFuncionariosByAlphabeticalOrderedName(funcionarios);
        printTotalSalarioOfAllFuncionarios(funcionarios);
        printAmountOfMinimalWagesPerFuncionario(funcionarios);
    }

    private static void removeJoao(List<Funcionario> funcionarios) {
        for (var funcionario : funcionarios) {
            if (funcionario.getNome().equalsIgnoreCase("João")) {
                funcionarios.remove(funcionario);
                break;
            }
        }
    }

    private static void printAllFuncionarios(List<Funcionario> funcionarios) {
        funcionarios.forEach(System.out::println);
    }

    private static void updateSalarios(List<Funcionario> funcionarios) {
        funcionarios.forEach(funcionario -> {
            var salario = funcionario.getSalario();
            var salarioIncrease = salario.multiply(new BigDecimal("0.10"));

            salario = salario.add(salarioIncrease);
            funcionario.setSalario(salario);
        });
    }

    private static Map<String, List<Funcionario>> aggregateFuncionariosByFuncao(List<Funcionario> funcionarios) {
        var funcionariosMap = new HashMap<String, List<Funcionario>>();

        funcionarios.forEach(funcionario -> {
            var funcao = funcionario.getFuncao();

            if (funcionariosMap.containsKey(funcao)) {
                funcionariosMap.get(funcao).add(funcionario);
                return;
            }

            var list = new ArrayList<Funcionario>();
            list.add(funcionario);

            funcionariosMap.put(funcao, list);
        });

        return funcionariosMap;
    }

    private static void printAllFuncionariosByFuncao(Map<String, List<Funcionario>> funcionariosMap) {
        funcionariosMap.keySet().forEach(funcao -> {
            System.out.println("\nFuncionarios na Funcao: " + funcao);
            System.out.println("===============================================");
            funcionariosMap.get(funcao).forEach(System.out::println);
        });
    }

    private static void printAllFuncionariosBornInMonth(List<Funcionario> funcionarios, int month) {
        System.out.println("\nFuncionarios nascidos no mes: " + month);
        System.out.println("===============================================");
        funcionarios.stream()
                .filter(funcionario -> funcionario.getDataNascimento().getMonthValue() == month)
                .forEach(System.out::println);
    }

    private static void printOldestFuncionario(List<Funcionario> funcionarios) {
        var oldestFuncionario = Collections.min(
                funcionarios,
                Comparator.comparing(Funcionario::getDataNascimento)
        );

        var now = LocalDate.now();
        var period = Period.between(oldestFuncionario.getDataNascimento(), now);

        System.out.println("\nFuncionario mais velho:");
        System.out.println("===============================================");
        System.out.printf(
                "Funcionario { nome = '%s', idade: '%d' }\n",
                oldestFuncionario.getNome(),
                period.getYears()
        );
    }

    private static void printAllFuncionariosByAlphabeticalOrderedName(List<Funcionario> funcionarios) {
        funcionarios.sort(Comparator.comparing(Funcionario::getNome));

        System.out.println("\nFuncionarios por ordem alfabetica:");
        System.out.println("===============================================");
        funcionarios.forEach(System.out::println);
    }

    private static void printTotalSalarioOfAllFuncionarios(List<Funcionario> funcionarios) {
        var totalSalario = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("\nSalario total de todos os funcionarios: ");
        System.out.println("===============================================");
        System.out.println("R$ " + BigDecimalConverter.convertToString(totalSalario));
    }

    private static void printAmountOfMinimalWagesPerFuncionario(List<Funcionario> funcionarios) {
        Map<Funcionario, Double> minimalWageRatios = funcionarios.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        funcionario -> funcionario.getSalario().doubleValue() / MINIMAL_WAGE
                ));

        System.out.println("\nSalarios minimos por funcionario:");
        System.out.println("===============================================");
        minimalWageRatios.forEach((funcionario, minimalWages) -> {
            System.out.printf(
                    BRAZIL_LOCALE,
                    "Funcionario { nome = '%s', salarios_minimos = '%.2f' }\n",
                    funcionario.getNome(),
                    minimalWages
            );
        });
    }
}
