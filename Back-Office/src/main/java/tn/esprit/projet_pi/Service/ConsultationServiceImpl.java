package tn.esprit.projet_pi.Service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.Repository.UserRepo;
import tn.esprit.projet_pi.entity.Consultation;
import tn.esprit.projet_pi.Repository.ConsultationRepository;
import tn.esprit.projet_pi.Repository.UserRepo;
import tn.esprit.projet_pi.entity.User;
import tn.esprit.projet_pi.entity.Role;



import java.util.List;

@Service
public class ConsultationServiceImpl implements IConsultationService {

    private final ConsultationRepository consultationRepository;
    private final UserRepo userRepo;


    public ConsultationServiceImpl(ConsultationRepository consultationRepository, UserRepo userRepo) {
        this.consultationRepository = consultationRepository;
        this.userRepo = userRepo;
    }


    @Override
    public Consultation addConsultation(Consultation consultation) {
        // Récupère l'email de l'utilisateur connecté
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Cherche l'étudiant connecté
        User etudiant = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        // Cherche le médecin (tu peux adapter selon ta logique si tu en as plusieurs)
        User medecin = userRepo.findByRole(Role.Medcin)
                .orElseThrow(() -> new RuntimeException("Aucun médecin trouvé"));


        // Affecte les utilisateurs
        consultation.setEtudiant(etudiant);
        consultation.setMedecin(medecin);

        return consultationRepository.save(consultation);
    }


    @Override
    public Consultation updateConsultation(Long id, Consultation c) {
        c.setId(id);
        return consultationRepository.save(c);
    }

    @Override
    public void deleteConsultation(Long id) {
        consultationRepository.deleteById(id);
    }

    @Override
    public Consultation getConsultation(Long id) {
        return consultationRepository.findById(id).orElse(null);
    }

    @Override
    public List<Consultation> getByEtudiant(Long userId) {
        return consultationRepository.findByEtudiant_IdUser(userId);
    }

    @Override
    public List<Consultation> getByMedecin(Long medecinId) {
        return consultationRepository.findByMedecin_IdUser(medecinId);
    }

}
