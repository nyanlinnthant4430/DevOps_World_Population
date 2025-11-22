**Population Reporting System** 

Project Owner Goal:
To deliver accurate and flexible population data for countries, cities, and regions so that analysts, planners, and researchers can make informed, data-driven decisions.

Issue 1: View Countries by Population

User Story: I want to view countries by population to understand distribution globally, by continent, or by region.

Priority: ⭐️⭐️⭐️⭐️⭐️ (Highest)

Reason: Foundational feature — most other reports depend on this logic.

Story Points: 8 (medium–high effort: SQL + filtering logic + display formatting)

Issue 2: View Cities by Population

User Story: I want to view cities by population to support urban planning at various levels.

Priority: ⭐️⭐️⭐️ (Medium–High)

Reason: Similar logic to countries but more data and filters.

Story Points: 5 (moderate complexity, uses existing query structure)

Issue 3: Compare Capital Cities by Population

User Story: I want to compare capital cities by population across different regions.

Priority: ⭐️⭐️ (Medium)

Reason: Depends on city and country data being correctly linked.

Story Points: 3 (lower complexity; limited dataset)

Issue 4: Show Total, Urban, and Rural Populations

User Story: I want to see total, urban, and rural population by continent, region, and country to plan resource allocation.

Priority: ⭐️⭐️⭐️⭐️ (High)

Reason: Critical for analytics — involves calculations and aggregation.

Story Points: 8 (data aggregation + percentage calculations)

Issue 5: Retrieve Total Population of Any Area

User Story: I want to retrieve the total population of any geographic area for quick reference.

Priority: ⭐️ (Low–Medium)

Reason: Simple query feature once others are done.

Story Points: 2 (straightforward SQL SELECT queries)