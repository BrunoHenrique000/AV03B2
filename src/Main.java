import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static Scanner scan = new Scanner(System.in);
    public static Vaga vaga = new Vaga();
    public static Veiculo veiculo = new Veiculo();
    public static ArrayList<Vaga> ListaVagas = new ArrayList<>();
    public static ArrayList<Veiculo> ListaVeiculos = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Sistema de gerenciamento de estacionamento");

        boolean rodar = true;

        while (rodar) {
            System.out.println("Escolha o que deseja: \n");
            System.out.println("""
                    1. Cadastrar vaga
                    2. Cadastrar veículo
                    3. Registrar saída
                    4. Gerar relatório de vagas ocupadas
                    5. Gerar histórico de permanência dos veículos
                    6. Sair
                    """);

            int escolha = scan.nextInt();
            scan.nextLine();

            if (escolha < 1 || escolha > 7) {
                System.out.println("ERRO: Escolha um número válido (1 - 7)");
            }

            if (escolha == 1) {
                cadastrarVagas();
            }

            if (escolha == 2) {
                cadastrarVeiculo();
            }

            if (escolha == 3) {
                registrarSaida();
            }

            if (escolha == 4) {
                gerarRelatorio();
            }

            if (escolha == 5) {
                gerarHistorico();
            }

            if (escolha == 6) {
                rodar = false;
            }
        }
    }

    public static void cadastrarVagas() {
        System.out.println("Cadastro de vagas");

        System.out.println("Entre com o número da vaga: ");
        int numVaga = scan.nextInt();
        scan.nextLine();

        System.out.println("Entre com o tamanho da vaga (1.Pequeno | 2.Médio | 3.Grande): ");
        int tamVaga = scan.nextInt();
        scan.nextLine();

        boolean disponibilidadeVaga = true;

        Vaga vaga1 = new Vaga(numVaga, tamVaga, disponibilidadeVaga);
        ListaVagas.add(vaga1);

        System.out.println("Vaga cadastrada com sucesso! \n");
    }

    public static void cadastrarVeiculo() {
        System.out.println("Cadastro de veículos");

        System.out.println("Entre com a placa do veículo: ");
        String placaVeiculo = scan.nextLine();

        System.out.println("Entre com o modelo do veículo: ");
        String modeloVeiculo = scan.nextLine();

        System.out.println("Entre com o tamanho do veículo (1.Pequeno | 2.Médio | 3.Grande): ");
        int tamVeiculo = scan.nextInt();
        scan.nextLine();

        System.out.println("Horário de entrada HH:mm (08:00): ");
        String horaEntradaString = scan.nextLine();
        LocalTime horaEntrada = LocalTime.parse(horaEntradaString, DateTimeFormatter.ofPattern("HH:mm"));

        LocalTime horaSaida = null;

        for (Vaga vaga : ListaVagas) {
            if (vaga.isDisponibilidade() && vaga.getTamanho() == tamVeiculo) {
                Veiculo veiculo1 = new Veiculo(placaVeiculo, modeloVeiculo, tamVeiculo, horaEntrada, null);
                vaga.setDisponibilidade(false);

                ListaVeiculos.add(veiculo1);
                System.out.println("Veículo registrado e alocado com sucesso! \n");

                return;
            }

            else {
                System.out.println("Não há vagas disponíveis. \n");
            }
        }
    }

    public static void registrarSaida() {
        System.out.println("Registro de saída do veículo");
        System.out.println("Informe a placa do veículo: ");
        String placa = scan.nextLine();

        Veiculo veiculoSaida = null;

        for (Veiculo veiculo : ListaVeiculos) {
            if (veiculo.getPlaca().equalsIgnoreCase(placa) && veiculo.getHoraSaida() == null) {
                veiculoSaida = veiculo;
                break;
            }
        }

        if (veiculoSaida == null) {
            System.out.println("Veículo não encontrado ou já removido");
            return;
        }

        System.out.println("Horário de saída HH:mm (08:00): ");
        String horaSaidaString = scan.nextLine();
        LocalTime horaSaida = LocalTime.parse(horaSaidaString, DateTimeFormatter.ofPattern("HH:mm"));
        veiculoSaida.setHoraSaida(horaSaida);

        long minutosPermanencia = java.time.Duration.between(veiculoSaida.getHoraEntrada(), horaSaida).toMinutes();
        double valorAPagar;

        if (minutosPermanencia <= 60) {
            valorAPagar = 5.0;
        } else if (minutosPermanencia <= 180) {
            valorAPagar = 10.0;
        } else {
            valorAPagar = 15.0;
        }

        for (Vaga vaga : ListaVagas) {
            if (!vaga.isDisponibilidade() && vaga.getTamanho() == veiculoSaida.getTamanho()) {
                vaga.setDisponibilidade(true);
                break;
            }
        }

        System.out.println("Sucesso!");
        System.out.printf("Veículo: %s \n", veiculoSaida.getPlaca());
        System.out.printf("Tempo de permanência em minutos: %d \n", minutosPermanencia);
        System.out.printf("Valor a pagar: R$ %.2f \n", valorAPagar);
    }

    public static void gerarRelatorio() {
        System.out.println("Relatório de vagas");

        boolean vagasOcupadas = false;

        for (Vaga vaga : ListaVagas) {
            if (!vaga.isDisponibilidade()) {
                vagasOcupadas = true;

                for (Veiculo veiculo : ListaVeiculos) {
                    if (veiculo.getHoraSaida() == null) {
                        System.out.println("Número da vaga: " + vaga.getNumero());
                        System.out.println("Tamanho da vaga: " + vaga.getTamanho());
                        System.out.println("Placa do veículo: " + veiculo.getPlaca());
                        break;
                    }
                }
            }
        }

        if (!vagasOcupadas) {
            System.out.println("Nenhuma vaga está ocupada no momento.");
        }
    }

    public static void gerarHistorico() {
        System.out.println("Histórico de Permanência dos Veículos");

        boolean historicoEncontrado = false;

        for (Veiculo veiculo : ListaVeiculos) {
            if (veiculo.getHoraSaida() != null) {
                historicoEncontrado = true;

                long minutosPermanencia = java.time.Duration.between(veiculo.getHoraEntrada(), veiculo.getHoraSaida()).toMinutes();
                double valorPago = calcularValor(minutosPermanencia);

                System.out.println("Placa do veículo: " + veiculo.getPlaca());
                System.out.println("Modelo: " + veiculo.getModelo());
                System.out.println("Tempo de permanência: " + minutosPermanencia + " minutos");
                System.out.println("Valor pago: R$ " + String.format("%.2f", valorPago));
            }
        }

        if (!historicoEncontrado) {
            System.out.println("Histórico não encontrado");
        }
    }

    private static double calcularValor(long minutos) {
        if (minutos <= 60) {
            return 5.0;
        }

        else if (minutos <= 180) {
            return 10.0;
        }

        else {
            return 15.0;
        }
    }
}
