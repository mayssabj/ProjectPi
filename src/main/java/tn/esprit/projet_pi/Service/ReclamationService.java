package tn.esprit.projet_pi.Service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.Repository.UserRepo;
import tn.esprit.projet_pi.entity.Reclamation;
import tn.esprit.projet_pi.Repository.ReclamationRepository;
import tn.esprit.projet_pi.entity.User;

import java.util.List;

@Service
public class ReclamationService {
    @Autowired
    private ReclamationRepository reclamationRepository;
    @Autowired
    private EmailService emailService;



    public Reclamation createReclamation(Reclamation reclamation) {
        Reclamation savedReclamation = reclamationRepository.save(reclamation);

        emailService.sendReclamationResponse(savedReclamation);

        return savedReclamation;
    }

    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    /*public Reclamation getReclamationById(Long id) {
        Reclamation reclamation = reclamationRepository.findById(id);
        return reclamation;
    }*/

    /*public Reclamation updateReclamation(Long id, Reclamation updatedReclamation) {
        Reclamation reclamation = getReclamationById(id);
        reclamation.setSubject(updatedReclamation.getSubject());
        reclamation.setDescription(updatedReclamation.getDescription());
        reclamation.setStatus(updatedReclamation.getStatus());
        return reclamationRepository.save(reclamation);
    }*/

    public List<Reclamation> getReclamationsByUserId(Long userId) {
        return reclamationRepository.findByUser_IdUser(userId);
    }

    @Transactional
    public void deleteReclamation(Long id) {
        reclamationRepository.deleteById(id);
    }

}
