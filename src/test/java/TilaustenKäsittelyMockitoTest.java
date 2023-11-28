import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.testng.AssertJUnit.assertEquals;

public class TilaustenKäsittelyMockitoTest {

    @Mock
    IHinnoittelija hinnoittelijaMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testaaKäsittelijäWithMockitoHinnoittelijaAlle100() {
        // Arrange
        float alkuSaldo = 100.0f;
        float listaHinta = 30.0f;
        float alennus = 20.0f;
        float loppuSaldo = alkuSaldo - (listaHinta * (1 - alennus / 100));
        Asiakas asiakas = new Asiakas(alkuSaldo);
        Tuote tuote = new Tuote("TDD in Action", listaHinta);

        // Record
        when(hinnoittelijaMock.getAlennusProsentti(asiakas, tuote)).thenReturn(alennus);

        // Act
        TilaustenKäsittely käsittelijä = new TilaustenKäsittely();
        käsittelijä.setHinnoittelija(hinnoittelijaMock);
        käsittelijä.käsittele(new Tilaus(asiakas, tuote));


        // Assert
        assertEquals(loppuSaldo, asiakas.getSaldo(), 0.001);
        verify(hinnoittelijaMock, times(2)).getAlennusProsentti(asiakas, tuote);
    }

    @Test
    public void testaaKäsittelijäWithMockitoHinnoittelijaHinta100TaiYli() {
        // Arrange
        float alkuSaldo = 100.0f;
        float listaHinta = 110.0f;
        float alennus = 20.0f;
        float loppuSaldo = alkuSaldo - (listaHinta * (1 - (alennus + 5.0f) / 100));
        Asiakas asiakas = new Asiakas(alkuSaldo);
        Tuote tuote = new Tuote("TDD in Action2", listaHinta);

        // Record behavior of the mock
        when(hinnoittelijaMock.getAlennusProsentti(asiakas, tuote)).thenReturn(alennus + 5.0f);

        // Act
        TilaustenKäsittely käsittelijä = new TilaustenKäsittely();
        käsittelijä.setHinnoittelija(hinnoittelijaMock);
        käsittelijä.käsittele(new Tilaus(asiakas, tuote));


        // Assert
        assertEquals(loppuSaldo, asiakas.getSaldo(), 0.001);
        verify(hinnoittelijaMock, times(2)).getAlennusProsentti(asiakas, tuote);
    }
}