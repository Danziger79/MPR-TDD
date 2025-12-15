package service;

import interfaces.CertificateRepository;
import interfaces.EmailService;
import model.Email;
import model.Employee;
import model.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

// Rola: Test jednostkowy weryfikujący szczegóły interakcji (Argument Capturing).
// Opis: Klasa sprawdza, czy serwis TrainingReminderService poprawnie konstruuje wiadomości e-mail.
// Kluczowym elementem jest użycie ArgumentCaptor, który pozwala "przechwycić" obiekt przekazany
// do metody mocka wewnątrz testowanego serwisu i poddać go szczegółowej inspekcji (np. sprawdzić treść maila).
@ExtendWith(MockitoExtension.class)
class TrainingReminderServiceTest {

    @Mock
    private CertificateRepository certificateRepository;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private TrainingReminderService service;

    // @Captor: Specjalny obiekt Mockito służący do "przechwytywania" argumentów wywołania metody.
    // Zamiast tylko sprawdzać, CZY metoda send() została wywołana, Captor pozwala sprawdzić,
    // CO DOKŁADNIE zostało do niej przekazane (jaki obiekt Email).
    @Captor
    private ArgumentCaptor<Email> emailCaptor;

    @Test
    void shouldSendEmailWithCorrectContent() {
        // ARRANGE
        Employee emp = new Employee("Anna", "anna@corp.com", "HR", Position.MANAGER, 12000);
        // Konfiguracja stuba: repozytorium zwraca jednego pracownika z wygasającym certyfikatem.
        when(certificateRepository.findEmployeesWithExpiringCertificates(30))
                .thenReturn(List.of(emp));

        // ACT
        service.sendReminders();

        // ASSERT - Przechwytywanie argumentu (Argument Capturing).
        // verify sprawdza, czy metoda send() została wywołana.
        // emailCaptor.capture() "łapie" argument w locie i zapisuje go wewnątrz obiektu Captor.
        verify(emailService).send(emailCaptor.capture());

        // Pobranie przechwyconego obiektu w celu inspekcji.
        Email sentEmail = emailCaptor.getValue();

        // Szczegółowa weryfikacja zawartości obiektu, który został utworzony wewnątrz metody sendReminders().
        assertEquals("anna@corp.com", sentEmail.recipient());
        assertEquals("Przypomnienie o szkoleniu", sentEmail.subject());
        assertTrue(sentEmail.body().contains("Anna"), "Treść maila powinna być spersonalizowana (zawierać imię).");
    }

    @Test
    void shouldNotSendEmailsWhenNoCertificatesExpiring() {
        // ARRANGE
        // Scenariusz: Brak pracowników z wygasającymi certyfikatami.
        when(certificateRepository.findEmployeesWithExpiringCertificates(30))
                .thenReturn(Collections.emptyList());

        // ACT
        service.sendReminders();

        // ASSERT - Weryfikacja braku interakcji (Zero Interactions).
        // verify(mock, never()) upewnia się, że metoda send() NIE została wywołana ani razu.
        // Jest to kluczowe, aby uniknąć wysyłania pustych maili lub spamu.
        verify(emailService, never()).send(any());
    }
}

// trzeba weryfikowac to co w when jest