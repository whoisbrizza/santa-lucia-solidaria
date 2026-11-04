
package br.com.whoisbrizza.santaluciasolidaria.service;

import br.com.whoisbrizza.santaluciasolidaria.model.Carona;
import br.com.whoisbrizza.santaluciasolidaria.model.Usuario;
import br.com.whoisbrizza.santaluciasolidaria.repository.CaronaRepository;
import br.com.whoisbrizza.santaluciasolidaria.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaronaServiceTest {

    @Mock
    private CaronaRepository caronaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CaronaService caronaService;

    @Test
    void deveImpedirCadastroSemVagas() {
        Carona carona = new Carona();
        carona.setVagasDisponiveis(0);

        assertThrows(IllegalArgumentException.class,
                () -> caronaService.cadastrar(carona, 1L));

        verify(caronaRepository, never()).save(any());
    }

    @Test
    void deveImpedirCadastroComDataPassada() {
        Carona carona = new Carona();
        carona.setVagasDisponiveis(2);
        carona.setData(LocalDate.now().minusDays(1));

        assertThrows(IllegalArgumentException.class,
                () -> caronaService.cadastrar(carona, 1L));

        verify(caronaRepository, never()).save(any());
    }

    @Test
    void deveImpedirCadastroComMotoristaInexistente() {
        Carona carona = new Carona();
        carona.setVagasDisponiveis(2);
        carona.setData(LocalDate.now());

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> caronaService.cadastrar(carona, 1L));

        verify(caronaRepository, never()).save(any());
    }

    @Test
    void deveCadastrarCaronaValida() {
        Carona carona = new Carona();
        carona.setVagasDisponiveis(2);
        carona.setData(LocalDate.now());

        Usuario motorista = new Usuario();
        motorista.setId(1L);

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(motorista));

        when(caronaRepository.save(carona))
                .thenReturn(carona);

        Carona resultado = caronaService.cadastrar(carona, 1L);

        assertNotNull(resultado);
        assertEquals(motorista, resultado.getMotorista());

        verify(caronaRepository).save(carona);
    }
}
