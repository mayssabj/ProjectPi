package tn.esprit.projet_pi.Service;

import tn.esprit.projet_pi.entity.Menu;

import java.util.List;

public interface MenuService {
    public void generateWeeklyMenus(Long userId);
    //public List<Menu> getMenusDeLaSemaine();
    public List<Menu> getAllMenus();
}