
package br.com.whoisbrizza.santaluciasolidaria.service;

import br.com.whoisbrizza.santaluciasolidaria.model.Carona;
import br.com.whoisbrizza.santaluciasolidaria.model.Usuario;
import br.com.whoisbrizza.santaluciasolidaria.model.SolicitacaoCarona;
import br.com.whoisbrizza.santaluciasolidaria.repository.CaronaRepository;
import br.com.whoisbrizza.santaluciasolidaria.repository.UsuarioRepository;
import br.com.whoisbrizza.santaluciasolidaria.repository.SolicitacaoCaronaRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitacaoCaronaServiceTest {

    @Mock
    private SolicitacaoCaronaRepository solicitacaoRepository;

    @Mock
    private CaronaRepository caronaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private SolicitacaoCaronaService service;

    private Carona criarCarona() {
        Usuario motorista = new Usuario();
        motorista.setId(2L);

        Carona carona = new Carona();
        carona.setId(1L);
        carona.setMotorista(motorista);
        carona.setVagasDisponiveis(2);

        return carona;
    }

    private SolicitacaoCarona criarSolicitacao() {
        Usuario passageiro = new Usuario();
        passageiro.setId(3L);

        SolicitacaoCarona solicitacao = new SolicitacaoCarona();
        solicitacao.setId(1L);
        solicitacao.setCarona(criarCarona());
        solicitacao.setPassageiro(passageiro);
        solicitacao.setStatus(SolicitacaoCarona.Status.PENDENTE);

        return solicitacao;
    }

    @Test
    void deveImpedirMotoristaDeSolicitarPropriaCarona() {
        Carona carona = criarCarona();

        when(caronaRepository.findById(1L))
                .thenReturn(Optional.of(carona));

        assertThrows(IllegalArgumentException.class,
                () -> service.solicitar(1L, 2L));

        verify(solicitacaoRepository, never()).save(any());
    }

    @Test
    void deveImpedirSolicitacaoSemVagas() {
        Carona carona = criarCarona();
        carona.setVagasDisponiveis(0);

        Usuario passageiro = new Usuario();
        passageiro.setId(3L);

        when(caronaRepository.findById(1L))
                .thenReturn(Optional.of(carona));
        when(usuarioRepository.findById(3L))
                .thenReturn(Optional.of(passageiro));

        assertThrows(IllegalArgumentException.class,
                () -> service.solicitar(1L, 3L));

        verify(solicitacaoRepository, never()).save(any());
    }

    @Test
    void deveImpedirSolicitacaoDuplicada() {
        Carona carona = criarCarona();

        Usuario passageiro = new Usuario();
        passageiro.setId(3L);

        when(caronaRepository.findById(1L))
                .thenReturn(Optional.of(carona));
        when(usuarioRepository.findById(3L))
                .thenReturn(Optional.of(passageiro));
        when(solicitacaoRepository
                .existsByCaronaIdAndPassageiroId(1L, 3L))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.solicitar(1L, 3L));

        verify(solicitacaoRepository, never()).save(any());
    }

    @Test
    void deveAceitarSolicitacaoEDiminuirVaga() {
        SolicitacaoCarona solicitacao = criarSolicitacao();

        when(solicitacaoRepository.findById(1L))
                .thenReturn(Optional.of(solicitacao));
        when(caronaRepository.save(any(Carona.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(solicitacaoRepository.save(any(SolicitacaoCarona.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SolicitacaoCarona resultado = service.atualizarStatus(
                1L, 2L, SolicitacaoCarona.Status.ACEITA);

        assertEquals(SolicitacaoCarona.Status.ACEITA,
                resultado.getStatus());
        assertEquals(1, resultado.getCarona().getVagasDisponiveis());

        verify(caronaRepository).save(solicitacao.getCarona());
    }

    @Test
    void deveRecusarSolicitacaoSemDiminuirVaga() {
        SolicitacaoCarona solicitacao = criarSolicitacao();

        when(solicitacaoRepository.findById(1L))
                .thenReturn(Optional.of(solicitacao));
        when(solicitacaoRepository.save(any(SolicitacaoCarona.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SolicitacaoCarona resultado = service.atualizarStatus(
                1L, 2L, SolicitacaoCarona.Status.RECUSADA);

        assertEquals(SolicitacaoCarona.Status.RECUSADA,
                resultado.getStatus());
        assertEquals(2, resultado.getCarona().getVagasDisponiveis());

        verify(caronaRepository, never()).save(any());
    }

    @Test
    void deveImpedirOutroMotoristaDeAnalisarSolicitacao() {
        SolicitacaoCarona solicitacao = criarSolicitacao();

        when(solicitacaoRepository.findById(1L))
                .thenReturn(Optional.of(solicitacao));

        assertThrows(IllegalArgumentException.class,
                () -> service.atualizarStatus(
                        1L, 99L, SolicitacaoCarona.Status.ACEITA));

        verify(solicitacaoRepository, never()).save(any());
    }
}
