package com.smartmobility.backend.business;

import com.smartmobility.backend.api.dto.ReportData;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class GestoreReportistica {

    // IF-19
    public ReportData analizzaTrafficoUrbano(Date periodo) {
        ReportData report = new ReportData();
        report.setNoleggiMensili(1245);
        report.setZoneAttive(1); // MOCK data per l'Admin
        return report;
    }

    // IF-20
    public ReportData estraiStatisticheManutenzione() {
        ReportData report = new ReportData();
        report.setTempoMedioRiparazioneOre(4.2);
        report.setGuastiAperti(0); // MOCK data per efficienza flotta
        return report;
    }
}