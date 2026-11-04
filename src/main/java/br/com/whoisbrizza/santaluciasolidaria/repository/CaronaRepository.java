package br.com.whoisbrizza.santaluciasolidaria.repository;

import br.com.whoisbrizza.santaluciasolidaria.model.Carona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CaronaRepository extends JpaRepository<Carona, Long> {

    List<Carona> findByDestinoContainingIgnoreCase(String destino);

    List<Carona> findByData(LocalDate data);
}
