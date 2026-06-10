package com.smartmobility.backend.core;

import com.smartmobility.backend.model.*;
import com.smartmobility.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UtenteRepository utenteRepository;
    private final MezzoRepository mezzoRepository;
    private final HubRicaricaRepository hubRepository;

    @Autowired
    public DataSeeder(UtenteRepository utenteRepository, MezzoRepository mezzoRepository, HubRicaricaRepository hubRepository) {
        this.utenteRepository = utenteRepository;
        this.mezzoRepository = mezzoRepository;
        this.hubRepository = hubRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        mezzoRepository.deleteAll();
        utenteRepository.deleteAll();
        hubRepository.deleteAll();

        // 1. POPOLAMENTO UTENTI (RBAC - Role Based Access Control)
        // La password per tutti nei test sarà "password123" (simuliamo l'hash per comodità)
        Utente u1 = new Utente("U_825055", "Tommaso", "Gelao", "t.gelao2@studenti.uniba.it", "password123", "UTENTE");
        u1.setPatenteBValida(true);
        u1.aggiungiFondi(50.0f); // Ti diamo 50 euro per i test

        Utente u2 = new Utente("U_827882", "Massimo", "Morisco", "m.morisco12@studenti.uniba.it", "password123", "OPERATORE");
        Utente u3 = new Utente("U_831053", "Antonello", "Antenori", "a.antenori1@studenti.uniba.it", "password123", "ADMIN");
        Utente u4 = new Utente("U_828273", "Andrea", "Estivo", "a.estivo@studenti.uniba.it", "password123", "UTENTE");

        utenteRepository.saveAll(List.of(u1, u2, u3, u4));

        // 2. POPOLAMENTO FLOTTA ESPANSA (Zootropolis)
        // Monopattini
        Monopattino m1 = new Monopattino("MN_001", "SENS_M1"); m1.setLatitudine(41.1171); m1.setLongitudine(16.8718);
        Monopattino m2 = new Monopattino("MN_002", "SENS_M2"); m2.setLatitudine(41.1185); m2.setLongitudine(16.8700); m2.setLivelloBatteria(8); // Guasto/Scarico
        
        // Biciclette
        Bicicletta b1 = new Bicicletta("BC_001", "SENS_B1"); b1.setLatitudine(41.1160); b1.setLongitudine(16.8730);
        Bicicletta b2 = new Bicicletta("BC_002", "SENS_B2"); b2.setLatitudine(41.1190); b2.setLongitudine(16.8680);

        // Auto
        Auto a1 = new Auto("AU_001", "SENS_A1"); a1.setLatitudine(41.1150); a1.setLongitudine(16.8750);
        
        // Motocicli
        Motociclo mt1 = new Motociclo("MT_001", "SENS_MT1"); mt1.setLatitudine(41.1140); mt1.setLongitudine(16.8720);

        mezzoRepository.saveAll(List.of(m1, m2, b1, b2, a1, mt1));

        // 3. HUB DI RICARICA
        HubRicarica h1 = new HubRicarica("HUB_01", "Stazione FS", 41.1175, 16.8690, 10);
        HubRicarica h2 = new HubRicarica("HUB_02", "Piazza Umberto", 41.1200, 16.8710, 15);
        hubRepository.saveAll(List.of(h1, h2));

        System.out.println("====================================================");
        System.out.println("✅ BACKEND AGGIORNATO E POPOLATO (MODELLO 1:1)");
        System.out.println("🔑 Utenti, Operatori e Admin generati.");
        System.out.println("====================================================");
    }
}