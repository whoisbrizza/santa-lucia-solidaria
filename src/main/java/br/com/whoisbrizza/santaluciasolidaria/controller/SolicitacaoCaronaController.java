package br.com.whoisbrizza.santaluciasolidaria.controller;

import br.com.whoisbrizza.santaluciasolidaria.model.SolicitacaoCarona;
import br.com.whoisbrizza.santaluciasolidaria.service.SolicitacaoCaronaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoCaronaController {

    private final SolicitacaoCaronaService solicitacaoService;

    public SolicitacaoCaronaController(
            SolicitacaoCaronaService solicitacaoService) {
        this.solicitacaoService = solicitacaoService;
    }

    @PostMapping("/carona/{caronaId}/passageiro/{passageiroId}")
    public SolicitacaoCarona solicitar(
            @PathVariable Long caronaId,
            @PathVariable Long passageiroId) {
        return solicitacaoService.solicitar(caronaId, passageiroId);
    }

    @GetMapping("/passageiro/{passageiroId}")
    public List<SolicitacaoCarona> listarPorPassageiro(
            @PathVariable Long passageiroId) {
        return solicitacaoService.listarPorPassageiro(passageiroId);
    }

    @GetMapping("/carona/{caronaId}")
    public List<SolicitacaoCarona> listarPorCarona(
            @PathVariable Long caronaId) {
        return solicitacaoService.listarPorCarona(caronaId);
    }

    @PatchMapping("/{solicitacaoId}/status")
    public SolicitacaoCarona atualizarStatus(
            @PathVariable Long solicitacaoId,
            @RequestParam Long motoristaId,
            @RequestParam SolicitacaoCarona.Status status) {
        return solicitacaoService.atualizarStatus(
                solicitacaoId, motoristaId, status);
    }
}