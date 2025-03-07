package tn.esprit.projet_pi.Service;

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



    public Reclamation createReclamation(Reclamation reclamation) {
        return reclamationRepository.save(reclamation);
    }

    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    public Reclamation getReclamationById(Long id) {
        Reclamation reclamation = reclamationRepository.findById(id);
        return reclamation;
    }

    public Reclamation updateReclamation(Long id, Reclamation updatedReclamation) {
        Reclamation reclamation = getReclamationById(id);
        reclamation.setSubject(updatedReclamation.getSubject());
        reclamation.setDescription(updatedReclamation.getDescription());
        reclamation.setStatus(updatedReclamation.getStatus());
        return reclamationRepository.save(reclamation);
    }

    public void deleteReclamation(Long id) {
        reclamationRepository.deleteById(id);
    }

}
