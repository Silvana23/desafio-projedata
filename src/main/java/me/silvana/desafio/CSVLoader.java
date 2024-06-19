package me.silvana.desafio;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class CSVLoader {
    public static Optional<List<Funcionario>> loadFuncionarios() {
        try (
                var inputStream = CSVLoader.class.getResourceAsStream("/funcionarios.csv");
                var reader = new InputStreamReader(Objects.requireNonNull(inputStream));
                var parser = new CSVParser(
                        reader,
                        CSVFormat.Builder.create()
                                .setSkipHeaderRecord(true)
                                .setHeader("nome", "data_nascimento", "salario", "funcao")
                                .build()
                )
        ) {
            var funcionarios = parser.stream()
                    .map(record -> {
                        var nome = record.get("nome");
                        var dataNascimento = LocalDate.parse(
                                record.get("data_nascimento"),
                                DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        );
                        var salario = new BigDecimal(record.get("salario"));
                        var funcao = record.get("funcao");

                        return new Funcionario(nome, dataNascimento, salario, funcao);
                    })
                    .collect(Collectors.toList());

            return Optional.of(funcionarios);
        } catch (IOException e) {
            System.err.println("Unable to open the CSV file.");
            System.err.println(e.getMessage());
            return Optional.empty();
        }
    }
}
