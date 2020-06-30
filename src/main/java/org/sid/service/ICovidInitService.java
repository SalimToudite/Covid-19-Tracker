package org.sid.service;

import org.springframework.stereotype.Service;

import java.io.IOException;


public interface ICovidInitService {

    public void initInfected() throws IOException, InterruptedException;
    public void initDeaths() throws IOException, InterruptedException;
    public void initRecovered() throws IOException, InterruptedException;
}
