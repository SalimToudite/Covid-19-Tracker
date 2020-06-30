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
        for (CSVRecord record : records) {
            Infected infected = new Infected();
            infected.setState(record.get("Province/State"));
            infected.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
            infected.setLatestTotalCases(latestCases);
            infected.setDiffFromPrevDay(latestCases - prevDayCases);
            newStats.add(infected);
            infectedRepository.save(infected);
        }
        this.InfectedStats = newStats;

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
        for (CSVRecord record : records) {
            Deaths deaths = new Deaths();
            deaths.setState(record.get("Province/State"));
            deaths.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
            deaths.setLatestDeathsCases(latestCases);
            deaths.setDiffFromPrevDay(latestCases - prevDayCases);
            newStats.add(deaths);
            deathsRepository.save(deaths);
        }
        this.DeathsStats = newStats;

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
        for (CSVRecord record : records) {
            Recovered recovered = new Recovered();
            recovered.setState(record.get("Province/State"));
            recovered.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
            recovered.setLatestRecoveredCases(latestCases);
            recovered.setDiffFromPrevDay(latestCases - prevDayCases);
            newStats.add(recovered);
            recoveredRepository.save(recovered);
        }
        this.RecoveredStats = newStats;

    }
}
