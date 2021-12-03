package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired//Spring inyecta el objeto PasswordEncoder que se crea con el @Bean en la clase WebAuthentication
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean//crea un contenedor en spring acceder y manipularlo
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {//CommandLineRunner es una interfaz que nos permite usar ClientRepository como repositorio y con métodos
			Client cliente1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("123"));
			Client cliente2 = new Client("Marcos", "Sotello", "marcos@mindhub.com", passwordEncoder.encode("456"));
			Client cliente3 = new Client("admin", "admin", "admin", passwordEncoder.encode("admin"));
			//uso el repository para manipular la base de datos con sus métodos
			clientRepository.save(cliente1);
			clientRepository.save(cliente2);
			clientRepository.save(cliente3);

			LocalDateTime hoy = LocalDateTime.now();
			Account cuenta1 = new Account("VIN001", hoy, 40000, AccountType.SAVING);
			Account cuenta2 = new Account("VIN002", hoy.plusDays(1), 75500, AccountType.CURRENT);
			Account cuenta3 = new Account("VIN003", hoy, 15000, AccountType.SAVING);
			Account cuenta4 = new Account("VIN004", hoy, 72200, AccountType.CURRENT);

			cliente1.addAccount(cuenta1);	cliente1.addAccount(cuenta2);
			cliente2.addAccount(cuenta3);	cliente2.addAccount(cuenta4);

			accountRepository.save(cuenta1);
			accountRepository.save(cuenta2);
			accountRepository.save(cuenta3);
			accountRepository.save(cuenta4);

			Transaction transaction1 = new Transaction(-1000.00, "descripcion 1", hoy.minusDays(1), TransactionType.DEBIT,49000.00);
			Transaction transaction2 = new Transaction(-5000.00, "descripcion 2", hoy, TransactionType.DEBIT,44000.00);
			Transaction transaction3 = new Transaction(1000.00, "descripcion 3", hoy, TransactionType.CREDIT, 76000.00);
			Transaction transaction4 = new Transaction(-500.00, "descripcion 4", hoy, TransactionType.DEBIT, 75500.00);
			Transaction transaction5 = new Transaction(5000.00, "descripcion 5", hoy, TransactionType.CREDIT, 25000.00);
			Transaction transaction6 = new Transaction(-10000.00, "descripcion 6", hoy, TransactionType.DEBIT, 15000.00);
			Transaction transaction7 = new Transaction(7000.00, "descripcion 7", hoy, TransactionType.CREDIT, 73000.00);
			Transaction transaction8 = new Transaction(-800.00, "descripcion 8", hoy, TransactionType.DEBIT, 72200.00);

			Transaction transaction9 = new Transaction(-4000.00, "descripcion 9", hoy.plusDays(1), TransactionType.DEBIT, 40000.00);

			cuenta1.addTransaction(transaction1);	cuenta1.addTransaction(transaction2);
			cuenta2.addTransaction(transaction3);	cuenta2.addTransaction(transaction4);
			cuenta3.addTransaction(transaction5);	cuenta3.addTransaction(transaction6);
			cuenta4.addTransaction(transaction7);	cuenta4.addTransaction(transaction8);

			cuenta1.addTransaction(transaction9);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);
			transactionRepository.save(transaction9);

			List<Integer> cuotasHipotecario = List.of(12, 24, 36, 48, 60);
			List<Double> interesesHipotecario = List.of(0.2, 0.25, 0.3, 0.35);
			Loan hipotecario = new Loan("Hypothecary", 500000, cuotasHipotecario, interesesHipotecario);
			List<Integer> cuotasPersonal = List.of(6, 12, 24);
			List<Double> interesesPersonal = List.of(0.15, 0.2, 0.25);
			Loan personal = new Loan("Personal", 100000, cuotasPersonal, interesesPersonal);
			List<Integer> cuotasAutomotriz = List.of(6, 12, 24, 36);
			List<Double> interesesAutomotriz = List.of(0.2, 0.25, 0.3);
			Loan automotriz = new Loan("Automotive", 300000, cuotasAutomotriz, interesesAutomotriz);

			loanRepository.save(hipotecario);
			loanRepository.save(personal);
			loanRepository.save(automotriz);

			ClientLoan clientLoan1 = new ClientLoan(400000, 60, cliente1, hipotecario,0.2);
			ClientLoan clientLoan2 = new ClientLoan(50000, 12, cliente1, personal, 0.15);
			ClientLoan clientLoan3 = new ClientLoan(100000, 24, cliente2, personal, 0.25);
			ClientLoan clientLoan4 = new ClientLoan(200000, 36, cliente2, automotriz, 0.3);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			Card card1 = new Card(cliente1.getLastName()+" "+cliente1.getFirstName(),CardType.DEBIT, CardColor.GOLD, "1234 1234 1234 1230", 123, LocalDate.now().minusYears(6), LocalDate.now().minusYears(1),cliente1);
			Card card2 = new Card(cliente1.getLastName()+" "+cliente1.getFirstName(),CardType.CREDIT, CardColor.TITANIUM, "1234 1234 1234 1231", 456, LocalDate.now(), LocalDate.now().plusYears(5),cliente1);
			Card card3 = new Card(cliente2.getLastName()+" "+cliente2.getFirstName(),CardType.CREDIT, CardColor.SILVER, "1234 1234 1234 1232", 789, LocalDate.now(), LocalDate.now().plusYears(5),cliente2);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
		};
	}
}
