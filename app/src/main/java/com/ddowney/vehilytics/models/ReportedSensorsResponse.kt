package com.ddowney.vehilytics.models

data class ReportedSensorsResponse(val sensors: List<Sensor>,
                                   val warnings: List<String>,
                                   val errors: List<String>)