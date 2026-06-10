package com.smartmobility.backend.api.dto;

public record DashboardStats(
    long totaleUtenti,
    long mezziDisponibili,
    long mezziInUso,
    long mezziInManutenzione,
    long corseTotali,
    float incassoStimato
) {}