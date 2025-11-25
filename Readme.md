# Population Reporting System

A Java-driven DevOps project designed to generate world population reports through SQL queries and object-oriented programming. The system fetches and presents population data for countries, cities, regions, and languages, organized and ranked by population. It also showcases professional software engineering practices, including Scrum methodology, Git branching strategies, and continuous integration.

## Requirements Implementation Status

32 requirements of 32 have been implemented, which is **100%** completed.

> This shows the latest progress of the World Population Report System according to the official project requirements.
---

* **Master Build Status** ![Master Build Status](https://img.shields.io/github/actions/workflow/status/nyanlinnthant4430/DevOps_World_Population/main.yml?branch=master)
* **Develop Build Status** ![Develop Build Status](https://img.shields.io/github/actions/workflow/status/nyanlinnthant4430/DevOps_World_Population/main.yml?branch=develop)
* **License** ![GitHub license](https://img.shields.io/github/license/nyanlinnthant4430/DevOps_World_Population)
* **Release** [![Releases](https://img.shields.io/github/release/nyanlinnthant4430/DevOps_World_Population/all.svg?style=flat-square)](https://github.com/nyanlinnthant4430/DevOps_World_Population/releases)
* [![codecov](https://codecov.io/gh/nyanlinnthant4430/DevOps_World_Population/graph/badge.svg)](https://codecov.io/gh/nyanlinnthant4430/DevOps_World_Population)

##  Role of Team Members & Responsibilities

| Student ID   | Name            | Role                     | Responsibility                                                                               |
|--------------|-----------------|--------------------------|----------------------------------------------------------------------------------------------|
| **40804626** | Min Pyae Soe    | Scrum Master / Developer | Backlog and Sprint/ Git Management/ Merge Branches                                           |
| **40804627** | Nyan Linn Thant | Product Owner/ Developer | Overall Management/ Backlog and Sprint / Git Management/ Plant UML / Use Case/ Documentation |
| **40804628** | Aung Win Tun    | Developer / Tester       | Country Report / Population Report / Testing                                                 |
| **40804619** | Nyein Su Nandar | Developer / Tester       | Capital City Report / Testing                                                                |
| **40894620** | Ye Htet Aung    | Developer / Tester       | City Report / Testing                                                                        |
| **40804633** | Aung Myat       | Developer / Tester       | Policy Report / Testing                                                                      |


### Branching Strategy
- **master** → Production-ready, stable code
- **develop** → Integration branch for all new features
- **release** → Final testing before merging into `master`
- **feature/** → Individual features developed by team members


### Packages
- **com.napier.devops** -> Main Application Classes
- **com.napier.devops.basicpopulation** -> Population Reports
- **com.napier.devops.capital_city_report** -> Capital City Reports
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

###  Pre Setup Requirements
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
To ensure high code quality, consistency, and effective teamwork, please follow the guidelines instructions below.

---

## Phase 1: Set Up Your Local Environment

1. Ensure you have the following installed:

    * **Java JDK 19**
    * **MySQL Server**
    * **Apache Maven**
    * **IntelliJ IDEA** (or any Java IDE)
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

## Phase 5: Submit a Pull Request

1. Go to GitHub and open a **Pull Request from your feature branch into the `develop`** branch.
2. Provide a **clear title and detailed description**:

    * The feature or fix you added
    * Any linked issues or tasks
    * Instructions for testing the update
3. Ask **at least one team member** to review your PR.

---

## Phase 6: Review and Merge

1. Make any necessary updates based on **review feedback**.
2. After approval, **merge your pull request into `develop`** branch.
3. Remove your feature branch afterward to keep the repository tidy.

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

* Communicate respectfully and professionally at all times.
* Raise questions or request clarification as early as possible to avoid misunderstandings.
* Record any assumptions, issues, or design decisions in the codebase or project documentation.

---

## Code of Conduct

Please review our [Code of Conduct](Code_of_Conduct.md) to familiarize with the standards and expectations for not only team collaboration but also communication.
