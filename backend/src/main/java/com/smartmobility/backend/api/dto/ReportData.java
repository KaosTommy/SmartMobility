package com.smartmobility.backend.api.dto;

public class ReportData {
    private int noleggiMensili;
    private int zoneAttive;
    private double tempoMedioRiparazioneOre;
    private int guastiAperti;

    public ReportData() {}

    public int getNoleggiMensili() { return noleggiMensili; }
    public void setNoleggiMensili(int noleggiMensili) { this.noleggiMensili = noleggiMensili; }
    public int getZoneAttive() { return zoneAttive; }
    public void setZoneAttive(int zoneAttive) { this.zoneAttive = zoneAttive; }
    public double getTempoMedioRiparazioneOre() { return tempoMedioRiparazioneOre; }
    public void setTempoMedioRiparazioneOre(double tempoMedioRiparazioneOre) { this.tempoMedioRiparazioneOre = tempoMedioRiparazioneOre; }
    public int getGuastiAperti() { return guastiAperti; }
    public void setGuastiAperti(int guastiAperti) { this.guastiAperti = guastiAperti; }
}