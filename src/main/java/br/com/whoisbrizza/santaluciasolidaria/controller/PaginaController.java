package br.com.whoisbrizza.santaluciasolidaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @GetMapping("/oferecer-carona")
    public String oferecerCarona() {
        return "oferecer-carona";
    }

    @GetMapping("/consultar-caronas")
    public String consultarCaronas() {
        return "consultar-caronas";
    }

    @GetMapping("/gerenciar-solicitacoes")
public String gerenciarSolicitacoes() {
    return "gerenciar-solicitacoes";
}

}