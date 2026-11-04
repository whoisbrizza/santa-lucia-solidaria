package br.com.whoisbrizza.santaluciasolidaria.service;

import br.com.whoisbrizza.santaluciasolidaria.model.Carona;
import br.com.whoisbrizza.santaluciasolidaria.model.Usuario;
import br.com.whoisbrizza.santaluciasolidaria.model.SolicitacaoCarona;
import br.com.whoisbrizza.santaluciasolidaria.repository.CaronaRepository;
import br.com.whoisbrizza.santaluciasolidaria.repository.UsuarioRepository;
import br.com.whoisbrizza.santaluciasolidaria.repository.SolicitacaoCaronaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitacaoCaronaService {

    private final SolicitacaoCaronaRepository solicitacaoRepository;
    private final CaronaRepository caronaRepository;
    private final UsuarioRepository usuarioRepository;

    public SolicitacaoCaronaService(
            SolicitacaoCaronaRepository solicitacaoRepository,
            CaronaRepository caronaRepository,
            UsuarioRepository usuarioRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.caronaRepository = caronaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public SolicitacaoCarona solicitar(Long caronaId, Long passageiroId) {

        Carona carona = caronaRepository.findById(caronaId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Carona não encontrada."));

        Usuario passageiro = usuarioRepository.findById(passageiroId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Passageiro não encontrado."));

        if (carona.getMotorista().getId().equals(passageiroId)) {
            throw new IllegalArgumentException(
                    "O motorista não pode solicitar a própria carona.");
        }

        if (carona.getVagasDisponiveis() <= 0) {
            throw new IllegalArgumentException(
                    "Não há vagas disponíveis.");
        }

        if (solicitacaoRepository
                .existsByCaronaIdAndPassageiroId(caronaId, passageiroId)) {
            throw new IllegalArgumentException(
                    "Você já solicitou esta carona.");
        }

        SolicitacaoCarona solicitacao = new SolicitacaoCarona();
        solicitacao.setCarona(carona);
        solicitacao.setPassageiro(passageiro);

        return solicitacaoRepository.save(solicitacao);
    }

    public List<SolicitacaoCarona> listarPorPassageiro(Long passageiroId) {
        return solicitacaoRepository.findByPassageiroId(passageiroId);
    }

    public List<SolicitacaoCarona> listarPorCarona(Long caronaId) {
        return solicitacaoRepository.findByCaronaId(caronaId);
    }

    public SolicitacaoCarona atualizarStatus(
            Long solicitacaoId,
            Long motoristaId,
            SolicitacaoCarona.Status status) {

        SolicitacaoCarona solicitacao = solicitacaoRepository
                .findById(solicitacaoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Solicitação não encontrada."));

        if (!solicitacao.getCarona().getMotorista().getId()
                .equals(motoristaId)) {
            throw new IllegalArgumentException(
                    "Somente o motorista pode analisar esta solicitação.");
        }

        if (solicitacao.getStatus() != SolicitacaoCarona.Status.PENDENTE) {
            throw new IllegalArgumentException(
                    "Esta solicitação já foi analisada.");
        }

        if (status == SolicitacaoCarona.Status.ACEITA) {
            Carona carona = solicitacao.getCarona();

            if (carona.getVagasDisponiveis() <= 0) {
                throw new IllegalArgumentException(
                        "Não há vagas disponíveis.");
            }

            carona.setVagasDisponiveis(
                    carona.getVagasDisponiveis() - 1);

            caronaRepository.save(carona);
        }

        solicitacao.setStatus(status);

        return solicitacaoRepository.save(solicitacao);
    }
}