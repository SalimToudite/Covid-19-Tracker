package org.sid.Controller;

import org.sid.model.Deaths;
import org.sid.model.Infected;
import org.sid.model.Recovered;
import org.sid.service.CovidInitServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private CovidInitServiceImpl covidInitService;


    @GetMapping("/")
    public String home(Model model) {
        Map<String,Long > infectedMonthsCases = covidInitService.getMonthInfectedData();
        Map<String,Long > deathsMonthsCases = covidInitService.getMonthDeathsData();
        Map<String,Long > recoveredMonthsCases = covidInitService.getMonthRecoveredData();
        model.addAttribute("infectedMonthsCases",infectedMonthsCases);
        model.addAttribute("deathsMonthsCases",deathsMonthsCases);
        model.addAttribute("recoveredMonthsCases",recoveredMonthsCases);

        List<Infected> infectedList = covidInitService.getInfectedStats();
        List<Deaths> deathsList = covidInitService.getDeathsStats();
        List<Recovered> recoveredList = covidInitService.getRecoveredStats();
        int totalInfectedCases = infectedList.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalDeathsCases = deathsList.stream().mapToInt((stat -> stat.getLatestDeathsCases())).sum();
        int totalRecoveredCases = recoveredList.stream().mapToInt((stat -> stat.getLatestRecoveredCases())).sum();
        model.addAttribute("totalReportedCases", totalInfectedCases);
        model.addAttribute("totalDeathsCases", totalDeathsCases);
        model.addAttribute("totalRecoveredCases", totalRecoveredCases);

        return "home";
    }

    @GetMapping("/infected")
    public String infected(Model model) {
        Map<String,Long > monthsCases = covidInitService.getMonthInfectedData();
        model.addAttribute("monthsCases",monthsCases);
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
        Map<String,Long > monthsCases = covidInitService.getMonthDeathsData();
        model.addAttribute("monthsCases",monthsCases);
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
        Map<String,Long > monthsCases = covidInitService.getMonthRecoveredData();
        model.addAttribute("monthsCases",monthsCases);
        List<Recovered> recoveredList = covidInitService.getRecoveredStats();
        int totalRecoveredCases = recoveredList.stream().mapToInt((stat -> stat.getLatestRecoveredCases())).sum();
        int totalNewCases = recoveredList.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
        model.addAttribute("recoveredList",recoveredList);
        model.addAttribute("totalRecoveredCases", totalRecoveredCases);
        model.addAttribute("totalNewCases", totalNewCases);

        return "recovered";
    }

}
