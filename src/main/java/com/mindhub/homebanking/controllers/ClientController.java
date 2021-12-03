package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")//le damos una ruta personalizada a nuestro DTO
public class ClientController {//La clase ClientController será el controlador que administre las peticiones que se realicen sobre la entidad Client y devuelve JSON
    @Autowired//inyecta una instancia de un objeto en la propiedad anotada
    private ClientRepository clientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){//Una colección ordenada e indexada. Permite duplicados.
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
                              //findAll().stream().map(    ClientDTO::new             ).collect(toList());
    }
    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientRepository.findById(id).map(client -> new ClientDTO(client)).orElse(null);
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register( @RequestParam String firstName, @RequestParam String lastName,//Anotación que indica que un parámetro de método debe estar vinculado a un parámetro de solicitud web.
                                            @RequestParam String email, @RequestParam String password) {// Compatible con los métodos de controlador anotado en los entornos Servlet y Portlet.

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        Client clientRegistered = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientRepository.save(clientRegistered);
        Account newAccount = new Account("VIN-" + getRandomNumber(10000000, 99999999), LocalDateTime.now(), 0, AccountType.SAVING);
        clientRegistered.addAccount(newAccount);
        accountRepository.save(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
        Client clientAux = clientRepository.findByEmail(authentication.getName());
        ClientDTO clientDTO = new ClientDTO(clientAux);
        return clientDTO;
    }
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
