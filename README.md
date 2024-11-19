# Welcome to keywordr
Keywordr is Java based application that utilizes two keyword sets to scrape and categorize job opportunities off of user defined Company entries.
The app integrates with Firestore for data storage and provides a foundation for building a ReactJS frontend.

### Project structure

```
keywordr
├── README.md                        # Placeholder for project documentation.
├── assembly.xml                     # Maven Assembly Plugin configuration.
├── bin                              # Contains executable scripts for different OS environments.
│   ├── keywordr.bat                 # Windows batch script to execute the app.
│   └── keywordr.sh                  # Bash script for Unix-based systems.
├── chromedriver                     # Folder for ChromeDriver dependency.
│   └── README.md                    # Documentation for ChromeDriver.
├── config                           # Configuration files and initial data for the app.
│   ├── Company.json                 # JSON file listing companies to analyze.
│   ├── JobKeywords.csv              # CSV file with job-related keywords.
│   ├── TechKeywords.csv             # CSV file with tech-related keywords.
│   └── keywordr_configuration.ini   # General app configuration settings.
├── pom.xml                          # Maven Project Object Model file.
└── src                              # Java source code and resources.
```

### config/Company.json
Company entry location, each company is added as a JSON object to a list of companies with following information:

- name
- location
- URL

Example `Company.json` with one company:

```json
{
  "companies": [
      {
        "name": "myCompany",
        "location": "Belgrade, Serbia",
        "URL": "https://myCompany/careers"
      }
  ]
}
```

Defined URL need to be a path to a company job listing wall page.
Defined URL will be used for data scraping, so it needs to contain company's job ads.
In most cases it is an URL endpoint ending with `/careers`, `/career` or it is separate site for hiring opportunities.

### config/JobKeywords.csv
User can tune in the algorithm by defining most used keywords for job ad detection.

Example `JobKeywords.csv` with 7 keywords:

```csv
Developer,Engineer,Send us your CV,DevOps,Data Scientist,Apply,Tech Lead
```

Example file contains some general keywords found in job ads like:

- 'Apply' or
- 'Send us your CV'

it also contains keywords associated with hiring role:

- 'Developer'
- 'Engineer'
- 'Data Scientist'
- 'Tech Lead'
- 'DevOps'

Keywords are case-insensitive.
Values are separated by comma.

If you are also interested in finding some director roles you can include `Director` into the list as well.

### config/TechKeywords.csv
Second keyword list is used to flag each found job with appropriate technologies/skills.

Example 'TechKeywords.csv' with 10 keywords:

```csv
Java,Scala,Kotlin,Spring Boot,Maven,Git,Docker,Kubernetes,AWS,GCP
```

Algorithm will flag each job with as many tech keywords as it finds in job description.
Useful for searching optimization e.g. finding `Java` jobs in large set of jobs found by keywordr.

Keywords are case-insensitive.
Values are separated by comma.

### keywordr_configuration.ini
General application configuration used to define settings like:

- Path to Company.json, JobKeywords.csv and TechKeywords.csv
- If to use Selenium for dynamic content loading for defined URLs
- Timeout for dynamic content to load if using Selenium
- Output file path
- Output mode
- Firestore settings
- Log level

### bin/keywordr.bat & bin/keywordr.sh
Windows command line and Linux shell executables used to run keywordr application.

### assembly.xml
Maven assembly plugin file, used to define build distribution zip file structure.

### How to run
1. Clone the project
2. Run Maven `clean` and `install` commands
3. In `target/` directory there will be assembled distribution zip file
4. Unzip the dist zip
5. According to your OS execute either `bin/keywordr.bat` ot `bin/keywordr.sh`
6. Check results in `result/` folder

Additionally navigate to `config/` and alter any of the configuration files as per your needs.