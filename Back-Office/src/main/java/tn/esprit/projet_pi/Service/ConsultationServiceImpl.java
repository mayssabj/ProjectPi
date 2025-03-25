package tn.esprit.projet_pi.Service;

import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.entity.Consultation;
import tn.esprit.projet_pi.Repository.ConsultationRepository;

import java.util.List;

@Service
public class ConsultationServiceImpl implements IConsultationService {

    private final ConsultationRepository consultationRepo;

    public ConsultationServiceImpl(ConsultationRepository consultationRepo) {
        this.consultationRepo = consultationRepo;
    }

    @Override
    public Consultation ajouterConsultation(Consultation consultation) {
        return consultationRepo.save(consultation);
    }

    @Override
    public Consultation mettreAJourConsultation(Consultation consultation) {
        return consultationRepo.save(consultation);
    }

    @Override
    public Consultation getConsultation(Long id) {
        return consultationRepo.findById(id).orElse(null);
    }

    @Override
    public List<Consultation> getConsultationsParUser(Long userId) {
        return consultationRepo.findByUserId(userId);
    }

    @Override
    public List<Consultation> getConsultationsParMedecin(Long medecinId) {
        return consultationRepo.findByMedecinId(medecinId);
    }

    @Override
    public void supprimerConsultation(Long id) {
        consultationRepo.deleteById(id);
    }
}
