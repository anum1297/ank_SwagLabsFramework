
# 🧪 Swag Labs Automation Framework

Automated Test Framework built using **Selenium WebDriver**, **TestNG**, **Extent Reports**, and **Maven** to validate the UI workflows of [Swag Labs](https://www.saucedemo.com/).

---

## 📌 Table of Contents

- [📖 Overview](#overview)
- [🧰 Tech Stack](#tech-stack)
- [🗂 Project Structure](#project-structure)
- [🚀 Setup Instructions](#setup-instructions)
- [▶️ How to Run Tests](#how-to-run-tests)
- [📊 Reporting](#reporting)
- [🔁 CI/CD Pipeline](#cicd-pipeline)
- [📧 Email Report Integration](email-report-integration)
- [📫 Contribution](#contribution)
- [🔐 Conclusion](#conclusion)

---

## 📖 Overview

This automation suite is designed to:

- Perform **end-to-end testing** of Swag Labs (login, add to cart, checkout, etc.)
- Generate clean and detailed HTML test reports
- Support **parameterized execution** via TestNG and Jenkins
- Easily scalable for different environments and browsers

<img width="1919" height="892" alt="Screenshot 2025-07-26 195701" src="https://github.com/user-attachments/assets/3795ecf8-f27f-4383-9154-ae0872f077b4" />
<img width="1893" height="694" alt="Screenshot 2025-07-26 195809" src="https://github.com/user-attachments/assets/e43a10b6-48a1-4fff-b79c-61439d509937" />
<img width="1895" height="533" alt="Screenshot 2025-07-26 195748" src="https://github.com/user-attachments/assets/7a86ba5b-2edc-402d-9bd6-cb601761d2ec" />


- Walkthrough Video

<video controls src="Recording 2025-07-26 192306.mp4" title="Title"></video>

---

## 🧰 Tech Stack

| Tool            | Purpose                         |
|-----------------|----------------------------------|
| Java            | Programming language             |
| Selenium WebDriver | Browser automation           |
| TestNG          | Testing framework                |
| Maven           | Build and dependency management  |
| Extent Reports  | HTML reporting                   |
| Jenkins         | CI/CD pipeline                   |
| GitHub          | Version control                  |

---

## 🗂 Project Structure

```
SwagLabsAutomation/
├── src/
│   ├── main/
│   │   └── java/com.swaglabs.pages/    # Page Object Model classes
│   └── test/
│       └── java/com.swaglabs.tests/    # TestNG test cases
├── testng.xml                           # Test suite configuration
├── pom.xml                              # Maven build file
├── reports/                             # Extent reports output
└── README.md                            # Project documentation
```
<img width="502" height="529" alt="Screenshot 2025-07-26 193755" src="https://github.com/user-attachments/assets/cf251e95-9ecd-431d-9677-57a16c8101d6" />
<img width="499" height="440" alt="Screenshot 2025-07-26 194303" src="https://github.com/user-attachments/assets/2bd11d91-f73f-4936-a1e4-3ecae0433ad3" />

---

## 🚀 Setup Instructions

### 🖥 Prerequisites

- Java JDK 8 or above
- Maven 3.x
- Eclipse or IntelliJ
- Git
- Chrome or Firefox browser

### 🔽 Clone the repository

```bash
git clone https://github.com/anum1297/ank_SwagLabsAutomation.git
cd ank_SwagLabsAutomation
```

### 📦 Install dependencies

```bash
mvn clean install
```

---

## ▶️ How to Run Tests

### 🧪 Using Maven

```bash
mvn test
```

### 🧪 With Parameters (e.g., from Jenkins)

```bash
mvn test -Dbrowser=chrome -Denv=qa
```

### 🧪 From Eclipse

1. Right-click `testng.xml`
2. Run As → TestNG Suite

---

## 📊 Reporting

- HTML report generated using **ExtentReports**
- After execution, the timestamped report is saved to:

```
reports/TestReport_<timestamp>.html
```

Open it in a browser to view test results, screenshots, and logs. The report is written before email delivery is attempted.

Email delivery is enabled by default. For Gmail, enable 2-Step Verification on the sender account and create a Google **App Password**. Use that app password—not your normal Gmail password.

On Linux or macOS, set the sender, recipient, and app password in the same terminal before running:

```bash
export SWAGLABS_MAIL_FROM="sender@gmail.com"
export SWAGLABS_MAIL_TO="recipient@example.com"
export SWAGLABS_MAIL_APP_PASSWORD="your-gmail-app-password"
mvn test
```

On Windows PowerShell:

```powershell
$env:SWAGLABS_MAIL_FROM = "sender@gmail.com"
$env:SWAGLABS_MAIL_TO = "recipient@example.com"
$env:SWAGLABS_MAIL_APP_PASSWORD = "your-gmail-app-password"
mvn test
```

Keep these values out of source control. To generate the report without sending email, run:

```bash
mvn -DsendReportEmail=false test
```

<img width="1919" height="876" alt="Screenshot 2025-07-26 200246" src="https://github.com/user-attachments/assets/0277ecbf-9b49-461d-9597-8577772cc317" />
<img width="1920" height="847" alt="image" src="https://github.com/user-attachments/assets/951e56b3-267f-47ff-b180-c0f5463e8fa9" />
<img width="1920" height="847" alt="image" src="https://github.com/user-attachments/assets/8d3c2fdd-a79e-49f0-a1eb-456c7339c0dd" />
<img width="1917" height="881" alt="Screenshot 2025-07-26 200356" src="https://github.com/user-attachments/assets/8900c7f7-2f2a-4379-bdc3-a03def67d37a" />
<img width="1917" height="875" alt="Screenshot 2025-07-26 200406" src="https://github.com/user-attachments/assets/e7b15a06-4bbf-4303-b65d-f534e6592f58" />

---

## 🔁 CI/CD Pipeline

### ✅ Jenkins Integration

- The repository [Jenkinsfile](./Jenkinsfile) runs Chrome in headed mode, emails the report, archives that build's timestamped report, and publishes the Surefire test results.
- Configure the Jenkins agent with **JDK 17**, **Maven**, **Google Chrome**, and outbound network access for Maven/driver downloads and Gmail SMTP (port 587).
- Headed Chrome requires Jenkins to have access to a graphical desktop. On a Linux Mint machine running Jenkins as the `jenkins` service user, keep the desktop session logged in and allow that user to connect to the X display. For the current desktop user `aniket` and display `:0`, run this from a terminal inside the logged-in desktop session:

  ```bash
  xhost +SI:localuser:jenkins
  sudo systemctl edit jenkins
  ```

  Add the following systemd override, using the desktop account's actual home path for `XAUTHORITY`:

  ```ini
  [Service]
  Environment="DISPLAY=:0"
  Environment="XAUTHORITY=/home/aniket/.Xauthority"
  ```

  Save and exit, then restart Jenkins:

  ```bash
  sudo systemctl restart jenkins
  ```

  The Jenkins agent running the job must be this same machine/display. If Jenkins starts without access to the desktop, Chrome cannot open a visible window; use a graphical Jenkins agent or switch the pipeline back to `chrome-headless`.
- Ensure Jenkins has the **Pipeline** and **Credentials Binding** plugins.
- In **Manage Jenkins → Credentials**, add:
  - A **Username with password** credential with ID `swaglabs-gmail-smtp`. Set the username to the Gmail sender address and the password to that account's Google App Password.
  - A **Secret text** credential with ID `swaglabs-report-recipient`. Set the secret to the destination email address.
- Create a **Pipeline** job configured as **Pipeline script from SCM**, point it to this repository, and set the script path to `Jenkinsfile`. Run the job with **SEND_REPORT_EMAIL** enabled.
- To run Jenkins without email, uncheck **SEND_REPORT_EMAIL**; the report and test results will still be archived.

---

## 📧 Email Report Integration

The TestNG listener sends a summary of passed, failed, and skipped tests and attaches the generated Extent HTML report after the suite completes. The sender and app password are read from environment variables locally or bound from Jenkins credentials in CI.

Open in your email to view test results with status:

<img width="1897" height="838" alt="image" src="https://github.com/user-attachments/assets/f19e42ca-ee75-4402-9d41-665d7289dc1d" />

---

## 📫 Contribution

Contributions are welcome!  
Please follow these steps:

1. Fork the repository
2. Create a feature branch:

```
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. Commit and push changes
4. Open a Pull Request

---

## ✅ Conclusion

This Swag Labs Automation Framework provides a scalable, maintainable, and extendable solution for automated testing of web applications. Leveraging modern tools like Selenium WebDriver, TestNG, and Maven—combined with rich reporting and CI/CD integration via Jenkins—it ensures robust test coverage and faster feedback cycles.

Whether you're running local smoke tests or orchestrating builds on a CI server, this framework adapts seamlessly to meet your testing and deployment needs.

We encourage developers and QA engineers to contribute, suggest improvements, and extend the framework for broader test coverage or new technologies. Together, we can keep raising the quality bar.

> 🎯 *Test Fast. Fail Fast. Fix Fast.*

---

Thank you for checking out this project! ⭐️
