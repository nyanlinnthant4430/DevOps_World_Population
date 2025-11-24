![workflow](https://github.com/<nyanlinnthant4430>/<DevOps_World_Population>/actions/workflows/main.yml/badge.svg)
# DevOPs_BSc_World_Population

A Java-driven DevOps project designed to generate world population reports through SQL queries and object-oriented programming. The system fetches and presents population data for countries, cities, regions, and languages, organized and ranked by population. It also showcases professional software engineering practices, including Scrum methodology, Git branching strategies, and continuous integration.



**Roles and Responsibilities of Team Members**

| Matriculation Number | Name            | Role          | Responsibilities                                                                                                                                               |
|----------------------|-----------------|---------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 40804626             | Min Pyae Soe    | Scrum Master  | Managed GitHub repo, Sprint Board, SQL code analysis, team coordination                                                                                        |
| 40804627             | Nyan Linn Thant | Product Owner | Requirements management, documentation, use case diagrams, formatted reports                                                                                   |
| 40804628             | Aung Win Htun   | Developer 1   | Docker setup, App.java, diagrams, integration tests for reports, SQL code, Coding for each Responsible Reports, reviewed other developers’ report/testing code |
| 40804619             | Nyein Su Nandar | Developer 2   | GitHub repo structure, CI/CD pipeline setup, SQL code, Integration tests for reports, Coding for each Responsible Reports                                      |
| 40804620             | Ye Htet Aung    | Developer 3   | Data model, service layer development, deployment configuration/testing, integration tests for reports, SQL code, Coding for each Responsible Reports          |
| 40804633             | Aung Myat       | Developer 4   | Formatted report generation, Integration tests for reports, JUnit tests, SQL code, Coding for each Responsible Reports                                         |




## ✅ Requirements Implementation Status

**32 requirements of 32 have been implemented — 100% completed.**

This section reflects the current progress of the **World Population Report System** based on the official project criteria.

[![codecov](https://codecov.io/gh/nyanlinnthant4430/DevOps_World_Population/branch/featuretest/graph/badge.svg)](https://codecov.io/gh/nyanlinnthant4430/DevOps_World_Population)

## Requirements Evidence Table
All Reports for World_Population

| ID | Requirement Name                                                                                      | Met? | Screenshot                                                                                                                                                                    |
|----|-------------------------------------------------------------------------------------------------------|-----|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1  | All countries in the world organised by population (largest to smallest)                              | Yes | ![](screenshots/ReportsbyPopulation/ReportsbyPopulation/AllCountriesintheworld.png)                                                                                           |
| 2  | All countries in a continent organised by population (largest to smallest)                            | Yes | ![](screenshots/ReportsbyPopulation/ReportsbyPopulation/AllCountriesinContinent.png)                                                                                          |
| 3  | All countries in a region organised by population (largest to smallest)                               | Yes | ![](screenshots/ReportsbyPopulation/ReportsbyPopulation/AllCountriesinRegion.png)                                                                                             |
| 4  | Top N populated countries in the world                                                                | Yes | ![](screenshots/ReportsbyPopulation/ReportsbyPopulation/TopNPopulatedCountriesInTheWorld.png)                                                                                 |
| 5  | Top N populated countries in a continent                                                              | Yes | ![](screenshots/ReportsbyPopulation/ReportsbyPopulation/TopNPopulatedCountriesByContinent.png)                                                                                |
| 6  | Top N populated countries in a region                                                                 | Yes | ![](screenshots/ReportsbyPopulation/ReportsbyPopulation/TopNPopulatedCountriesByRegion.png)                                                                                   |
| 7  | All the cities in the world organised by largest population to smallest.                              | Yes | ![](screenshots/FeatureCity_Report/FeatureCity_Report/All%20cities%20in%20the%20world%20by%20population.jpg)                                                                  |
| 8  | All the cities in a continent organised by largest population to smallest.                            | Yes | ![](screenshots/FeatureCity_Report/FeatureCity_Report/All%20cities%20in%20a%20continent%20by%20population.jpg)                                                                |
| 9  | All the cities in a region organised by largest population to smallest.                               | Yes | ![](screenshots/FeatureCity_Report/FeatureCity_Report/All%20cities%20in%20a%20region%20by%20population.jpg)                                                                   |
| 10 | All the cities in a country organised by largest population to smallest.                              | Yes | ![](screenshots/FeatureCity_Report/FeatureCity_Report/All%20cities%20in%20a%20country%20by%20population.jpg)                                                                  |
| 11 | All the cities in a district organised by largest population to smallest.                             | Yes | ![](screenshots/FeatureCity_Report/FeatureCity_Report/All%20cities%20in%20a%20district%20by%20population.jpg)                                                                 |
| 12 | Top N populated cities in the world                                                                   | Yes | ![](screenshots/FeatureCity_Report/FeatureCity_Report/Top%20N%20populated%20cities%20in%20the%20world.jpg)                                                                    |
| 13 | Top N populated cities in a continent                                                                 | Yes | ![](screenshots/FeatureCity_Report/FeatureCity_Report/Top%20N%20populated%20cities%20in%20a%20continent.jpg)                                                                  |
| 14 | Top N populated cities in a region                                                                    | Yes | ![](screenshots/FeatureCity_Report/FeatureCity_Report/Top%20N%20populated%20cities%20in%20a%20region.jpg)                                                                     |
| 15 | Top N populated cities in a country                                                                   | Yes | ![](screenshots/FeatureCity_Report/FeatureCity_Report/Top%20N%20populated%20cities%20in%20a%20country.jpg)                                                                    |
| 16 | Top N populated cities in a district                                                                  | Yes | ![](screenshots/FeatureCity_Report/FeatureCity_Report/Top%20N%20populated%20cities%20in%20a%20district.jpg)                                                                   |
| 17 | All the capital cities in the world organised by largest population to smallest.                      | Yes | ![](screenshots/city_report/city_report/Capital%20Cities%20in%20the%20world.png)                                                                                              |
| 18 | All the capital cities in a continent organised by largest population to smallest.                    | Yes | ![](screenshots/city_report/city_report/Capital%20Cities%20in%20the%20Continent.png)                                                                                          |
| 19 | All the capital cities in a region organised by largest to smallest.                                  | Yes | ![](screenshots/city_report/city_report/Capital%20Cities%20in%20the%20region.png)                                                                                             |
| 20 | The top N populated capital cities in the world where N is provided by the user.                      | Yes | ![](screenshots/city_report/city_report/Top%20N%20Capital%20Cities%20in%20the%20world.png)                                                                                    |
| 21 | The top N populated capital cities in a continent where N is provided by the user.                    | Yes | ![](screenshots/city_report/city_report/Top%20N%20Capital%20Cities%20in%20the%20Continent.png)                                                                                |
| 22 | The top N populated capital cities in a region where N is provided by the user.                       | Yes | ![](screenshots/city_report/city_report/Top%20N%20Capital%20Cities%20in%20the%20Region.png)                                                                                   |
| 23 | The population of people, people living in cities, and people not living in cities in each continent. | Yes | ![](screenshots/Population%20Report/Population%20Report/The%20population%20of%20people%20living%20in%20cities%20and%20not%20living%20in%20cities%20in%20each%20continent.png) |
| 24 | The population of people, people living in cities, and people not living in cities in each region.    | Yes | ![](screenshots/Population%20Report/Population%20Report/The%20population%20of%20people%20living%20in%20cities%20and%20not%20living%20in%20cities%20in%20each%20country.png)   |
| 25 | The population of people, people living in cities, and people not living in cities in each country.   | Yes | ![](screenshots/Population%20Report/Population%20Report/The%20population%20of%20people%20living%20in%20cities%20and%20not%20living%20in%20cities%20in%20each%20region.png)    |
| 26 | Population of The World                                                                               | Yes | ![](screenshots/AdditionalReports/AdditionalReports/PopulationintheWorld.png)                                                                                                                                                      |
| 27 | Population of A Continent                                                                             | Yes | ![](screenshots/AdditionalReports/AdditionalReports/PopulationinaContinent.png)                                                                                                                                                      |
| 28 | Population of A Region                                                                                | Yes | ![](screenshots/AdditionalReports/AdditionalReports/PopulationinaRegion.png)                                                                                                                                                      |
| 29 | Population of A Country                                                                               | Yes | ![](screenshots/AdditionalReports/AdditionalReports/PopulationinaCountry.png)                                                                                                                                                      |
| 30 | Population of A District                                                                              | Yes | ![](screenshots/AdditionalReports/AdditionalReports/PopulationinaDistrict.png)                                                                                                                                                      |
| 31 | Population of A City                                                                                  | Yes | ![](screenshots/AdditionalReports/AdditionalReports/PopulationinaCity.png)                                                                                                |
| 32 | Population By Spoken Language                                                                         | Yes | ![](screenshots/AdditionalReports/AdditionalReports/PopulationBySpokenLanguages.png)                                                                                                      |

