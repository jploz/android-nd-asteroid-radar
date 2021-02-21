package com.udacity.asteroidradar.api

import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test


const val EPSILON_DOUBLE = 1e-12


class NetworkUtilsKtTest {

    @Test
    fun parseDateAsteroidJson_correctJsonFormat() {

        val asteroidJson = """{
            "links": {
              "self": "http://www.neowsapp.com/rest/v1/neo/2069230?api_key=DEMO_KEY"
            },
            "id": "2069230",
            "neo_reference_id": "2069230",
            "name": "69230 Hermes (1937 UB)",
            "nasa_jpl_url": "http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=2069230",
            "absolute_magnitude_h": 17.5,
            "estimated_diameter": {
              "kilometers": {
                "estimated_diameter_min": 0.8405334021,
                "estimated_diameter_max": 1.8794898244
              },
              "meters": {
                "estimated_diameter_min": 840.5334020728,
                "estimated_diameter_max": 1879.4898243938
              },
              "miles": {
                "estimated_diameter_min": 0.5222830806,
                "estimated_diameter_max": 1.1678604717
              },
              "feet": {
                "estimated_diameter_min": 2757.6556068564,
                "estimated_diameter_max": 6166.3053954643
              }
            },
            "is_potentially_hazardous_asteroid": true,
            "close_approach_data": [
              {
                "close_approach_date": "2020-12-02",
                "close_approach_date_full": "2020-Dec-02 07:59",
                "epoch_date_close_approach": 1606895940000,
                "relative_velocity": {
                  "kilometers_per_second": "12.6073094776",
                  "kilometers_per_hour": "45386.3141193186",
                  "miles_per_hour": "28201.2905704177"
                },
                "miss_distance": {
                  "astronomical": "0.2616445704",
                  "lunar": "101.7797378856",
                  "kilometers": "39141470.428905048",
                  "miles": "24321381.9270557424"
                },
                "orbiting_body": "Earth"
              }
            ],
            "is_sentry_object": false
          }"""

        val formattedDate = "2020-12-02"

        val asteroidJsonObject = JSONObject(asteroidJson)

        val asteroid = parseDateAsteroidJson(formattedDate, asteroidJsonObject)

        assertEquals(2069230, asteroid.id)
        assertEquals("69230 Hermes (1937 UB)", asteroid.codename)
        assertEquals("2020-12-02", asteroid.closeApproachDate)
        assertEquals(17.5, asteroid.absoluteMagnitude, EPSILON_DOUBLE)
        assertEquals(1.8794898244, asteroid.estimatedDiameter, EPSILON_DOUBLE)
        assertEquals(12.6073094776, asteroid.relativeVelocity, EPSILON_DOUBLE)
        assertEquals(0.2616445704, asteroid.distanceFromEarth, EPSILON_DOUBLE)
        assertEquals(true, asteroid.isPotentiallyHazardous)
    }
}
