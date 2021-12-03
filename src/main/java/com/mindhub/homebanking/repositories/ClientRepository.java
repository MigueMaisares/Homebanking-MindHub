package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//le indica a Spring que debe genera el código necesario para que se pueda administrar la data de la aplicación desde el navegador usando JSON,
// es decir se crea una API REST automática (servicio REST) que expone los recursos de cada repositorio anotado con @RepositoryRestResource
@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long>{//manipula la info de Client y el tipo de la PK   Long
    //Una clase Repository es análoga a una tabla, es una clase que administra una colección de instancias.
    //JpaRepository provee a => ClientRepository de metodos para la manipulacion de la base de datos

    Client findByEmail(String email);
}
