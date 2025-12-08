package service;

import interfaces.CertificateRepository;
import interfaces.EmailService;
import model.Email;
import model.Employee;

public class TrainingReminderService {
    private final CertificateRepository certificateRepository;
    private final EmailService emailService;

    public TrainingReminderService(CertificateRepository certificateRepository, EmailService emailService) {
        this.certificateRepository = certificateRepository;
        this.emailService = emailService;
    }

    public void sendReminders() {
        var employees = certificateRepository.findEmployeesWithExpiringCertificates(30);

        for (Employee emp : employees) {
            String topic = "Przypomnienie o szkoleniu";
            String content = "Witaj " + emp.getName() + ", Twoje certyfikaty wygasają niedługo.";

            // Wysyłamy obiekt Email (który przechwycimy Captorem)
            emailService.send(new Email(emp.getEmail(), topic, content));
        }
    }
}