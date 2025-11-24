# World Population Report System

A Java-driven DevOps project designed to generate world population reports through SQL queries and object-oriented programming. The system fetches and presents population data for countries, cities, regions, and languages, organized and ranked by population. It also showcases professional software engineering practices, including Scrum methodology, Git branching strategies, and continuous integration.

## Requirements Implementation Status

32 requirements of 32 have been implemented, which is **100%** completed.

> This reflects the current progress of the World Population Report System based on the official project criteria.
---

* **Master Build Status** ![Master Build Status](https://img.shields.io/github/actions/workflow/status/nyanlinnthant4430/DevOps_World_Population/main.yml?branch=master)
* **Develop Build Status** ![Develop Build Status](https://img.shields.io/github/actions/workflow/status/nyanlinnthant4430/DevOps_World_Population/main.yml?branch=develop)
* **License** ![GitHub license](https://img.shields.io/github/license/nyanlinnthant4430/DevOps_World_Population)
* **Release** [![Releases](https://img.shields.io/github/release/nyanlinnthant4430/DevOps_World_Population/all.svg?style=flat-square)](https://github.com/nyanlinnthant4430/DevOps_World_Population/releases)
* [![codecov](https://codecov.io/gh/nyanlinnthant4430/DevOps_World_Population/graph/badge.svg?token=PUA2YDA3R2)](https://codecov.io/gh/nyanlinnthant4430/DevOps_World_Population)


##  Role of Team Members & Responsibilities

| Student ID   | Name              | Role                     | Responsibility                                                                               |
|--------------|-------------------|--------------------------|----------------------------------------------------------------------------------------------|
| **40804626** | [Min Pyae Soe]    | Scrum Master / Developer | Backlog and Sprint/ Git Management/ Merge Branches                                           |
| **40804627** | [Nyan Linn Thant] | Product Owner/ Developer | Overall Management/ Backlog and Sprint / Git Management/ Plant UML / Use Case/ Documentation |
| **40804628** | [Aung Win Tun]    | Developer / Tester       | Country Report / Population Report / Testing                                                 |
| **40804619** | [Nyein Su Nandar] | Developer / Tester       | Capital City Report / Testing                                                                |
| **40894620** | [Ye Htet Aung]    | Developer / Tester       | City Report / Testing                                                                        |
| **40804633** | [Aung Myat]       | Developer / Tester       | Policy Report / Testing                                                                      |


### Branching Strategy
- **master** → Production-ready, stable code
- **develop** → Integration branch for all new features
- **release** → Final testing before merging into `master`
- **feature/** → Individual features developed by team members


### Packages
- **com.napier.devops** -> Main Application Classes
- **com.napier.devops.basicpopulation** -> Population Reports
- **com.napier.devops.city_report** -> Capital City Reports
- **com.napier.devops.country_report** -> Country Reports
- **com.napier.devops.feature_policymaker** -> Policy Reports
- **com.napier.devops.FeatureCity_report** -> City Reports

### Features
- Produces population reports for **Countries, Cities, and Regions**
- Allows filtering by **Continent, Region and Language**
- Display data for ** Populated Countries, Capital Cities, Cities, or Regions**
- Built with a modular Java structure to enhance scalability and easy maintenance
- Integrates **SQL** to provide accurate, real-time population data
- Offers **multiple report perspectives** likes (World, Continent, Region, Capital Cities)
- Includes **Unit tests** and validation scripts to ensure dependable results
- Fully **Docker-Compatible** for consistent development and deployment environments.
---

## Tools
- Intellij IDEA (IDE)
- Zube for Product Backlog and Sprints
- Maven for build and dependency management
- GitHub for version control
- Docker for containerized database and application
- Code Cov for testing


##  Instructions for Setup

###  Prerequisites
- **Java JDK 19**
- **MySQL Server**
- **Apache Maven**
- **IntelliJ IDEA** (recommended IDE)

###  How to Run the Application

1. **Clone the repository**
   ```bash
   git clone https://github.com/nyanlinnthant4430/DevOps_World_Population.git
   cd DevOps_World_Population

2. **Build The Project**
   mvn compile and package

   mvn exec:java - mainClass="com.napier.devops.App"

3. app.connect("localhost:3306", 33060, "db/world_population/world.sql", "root", "password");

## Usage

1. Start the application and choose the type of report you want.
2. Apply filters based on the report criteria you need.
3. The results will appear in the console as neatly formatted tables.


# Contribution Guidelines

Thank you so much for your interest in contributing to the **World Population Report System**.
To ensure high code quality, consistency, and effective teamwork, please follow the guidelines outlined below.

---

## Phase 1: Set Up Your Local Environment

1. Ensure you have the following installed:

    * **Java JDK 17+**
    * **MySQL Server**
    * **Apache Maven**
    * **IntelliJ IDEA** (or any preferred Java IDE)
2. Clone the repository:

```bash
git clone https://github.com/nyanlinnthant4430/DevOps_World_Population.git
ccd DevOps_World_Population
```

3. Build the project to verify setup:

```bash
mvn compile package
```

---

## Phase 2: Synchronize with the Main Repository

1. Switch to the `develop` branch:

```bash
git checkout develop
```

2. Pull the latest changes:

```bash
git pull origin develop
```

---

## Phase 3: Create a Feature Branch

1. Create a new branch for your task or report:

```bash
git checkout -b feature/<your-feature-name>
```

*Example:* `feature/country-report`
2. Make all changes **only** in this branch.
3. Commit often and use clear, descriptive commit messages.

```bash
git add .
git commit -m "Add country_Report feature"
```

---

## Phase 4: Push Your Branch to GitHub

```bash
git push origin feature/<your-feature-name>
```

---

## Phase 5: Submit a Pull Request (PR)

1. Go to GitHub and create a **PR from your feature branch to `develop`**.
2. Include a **clear title and description**:

    * What feature or fix you implemented
    * Any related issue or task
    * Steps to test the changes
3. Request **at least one team member** for review.

---

## Phase 6: Review and Merge

1. Address any **feedback** or requested changes.
2. Once approved, **merge the PR into `develop`**.
3. Delete your feature branch to keep the repository clean:

```bash
git branch -d feature/<your-feature-name>
git push origin --delete feature/<your-feature-name>
```

---

## Phase 7: Update Your Local Branch

After merging, sync your local `develop` branch:

```bash
git checkout develop
git pull origin develop
```

---

## Phase 8: Follow the Code of Conduct

* Maintain professional and respectful communication.
* Ask questions or clarify requirements early.
* Document assumptions, bugs, or design decisions in the code or project wiki.

---

Following these steps ensures **smooth collaboration, high-quality code, and successful project delivery**.


## Requirements Verification


| ID | Requirement Name                                                                                      | Met? | Screenshot                                                                                                                                                                    |
|----|-------------------------------------------------------------------------------------------------------|------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1  | All countries in the world organised by population (largest to smallest)                              | Yes  | ![](screenshots/ReportsbyPopulation/ReportsbyPopulation/AllCountriesintheworld.png)                                                                                           |
| 2  | All countries in a continent organised by population (largest to smallest)                            | Yes  | ![](screenshots/ReportsbyPopulation/ReportsbyPopulation/AllCountriesinContinent.png)                                                                                          |
| 3  | All countries in a region organised by population (largest to smallest)                               | Yes  | ![](screenshots/ReportsbyPopulation/ReportsbyPopulation/AllCountriesinRegion.png)                                                                                             |
| 4  | Top N populated countries in the world                                                                | Yes  | ![](screenshots/ReportsbyPopulation/ReportsbyPopulation/TopNPopulatedCountriesInTheWorld.png)                                                                                 |
| 5  | Top N populated countries in a continent                                                              | Yes  | ![](screenshots/ReportsbyPopulation/ReportsbyPopulation/TopNPopulatedCountriesByContinent.png)                                                                                |
| 6  | Top N populated countries in a region                                                                 | Yes  | ![](screenshots/ReportsbyPopulation/ReportsbyPopulation/TopNPopulatedCountriesByRegion.png)                                                                                   |
| 7  | All the cities in the world organised by largest population to smallest.                              | Yes  | ![](screenshots/FeatureCity_Report/FeatureCity_Report/All%20cities%20in%20the%20world%20by%20population.jpg)                                                                  |
| 8  | All the cities in a continent organised by largest population to smallest.                            | Yes  | ![](screenshots/FeatureCity_Report/FeatureCity_Report/All%20cities%20in%20a%20continent%20by%20population.jpg)                                                                |
| 9  | All the cities in a region organised by largest population to smallest.                               | Yes  | ![](screenshots/FeatureCity_Report/FeatureCity_Report/All%20cities%20in%20a%20region%20by%20population.jpg)                                                                   |
| 10 | All the cities in a country organised by largest population to smallest.                              | Yes  | ![](screenshots/FeatureCity_Report/FeatureCity_Report/All%20cities%20in%20a%20country%20by%20population.jpg)                                                                  |
| 11 | All the cities in a district organised by largest population to smallest.                             | Yes  | ![](screenshots/FeatureCity_Report/FeatureCity_Report/All%20cities%20in%20a%20district%20by%20population.jpg)                                                                 |
| 12 | Top N populated cities in the world                                                                   | Yes  | ![](screenshots/FeatureCity_Report/FeatureCity_Report/Top%20N%20populated%20cities%20in%20the%20world.jpg)                                                                    |
| 13 | Top N populated cities in a continent                                                                 | Yes  | ![](screenshots/FeatureCity_Report/FeatureCity_Report/Top%20N%20populated%20cities%20in%20a%20continent.jpg)                                                                  |
| 14 | Top N populated cities in a region                                                                    | Yes  | ![](screenshots/FeatureCity_Report/FeatureCity_Report/Top%20N%20populated%20cities%20in%20a%20region.jpg)                                                                     |
| 15 | Top N populated cities in a country                                                                   | Yes  | ![](screenshots/FeatureCity_Report/FeatureCity_Report/Top%20N%20populated%20cities%20in%20a%20country.jpg)                                                                    |
| 16 | Top N populated cities in a district                                                                  | Yes  | ![](screenshots/FeatureCity_Report/FeatureCity_Report/Top%20N%20populated%20cities%20in%20a%20district.jpg)                                                                   |
| 17 | All the capital cities in the world organised by largest population to smallest.                      | Yes  | ![](screenshots/city_report/city_report/Capital%20Cities%20in%20the%20world.png)                                                                                              |
| 18 | All the capital cities in a continent organised by largest population to smallest.                    | Yes  | ![](screenshots/city_report/city_report/Capital%20Cities%20in%20the%20Continent.png)                                                                                          |
| 19 | All the capital cities in a region organised by largest to smallest.                                  | Yes  | ![](screenshots/city_report/city_report/Capital%20Cities%20in%20the%20region.png)                                                                                             |
| 20 | The top N populated capital cities in the world where N is provided by the user.                      | Yes  | ![](screenshots/city_report/city_report/Top%20N%20Capital%20Cities%20in%20the%20world.png)                                                                                    |
| 21 | The top N populated capital cities in a continent where N is provided by the user.                    | Yes  | ![](screenshots/city_report/city_report/Top%20N%20Capital%20Cities%20in%20the%20Continent.png)                                                                                |
| 22 | The top N populated capital cities in a region where N is provided by the user.                       | Yes  | ![](screenshots/city_report/city_report/Top%20N%20Capital%20Cities%20in%20the%20Region.png)                                                                                   |
| 23 | The population of people, people living in cities, and people not living in cities in each continent. | Yes  | ![](screenshots/Population%20Report/Population%20Report/The%20population%20of%20people%20living%20in%20cities%20and%20not%20living%20in%20cities%20in%20each%20continent.png) |
| 24 | The population of people, people living in cities, and people not living in cities in each region.    | Yes  | ![](screenshots/Population%20Report/Population%20Report/The%20population%20of%20people%20living%20in%20cities%20and%20not%20living%20in%20cities%20in%20each%20country.png)   |
| 25 | The population of people, people living in cities, and people not living in cities in each country.   | Yes  | ![](screenshots/Population%20Report/Population%20Report/The%20population%20of%20people%20living%20in%20cities%20and%20not%20living%20in%20cities%20in%20each%20region.png)    |
| 26 | Population of The World                                                                               | Yes  | ![](screenshots/AdditionalReports/AdditionalReports/PopulationintheWorld.png)                                                                                                 |
| 27 | Population of A Continent                                                                             | Yes  | ![](screenshots/AdditionalReports/AdditionalReports/PopulationinaContinent.png)                                                                                               |
| 28 | Population of A Region                                                                                | Yes  | ![](screenshots/AdditionalReports/AdditionalReports/PopulationinaRegion.png)                                                                                                  |
| 29 | Population of A Country                                                                               | Yes  | ![](screenshots/AdditionalReports/AdditionalReports/PopulationinaCountry.png)                                                                                                 |
| 30 | Population of A District                                                                              | Yes  | ![](screenshots/AdditionalReports/AdditionalReports/PopulationinaDistrict.png)                                                                                                |
| 31 | Population of A City                                                                                  | Yes  | ![](screenshots/AdditionalReports/AdditionalReports/PopulationinaCity.png)                                                                                                    |
| 32 | Population By Spoken Language                                                                         | Yes  | ![](screenshots/AdditionalReports/AdditionalReports/PopulationBySpokenLanguages.png)                                                                                          |

## Code of Conduct

Please review our [Code of Conduct](CODE_OF_CONDUCT.md) to familiarize with the standards and expectations for not only team collaboration but also communication.
