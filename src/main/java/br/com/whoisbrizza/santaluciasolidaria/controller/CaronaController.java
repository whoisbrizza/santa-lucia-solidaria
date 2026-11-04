package br.com.whoisbrizza.santaluciasolidaria.controller;

import br.com.whoisbrizza.santaluciasolidaria.model.Carona;
import br.com.whoisbrizza.santaluciasolidaria.service.CaronaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/caronas")
public class CaronaController {

    private final CaronaService caronaService;

    public CaronaController(CaronaService caronaService) {
        this.caronaService = caronaService;
    }

    @PostMapping("/{motoristaId}")
    public Carona cadastrar(
            @PathVariable Long motoristaId,
            @RequestBody Carona carona) {
        return caronaService.cadastrar(carona, motoristaId);
    }

    @GetMapping
    public List<Carona> listarTodas() {
        return caronaService.listarTodas();
    }

    @GetMapping("/destino")
    public List<Carona> buscarPorDestino(
            @RequestParam String destino) {
        return caronaService.buscarPorDestino(destino);
    }

    @GetMapping("/data")
    public List<Carona> buscarPorData(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate data) {
        return caronaService.buscarPorData(data);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        caronaService.excluir(id);
    }
}
