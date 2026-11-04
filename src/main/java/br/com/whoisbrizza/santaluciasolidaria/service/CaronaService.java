package br.com.whoisbrizza.santaluciasolidaria.service;

import br.com.whoisbrizza.santaluciasolidaria.model.Carona;
import br.com.whoisbrizza.santaluciasolidaria.model.Usuario;
import br.com.whoisbrizza.santaluciasolidaria.repository.CaronaRepository;
import br.com.whoisbrizza.santaluciasolidaria.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CaronaService {

    private final CaronaRepository caronaRepository;
    private final UsuarioRepository usuarioRepository;

    public CaronaService(
            CaronaRepository caronaRepository,
            UsuarioRepository usuarioRepository) {
        this.caronaRepository = caronaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Carona cadastrar(Carona carona, Long motoristaId) {

        if (carona.getVagasDisponiveis() == null ||
                carona.getVagasDisponiveis() <= 0) {
            throw new IllegalArgumentException(
                    "A carona precisa ter pelo menos uma vaga."
            );
        }

        if (carona.getData() == null ||
                carona.getData().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "A data da carona deve ser hoje ou futura."
            );
        }

        Usuario motorista = usuarioRepository.findById(motoristaId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Motorista não encontrado."
                ));

        carona.setMotorista(motorista);

        return caronaRepository.save(carona);
    }

    public List<Carona> listarTodas() {
        return caronaRepository.findAll();
    }

    public List<Carona> buscarPorDestino(String destino) {
        return caronaRepository
                .findByDestinoContainingIgnoreCase(destino);
    }

    public List<Carona> buscarPorData(LocalDate data) {
        return caronaRepository.findByData(data);
    }

    public void excluir(Long id) {
        Carona carona = caronaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Carona não encontrada."
                ));

        caronaRepository.delete(carona);
    }
}
