package pl.kobietydokodu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.kobietydokodu.domain.Kot;
import pl.kobietydokodu.dto.KotDTO;
import pl.kobietydokodu.service.JpaKotDAO;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Controller
public class KotyController {


@Autowired
public JpaKotDAO jpaKotDAO = new JpaKotDAO();

// w tej metodzie przenoszę do widoku message
    @RequestMapping("/start")
    public String metoda(Model model) {
    model.addAttribute("message","Możesz dodać nowego kota, wyświetlić listę kotów i informacje o wybranym kocie");
    return "glowny";
}
    @RequestMapping("/listakotow")
    public String listaKotow(Model model) {
                model.addAttribute("koty", jpaKotDAO.getList());
        return "listakotow";
    }
    @RequestMapping("/kot-{id}")
    public String pokazKota(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("kot", jpaKotDAO.getKotById(id));
        return "jeden";
    }
    @RequestMapping(value = "/dodajkota", method = RequestMethod.GET)
    public String dodajkota(Model model) {
    //to wstawiłam wg Mkyong.com:
    model.addAttribute("blank", new KotDTO());
    return "formularz";
    }
    @RequestMapping(value = "/dodajkota", method = RequestMethod.POST)
    public String dodajkota(@ModelAttribute("blank") @Valid KotDTO blank, BindingResult result, Model model) {
        if (result.hasErrors()) {
            //formularz nie jest uzupełniony prawidłowo
            return "formularz";
        } else {
            //formularz wypełniony prawidłowo
            Kot cat=new Kot();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            cat.setName(blank.getName());
            try{
                cat.setDateOfBirth(sdf.parse(blank.getDateOfBirth()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cat.setWeight(blank.getWeight());
            cat.setCatOwner(blank.getCatOwner());
            System.out.println("utworzony został nowy cat na podstawie formularza: ");
            System.out.println(cat.przedstawSie());
            boolean isCatNew = jpaKotDAO.dodajKota(cat);
            if (!isCatNew) {
                model.addAttribute("msg", "Twój kot jest już w Bazie Kotów, sprawdź:");
                System.out.println("spełniony warunek !isCatNew");
            }
            model.addAttribute("koty", jpaKotDAO.getList());
//            return "redirect:/listakotow";
//            powoduje wykonanie metody, więc tutaj nowy connect, nowe model.addAttribute, stare znikają, np msg
return "listakotow";
//następuje tylko wywołanie widoku przy atrybutach nadanych powyżej
        }
    }
}
