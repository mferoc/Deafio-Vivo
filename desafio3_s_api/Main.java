import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Main {
    public static void main(String[] args) {
        final DateTimeFormatter tempoVoltaFormat = new DateTimeFormatterBuilder().appendPattern("m:ss.SSS")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0).toFormatter();

        final List<Corrida> corridas = new ArrayList<>(0);
        try (final BufferedReader bf = new BufferedReader(new FileReader("run.csv"))) {
            bf.readLine(); // Ignora a primeira linha
            String row;
            while ((row = bf.readLine()) != null) {
                String[] linha = row.split(";");

                final LocalTime hora = LocalTime.parse(linha[0]);
                final SuperHeroi superHeroi = new SuperHeroi();
                superHeroi.setCodigo(Integer.parseInt(linha[1].split("–")[0]));
                superHeroi.setNome(linha[1].split("–")[1]);
                final int volta = Integer.parseInt(linha[2]);
                final LocalTime tempoVolta = LocalTime.parse(linha[3], tempoVoltaFormat);
                final double velocidadeMedia = Double.parseDouble(linha[4].replace(",", "."));

                final Corrida corrida = new Corrida();
                corrida.setHora(hora);
                corrida.setSuperHeroi(superHeroi);
                corrida.setVolta(volta);
                corrida.setTempoVolta(tempoVolta);
                corrida.setVelocidadeMedia(velocidadeMedia);

                corridas.add(corrida);
            }
        } catch (FileNotFoundException e) {
            System.err.printf("Arquivo não encontrado %s\n", e.getMessage());
        } catch (IOException e) {
            System.err.printf("Erro ao ler o arquivo %s\n", e.getMessage());
        }

        Map<Integer, List<Corrida>> groupBySuperHero = corridas.stream()
                .collect(Collectors.groupingBy(corrida -> corrida.getSuperHeroi().getCodigo()));

        // System.out.println(groupBySuperHero); //Testando o agrupamento por super
        // heroi

        /*
         * LocalTime plus(long amountToAdd, TemporalUnit unit)
         */
        groupBySuperHero.entrySet().stream().map(entry -> {
            Ranking r = new Ranking();
            r.setCodigo(entry.getKey());
            r.setNome(entry.getValue().get(0).getSuperHeroi().getNome());
            r.setVoltas(entry.getValue().size());
            entry.getValue().stream().reduce(LocalTime.MIDNIGHT,
                    (acc, corrida) -> acc.plus(Duration.ofNanos(corrida.getTempoVolta().toNanoOfDay())),
                    LocalTime::plus);
            return r;
        });
        System.out.println(groupBySuperHero);
    }

}

class Ranking {
    private int posicao;
    private int codigo;
    private String nome;
    private int voltas;
    private LocalTime tempoTotal;

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getVoltas() {
        return voltas;
    }

    public void setVoltas(int voltas) {
        this.voltas = voltas;
    }

    public LocalTime getTempoTotal() {
        return tempoTotal;
    }

    public void setTempoTotal(LocalTime tempoTotal) {
        this.tempoTotal = tempoTotal;
    }

}

class Corrida {
    private LocalTime hora;
    private SuperHeroi superHeroi;
    private int volta;
    private LocalTime tempoVolta;
    private double velocidadeMedia;

    /**
     * @return the hora
     */
    public LocalTime getHora() {
        return hora;
    }

    /**
     * @return the superHeroi
     */
    public SuperHeroi getSuperHeroi() {
        return superHeroi;
    }

    /**
     * @return the tempoVolta
     */
    public LocalTime getTempoVolta() {
        return tempoVolta;
    }

    /**
     * @return the velocidadeMedia
     */
    public double getVelocidadeMedia() {
        return velocidadeMedia;
    }

    /**
     * @return the volta
     */
    public int getVolta() {
        return volta;
    }

    /**
     * @param hora the hora to set
     */
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    /**
     * @param superHeroi the superHeroi to set
     */
    public void setSuperHeroi(SuperHeroi superHeroi) {
        this.superHeroi = superHeroi;
    }

    /**
     * @param tempoVolta the tempoVolta to set
     */
    public void setTempoVolta(LocalTime tempoVolta) {
        this.tempoVolta = tempoVolta;
    }

    /**
     * @param velocidadeMedia the velocidadeMedia to set
     */
    public void setVelocidadeMedia(double velocidadeMedia) {
        this.velocidadeMedia = velocidadeMedia;
    }

    /**
     * @param volta the volta to set
     */
    public void setVolta(int volta) {
        this.volta = volta;
    }

    @Override
    public String toString() {
        return "Corrida [hora=" + this.hora.toString() + ", superHeroi=" + this.superHeroi.toString() + ", volta="
                + this.volta + ", tempoVolta=" + this.tempoVolta.toString() + ", velocidadeMedia="
                + this.velocidadeMedia + " ]\n";
    }
}

class SuperHeroi {
    private int codigo;
    private String nome;

    /**
     * @return the codigo
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "SuperHeroi [codigo=" + this.codigo + ", nome=" + this.nome + "]";
    }
}