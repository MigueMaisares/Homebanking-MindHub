package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//Una entidad es un objeto de persistencia que representa una tabla en una base de datos, y cada instancia de entidad corresponde a una fila en la tabla.
@Entity//La anotación @Entity del modulo Spring JPA le dice a Spring que cree una tabla de clientes para esta clase.
public class Client {

    @Id//La anotación @Id dice que la variable de instancia id contiene la clave primaria PK de la base de datos para esta clase entidad
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")//estas anotaciones le dicen a JPA que use cualquier generador de ID proporcionado por el sistema de base de datos
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)//el campo client en Account es el relacional colector
    Set<Account> accounts = new HashSet<>();//SET no permite objetos duplicados.
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    Set<ClientLoan> clientLoans = new HashSet<>();//Las clases de implementación incluyen: HashSet (no ordenada), LinkedHashSet (ordenada), TreeSet (ordenada por orden natural o por comparador)
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    Set<Card> cards = new HashSet<>();

    public Client() { }//Esto es lo que llamará JPA para crear nuevas instancias para cualquier clase de entidad.

    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }
    public void addAccount (Account account){
        account.setClient(this);
        accounts.add(account);
    }//add en mi coleccion y adjudico la cuenta a mi mismo

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }
    public void addClientLoan (ClientLoan clientLoan) {
        clientLoans.add(clientLoan);
        clientLoan.setClient(this);
    }
    public List<Loan> getLoans() {
        return clientLoans.stream().map(client -> client.getLoan()).collect(Collectors.toList());
    }

    public Set<Card> getCards() {
        return cards;
    }
    public void addCard(Card card) {
        cards.add(card);
        card.setClient(this);
    }
}
