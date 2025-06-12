package com.neuromotion.backend.enums;

import java.time.DayOfWeek;

public enum DiaSemana {
    LUNES,
    MARTES,
    MIERCOLES,
    JUEVES,
    VIERNES,
    SABADO,
    DOMINGO;

  public DayOfWeek toJavaDayOfWeek() {
        return DayOfWeek.valueOf(this.name());
    }

}
