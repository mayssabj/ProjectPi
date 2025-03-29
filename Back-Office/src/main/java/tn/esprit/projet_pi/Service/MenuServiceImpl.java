package tn.esprit.projet_pi.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.projet_pi.Repository.MenuRepository;
import tn.esprit.projet_pi.Repository.PlatRepository;
import tn.esprit.projet_pi.Repository.RegimeAlimentaireRepository;
import tn.esprit.projet_pi.Repository.UserRepo;
import tn.esprit.projet_pi.entity.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
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

            User user = userOptional.get();

            // Get dates for next week (Monday to Sunday)
            LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));

            // Delete any existing non-validated menus for next week
            menuRepository.deleteByIsValidatedFalseAndDateBetween(
                    nextMonday,
                    nextMonday.plusDays(6)
            );

            List<RegimeAlimentaire> regimesExistants = (List<RegimeAlimentaire>) regimeAlimentaireRepository.findAll();
            Map<RegimeAlimentaireType, RegimeAlimentaire> regimeMap = new HashMap<>();

            for (RegimeAlimentaire regime : regimesExistants) {
                regimeMap.put(regime.getType(), regime);
            }

            // Pour chaque jour de la semaine (lundi à dimanche)
            for (int i = 0; i < 7; i++) {
                LocalDate date = nextMonday.plusDays(i);

                // Pour chaque type de régime
                for (RegimeAlimentaireType regimeType : RegimeAlimentaireType.values()) {
                    RegimeAlimentaire regimeAlimentaire = regimeMap.get(regimeType);

                    if (regimeAlimentaire == null) {
                        LOGGER.warning("⚠️ Aucun régime trouvé en base pour " + regimeType);
                        continue;
                    }

                    LOGGER.info("🟢 Génération du menu pour " + regimeType + " à la date " + date);

                    // Vérifier si un menu validé existe déjà
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
                        menu.setCreatedBy(user);
                        menu.setIsValidated(false); // Initial state is not validated

                        // Calculate and set total calories
                        menu.calculateTotalCalories();

                        menuRepository.save(menu);
                        LOGGER.info("✅ Menu enregistré pour " + date + " - Régime: " + regimeType +
                                " - Calories: " + menu.getTotalCalories());
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

    // Récupère tous les menus triés par date
    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findByOrderByDateAscIdAsc();
    }

    // Récupère uniquement les menus validés par un médecin
    @Override
    public List<Menu> getValidatedMenus() {
        return menuRepository.findByIsValidatedTrue();
    }

    // Valide plusieurs menus par un médecin
    @Override
    @Transactional
    public void validateMenus(Long doctorId, List<Long> menuIds) {
        User doctor = userRepository.findByidUser(doctorId)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));

        // Vérifier que l'utilisateur est bien un médecin
        if (!doctor.getRole().equals(Role.Medecin)) {
            throw new RuntimeException("Seuls les médecins peuvent valider les menus");
        }

        List<Menu> menus = menuRepository.findAllById(menuIds);
        for (Menu menu : menus) {
            menu.setIsValidated(true);
            menu.setValidatedBy(doctor);
        }

        menuRepository.saveAll(menus);
        LOGGER.info("✅ " + menus.size() + " menus validés par le médecin ID: " + doctorId);
    }

    // Régénère les menus de la semaine qui n'ont pas été validés
    @Override
    @Transactional
    public void regenerateWeeklyMenus(Long staffId) {
        // Récupérer tous les menus non validés
        List<Menu> nonValidatedMenus = menuRepository.findByIsValidatedFalse();

        LOGGER.info("🔄 Suppression de " + nonValidatedMenus.size() + " menus non validés");
        menuRepository.deleteAll(nonValidatedMenus);

        // Récupérer l'utilisateur staff
        User user = userRepository.findByidUser(staffId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Régénérer uniquement les menus pour les dates et régimes supprimés
        Map<LocalDate, Set<RegimeAlimentaireType>> menusToRegenerate = new HashMap<>();
        for (Menu menu : nonValidatedMenus) {
            LocalDate date = menu.getDate();
            RegimeAlimentaireType regime = menu.getRegime();

            if (!menusToRegenerate.containsKey(date)) {
                menusToRegenerate.put(date, new HashSet<>());
            }
            menusToRegenerate.get(date).add(regime);
        }

        // Générer les nouveaux menus pour chaque date et régime
        for (Map.Entry<LocalDate, Set<RegimeAlimentaireType>> entry : menusToRegenerate.entrySet()) {
            LocalDate date = entry.getKey();
            Set<RegimeAlimentaireType> regimes = entry.getValue();

            for (RegimeAlimentaireType regimeType : regimes) {
                RegimeAlimentaire regimeAlimentaire = regimeAlimentaireRepository.findByType(regimeType);

                if (regimeAlimentaire == null) {
                    LOGGER.warning("⚠️ Aucun régime trouvé en base pour " + regimeType);
                    continue;
                }

                LOGGER.info("🟢 Régénération du menu pour " + regimeType + " à la date " + date);

                List<Plat> platsDuJour = generateCompleteMenuForRegime(regimeAlimentaire, date);

                if (!platsDuJour.isEmpty()) {
                    Menu menu = new Menu();
                    menu.setDate(date);
                    menu.setRegime(regimeType);
                    menu.setPlats(platsDuJour);
                    menu.setCreatedBy(user);
                    menu.setIsValidated(false);

                    menu.calculateTotalCalories();

                    menuRepository.save(menu);
                    LOGGER.info("✅ Menu régénéré pour " + date + " - Régime: " + regimeType +
                            " - Calories: " + menu.getTotalCalories());
                } else {
                    LOGGER.warning("⚠️ Aucun plat disponible pour " + regimeType + " à la date " + date);
                }
            }
        }
    }

    // Méthode pour planifier la génération automatique des menus chaque vendredi
    @Override
    @Scheduled(cron = "0 0 0 * * FRI") // Exécute tous les vendredis à minuit
    public void scheduleMenuGeneration() {
        LOGGER.info("🔄 Exécution planifiée de la génération des menus hebdomadaires");
        try {
            // Trouver un utilisateur Staff pour la génération
            Optional<User> staffUser = userRepository.findAll().stream()
                    .filter(user -> user.getRole().equals(Role.Staff))
                    .findFirst();

            if (staffUser.isPresent()) {
                generateWeeklyMenus(staffUser.get().getId_user());
                LOGGER.info("✅ Génération automatique des menus terminée avec succès");
            } else {
                LOGGER.severe("❌ Aucun utilisateur Staff trouvé pour la génération automatique des menus");
            }
        } catch (Exception e) {
            LOGGER.severe("❌ Erreur lors de la génération automatique des menus : " + e.getMessage());
        }
    }
}
