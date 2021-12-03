package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class CardController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> createCard (Authentication authentication, @RequestParam CardType type, @RequestParam CardColor color){
        Client clientCurrent = clientRepository.findByEmail(authentication.getName());
        if (clientCurrent.getCards().size() > 6){
            return new ResponseEntity<>("You have more than six cards", HttpStatus.FORBIDDEN);
        } else {
            Card cardCreated = new Card(clientCurrent.getLastName() + " " + clientCurrent.getFirstName(),type, color,getRandomNumber(1000, 9999) + "-" + getRandomNumber(1000, 9999) + "-" + getRandomNumber(1000, 9999) + "-" + getRandomNumber(1000, 9999), getRandomNumber(100, 999), LocalDate.now(), LocalDate.now().plusYears(5), clientCurrent);
            cardRepository.save(cardCreated);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @DeleteMapping("/api/clients/current/cards")
    public ResponseEntity<Object> deleteCard (Authentication authentication, @RequestParam Long id){
        Client clientCurrent = clientRepository.findByEmail(authentication.getName());
        Card cardDeleted = cardRepository.findById(id).orElse(null);
        if (cardDeleted == null){
            return new ResponseEntity<>("The card does not exist", HttpStatus.FORBIDDEN);
        }
        if (!clientCurrent.getCards().contains(cardDeleted)){
            return new ResponseEntity<>("The card does not belong to the client", HttpStatus.FORBIDDEN);
        }
        cardRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
