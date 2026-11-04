package br.com.whoisbrizza.santaluciasolidaria.repository;

import br.com.whoisbrizza.santaluciasolidaria.model.SolicitacaoCarona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitacaoCaronaRepository
        extends JpaRepository<SolicitacaoCarona, Long> {

    List<SolicitacaoCarona> findByPassageiroId(Long passageiroId);

    List<SolicitacaoCarona> findByCaronaId(Long caronaId);

    boolean existsByCaronaIdAndPassageiroId(Long caronaId, Long passageiroId);
    
}