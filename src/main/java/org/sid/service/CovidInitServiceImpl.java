package org.sid.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.sid.dao.DeathsRepository;
import org.sid.dao.InfectedRepository;
import org.sid.dao.RecoveredRepository;
import org.sid.model.Deaths;
import org.sid.model.Infected;
import org.sid.model.Recovered;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CovidInitServiceImpl implements ICovidInitService {

    private static String INFECTED_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private static String DEATHS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";
    private static String RECOVERED_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv";

    @Autowired
    private InfectedRepository infectedRepository;
    @Autowired
    private DeathsRepository deathsRepository;
    @Autowired
    private RecoveredRepository recoveredRepository;

    private List<Infected> InfectedStats = new ArrayList<>();
    private List<Deaths> DeathsStats = new ArrayList<>();
    private List<Recovered> RecoveredStats = new ArrayList<>();
    private HashMap<String,Long> monthInfectedData = new HashMap<>();
    private HashMap<String,Long> monthDeathsData = new HashMap<>();
    private HashMap<String,Long> monthRecoveredData = new HashMap<>();

    public HashMap<String, Long> getMonthRecoveredData() {
        return monthRecoveredData;
    }

    public HashMap<String, Long> getMonthDeathsData() {
        return monthDeathsData;
    }

    public HashMap<String, Long> getMonthInfectedData() {
        return monthInfectedData;
    }

    public List<Infected> getInfectedStats() {
        return InfectedStats;
    }

    public List<Deaths> getDeathsStats() {
        return DeathsStats;
    }

    public List<Recovered> getRecoveredStats() {
        return RecoveredStats;
    }

    @Override
    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void initInfected() throws IOException, InterruptedException  {
        List<Infected> newStats = new ArrayList<>();
       HttpClient client1 = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(INFECTED_DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client1.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        long januaryCases=0;
        long februaryCases=0;
        long marchCases =0;
        long aprilCases =0;
        long mayCases =0;
        long juneCases =0;

        for (CSVRecord record : records) {
            Infected infected = new Infected();

            infected.setState(record.get("Province/State"));
            infected.setCountry(record.get("Country/Region"));
             int latestCases = Integer.parseInt(record.get(record.size() - 1));
             int prevDayCases = Integer.parseInt(record.get(record.size() - 2));

            januaryCases = januaryCases+ Integer.parseInt((record.get(13)));
             februaryCases = februaryCases+ Integer.parseInt((record.get(42)));
             marchCases = marchCases+ Integer.parseInt((record.get(73)));
             aprilCases = aprilCases + Integer.parseInt((record.get(103)));
             mayCases =mayCases + Integer.parseInt((record.get(134)));
             juneCases =juneCases + Integer.parseInt((record.get(164)));

            infected.setLatestTotalCases(latestCases);
            infected.setDiffFromPrevDay(latestCases - prevDayCases);
            newStats.add(infected);
            infectedRepository.save(infected);
        }
        this.InfectedStats = newStats;
        monthInfectedData.put("January",januaryCases);
        monthInfectedData.put("February",februaryCases - januaryCases);
        monthInfectedData.put("March",marchCases - februaryCases);
        monthInfectedData.put("April",aprilCases - marchCases);
        monthInfectedData.put("May",mayCases - aprilCases);
        monthInfectedData.put("June",juneCases - mayCases);

    }

    @Override
    public void initDeaths() throws IOException, InterruptedException  {
        List<Deaths> newStats = new ArrayList<>();
        HttpClient client2 = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DEATHS_DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client2.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        long januaryCases=0;
        long februaryCases=0;
        long marchCases =0;
        long aprilCases =0;
        long mayCases =0;
        long juneCases =0;
        for (CSVRecord record : records) {
            Deaths deaths = new Deaths();
            deaths.setState(record.get("Province/State"));
            deaths.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));

             januaryCases= januaryCases + Integer.parseInt((record.get(13)));
             februaryCases =februaryCases+ Integer.parseInt((record.get(42)));
             marchCases =marchCases+ Integer.parseInt((record.get(73)));
             aprilCases =aprilCases+ Integer.parseInt((record.get(103)));
             mayCases =mayCases+ Integer.parseInt((record.get(134)));
             juneCases =juneCases+ Integer.parseInt((record.get(164)));

            deaths.setLatestDeathsCases(latestCases);
            deaths.setDiffFromPrevDay(latestCases - prevDayCases);
            newStats.add(deaths);
            deathsRepository.save(deaths);
        }
        this.DeathsStats = newStats;
        monthDeathsData.put("January",januaryCases);
        monthDeathsData.put("February",februaryCases - januaryCases);
        monthDeathsData.put("March",marchCases - februaryCases);
        monthDeathsData.put("April",aprilCases - marchCases);
        monthDeathsData.put("May",mayCases - aprilCases);
        monthDeathsData.put("June",juneCases - mayCases);

    }

    @Override
    public void initRecovered() throws IOException, InterruptedException  {
        List<Recovered> newStats = new ArrayList<>();
        HttpClient client3 = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RECOVERED_DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client3.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        long januaryCases=0;
        long februaryCases=0;
        long marchCases =0;
        long aprilCases =0;
        long mayCases =0;
        long juneCases =0;
        for (CSVRecord record : records) {
            Recovered recovered = new Recovered();
            recovered.setState(record.get("Province/State"));
            recovered.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));

             januaryCases =januaryCases+ Integer.parseInt((record.get(13)));
             februaryCases =februaryCases+ Integer.parseInt((record.get(42)));
             marchCases =marchCases+ Integer.parseInt((record.get(73)));
             aprilCases =aprilCases+ Integer.parseInt((record.get(103)));
             mayCases =mayCases+ Integer.parseInt((record.get(134)));
             juneCases =juneCases+ Integer.parseInt((record.get(164)));


            recovered.setLatestRecoveredCases(latestCases);
            recovered.setDiffFromPrevDay(latestCases - prevDayCases);
            newStats.add(recovered);
            recoveredRepository.save(recovered);
        }
        this.RecoveredStats = newStats;
        monthRecoveredData.put("January",januaryCases);
        monthRecoveredData.put("February",februaryCases - januaryCases);
        monthRecoveredData.put("March",marchCases - februaryCases);
        monthRecoveredData.put("April",aprilCases - marchCases);
        monthRecoveredData.put("May",mayCases - aprilCases);
        monthRecoveredData.put("June",juneCases - mayCases);


    }
}
