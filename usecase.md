**Use Case Diagram — Population Reporting System**

**Overview**

The Population Reporting System is designed to provide users with detailed reports about countries, cities, capitals, populations, and languages around the world. The system connects to a central world database, allowing different user roles such as analysts, planners, officials, researchers, linguists, and the project owner to access accurate and up-to-date information. This ensures better decision-making, planning, and research analysis.

**Actors**

The Data Analyst is responsible for examining and comparing population data across countries to understand demographic trends.
The City Planner focuses on city-level data, using it to support urban development and resource management.
The Government Official views national and capital city reports to guide policy-making and national planning.
The Population Researcher analyzes statistical data on population growth, density, and distribution.
The Linguist studies languages spoken in different countries and their population percentages.
Finally, the Project Owner manages and oversees the overall system, ensuring all reports are accurate and functioning properly.

**Use Cases**

The View Country Reports use case allows users to see all countries in the world organized by population size and region.
The View City Reports use case provides information about major cities and their respective populations.
The View Capital City Reports use case focuses on data specific to capital cities, allowing users to compare them effectively.
The View Population Reports use case displays total, male, and female population data for countries or regions.
The View Language Reports use case presents the distribution of languages and the percentage of speakers in different areas.
Finally, the Access World Database use case connects the system to the global dataset, which supplies data for all the other reports.

**Relationships**

Each report use case (from UC1 to UC5) uses the Access World Database (UC6) to retrieve the necessary information. The Project Owner has access to all major reports, while each of the other actors interacts only with the use cases relevant to their specific roles. This structure ensures data access control, efficiency, and role-based functionality within the system.