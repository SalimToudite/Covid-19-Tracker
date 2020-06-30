package org.sid.Controller;

import org.sid.dao.DeathsRepository;
import org.sid.dao.InfectedRepository;
import org.sid.dao.RecoveredRepository;
import org.sid.model.Deaths;
import org.sid.model.Infected;
import org.sid.model.Recovered;
import org.sid.service.CovidInitServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CovidInitServiceImpl covidInitService;
    @Autowired
    private InfectedRepository infectedRepository;
    @Autowired
    private DeathsRepository deathsRepository;
    @Autowired
    private RecoveredRepository recoveredRepository;

    @GetMapping("/")
    public String home(Model model) {
        List<Infected> infectedList = covidInitService.getInfectedStats();
        List<Deaths> deathsList = covidInitService.getDeathsStats();
        List<Recovered> recoveredList = covidInitService.getRecoveredStats();
        int totalInfectedCases = infectedList.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalDeathsCases = deathsList.stream().mapToInt((stat -> stat.getLatestDeathsCases())).sum();
        int totalRecoveredCases = recoveredList.stream().mapToInt((stat -> stat.getLatestRecoveredCases())).sum();
        model.addAttribute("infectedList", infectedList);
        model.addAttribute("totalReportedCases", totalInfectedCases);
        model.addAttribute("deathsList", deathsList);
        model.addAttribute("totalDeathsCases", totalDeathsCases);
        model.addAttribute("recoveredList", recoveredList);
        model.addAttribute("totalRecoveredCases", totalRecoveredCases);

        return "home";
    }

    @GetMapping("/infected")
    public String infected(Model model) {
        List<Infected> infectedList = covidInitService.getInfectedStats();
        int totalInfectedCases = infectedList.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = infectedList.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
        model.addAttribute("infectedList", infectedList);
        model.addAttribute("totalInfectedCases", totalInfectedCases);
        model.addAttribute("totalNewCases", totalNewCases);

        return "infected";
    }

    @GetMapping("/deaths")
    public String deaths(Model model) {
        List<Deaths> AllDeaths = covidInitService.getDeathsStats();
        int totalDeathsCases = AllDeaths.stream().mapToInt((stat -> stat.getLatestDeathsCases())).sum();
        int totalNewCases = AllDeaths.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
        model.addAttribute("AllDeaths", AllDeaths);
        model.addAttribute("totalDeathsCases", totalDeathsCases);
        model.addAttribute("totalNewCases", totalNewCases);
        return "deaths";
    }

    @GetMapping("/recovered")
    public String recovered(Model model) {
        List<Recovered> recoveredList = covidInitService.getRecoveredStats();
        int totalRecoveredCases = recoveredList.stream().mapToInt((stat -> stat.getLatestRecoveredCases())).sum();
        int totalNewCases = recoveredList.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
        model.addAttribute("recoveredList",recoveredList);
        model.addAttribute("totalRecoveredCases", totalRecoveredCases);
        model.addAttribute("totalNewCases", totalNewCases);

        return "recovered";
    }

}
