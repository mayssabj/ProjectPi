package tn.esprit.projet_pi.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.projet_pi.Repository.MenuRepository;
import tn.esprit.projet_pi.Repository.PlatRepository;
import tn.esprit.projet_pi.Repository.RegimeAlimentaireRepository;
import tn.esprit.projet_pi.Repository.UserRepo;
import tn.esprit.projet_pi.entity.*;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {
    private static final Logger LOGGER = Logger.getLogger(MenuServiceImpl.class.getName());

    @Autowired
    private PlatRepository platRepository;

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private RegimeAlimentaireRepository regimeAlimentaireRepository;

    // Génère les menus pour chaque jour de la semaine pour toutes les catégories d'étudiants
    @Override
    @Transactional
    public void generateWeeklyMenus(Long userId) {
        try {
            Optional<User> userOptional = userRepository.findByidUser(userId);
            if (!userOptional.isPresent()) {
                throw new RuntimeException("Utilisateur non trouvé");
            }

            User user = userOptional.get(); // ✅ Get the user who creates the menu

            List<RegimeAlimentaire> regimesExistants = (List<RegimeAlimentaire>) regimeAlimentaireRepository.findAll();
            Map<RegimeAlimentaireType, RegimeAlimentaire> regimeMap = new HashMap<>();

            for (RegimeAlimentaire regime : regimesExistants) {
                regimeMap.put(regime.getType(), regime);
            }

            // Pour chaque jour de la semaine
            for (int i = 0; i < 7; i++) {
                LocalDate date = LocalDate.now().plusDays(i);

                // Pour chaque type de régime
                for (RegimeAlimentaireType regimeType : RegimeAlimentaireType.values()) {
                    RegimeAlimentaire regimeAlimentaire = regimeMap.get(regimeType);

                    if (regimeAlimentaire == null) {
                        LOGGER.warning("⚠️ Aucun régime trouvé en base pour " + regimeType);
                        continue;
                    }

                    LOGGER.info("🟢 Génération du menu pour " + regimeType + " à la date " + date);

                    // Vérifier si un menu existe déjà
                    if (menuRepository.existsByDateAndRegime(date, regimeType)) {
                        LOGGER.info("ℹ️ Menu déjà existant pour " + date + " - " + regimeType);
                        continue;
                    }

                    List<Plat> platsDuJour = generateCompleteMenuForRegime(regimeAlimentaire, date);

                    if (!platsDuJour.isEmpty()) {
                        Menu menu = new Menu();
                        menu.setDate(date);
                        menu.setRegime(regimeType);
                        menu.setPlats(platsDuJour);
                        menu.setCreatedBy(user); // ✅ Set the user who created the menu

                        menuRepository.save(menu); // Save the menu to the database
                        LOGGER.info("✅ Menu enregistré pour " + date + " - Régime: " + regimeType);
                    } else {
                        LOGGER.warning("⚠️ Aucun plat disponible pour " + regimeType + " à la date " + date);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.severe("❌ Erreur globale lors de la génération des menus : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la génération des menus", e);
        }
    }


    // Génère un menu complet (entrée, plat principal, dessert) pour un régime donné et un jour spécifique
    private List<Plat> generateCompleteMenuForRegime(RegimeAlimentaire regimeAlimentaire, LocalDate date) {
        List<Plat> plats = new ArrayList<>();
        Set<Plat> usedPlats = new HashSet<>(); // Pour suivre les plats déjà utilisés

        LOGGER.info("🔍 Génération du menu pour le régime : " + regimeAlimentaire.getType());

        // Récupérer les plats selon les catégories
        Plat entree = getPlatByCategorie(regimeAlimentaire, CategoriePlat.ENTREE, usedPlats);
        Plat platPrincipal = getPlatByCategorie(regimeAlimentaire, CategoriePlat.PLAT_PRINCIPAL, usedPlats);
        Plat dessert = getPlatByCategorie(regimeAlimentaire, CategoriePlat.DESSERT, usedPlats);

        // Ajouter les plats à la liste si présents
        if (entree != null) plats.add(entree);
        if (platPrincipal != null) plats.add(platPrincipal);
        if (dessert != null) plats.add(dessert);

        if (plats.isEmpty()) {
            LOGGER.warning("⚠️ Aucun plat disponible pour le régime " + regimeAlimentaire.getType());
        }

        return plats;
    }

    // Recherche un plat pour une catégorie spécifique, en évitant les répétitions
    private Plat getPlatByCategorie(RegimeAlimentaire regimeAlimentaire, CategoriePlat categorie, Set<Plat> usedPlats) {
        List<Plat> plats = regimeAlimentaire.getPlatsRecommandes()
                .stream()
                .filter(plat -> plat.getCategorie() == categorie)
                .filter(plat -> !usedPlats.contains(plat)) // Exclure les plats déjà utilisés
                .collect(Collectors.toList());

        if (plats.isEmpty()) {
            LOGGER.warning("⚠️ Aucun plat disponible pour la catégorie " + categorie + " dans le régime " + regimeAlimentaire.getType());
            return null;
        }

        // Choisir un plat aléatoire parmi ceux disponibles
        Plat plat = plats.get(new Random().nextInt(plats.size()));
        usedPlats.add(plat); // Ajouter à l'ensemble des plats utilisés
        return plat;
    }

    // Récupère les menus de la semaine
    public List<Menu> getAllMenus() {
        return menuRepository.findByOrderByDateAscIdAsc();
    }
}



