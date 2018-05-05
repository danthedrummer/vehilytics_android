package com.ddowney.vehilytics.models

import java.io.Serializable

data class Sensor(val id: String, val name: String,
                  val shortname: String, val unit: String) : Serializable