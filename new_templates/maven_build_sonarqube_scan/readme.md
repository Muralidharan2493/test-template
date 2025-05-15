# Maven Build and SonarQube Analysis Pipeline

This pipeline defines a build process for Maven projects, including SonarQube analysis for code quality. It consists of two main jobs: `MavenBuild` and `Sonar_prepare_analyze_and_publish`.

## Pre-Requisite

Onboard your application GitHub repository to SonarQube by following this steps - https://woolworths-agile.atlassian.net/wiki/spaces/CFT/pages/32612713396/SonarQube+Application+Onboarding
Create a Service Connection in Azure Devops to connect to SonarQube - https://woolworths-agile.atlassian.net/wiki/spaces/CFT/pages/32524140860/SonarQube+-+FAQ#How-to-create-service-connection-in-Azure-DevOps


## Parameters

The pipeline uses the following parameters, which can be configured to customize the build process:

### Maven Build Parameters

| Parameter | Description | Default | Required |
|---|---|---|---|
| `mavenPomFile` | Relative path from the repository root to the Maven POM file. | `pom.xml` | Yes |
| `options` | Specify any Maven command-line options. | `' '` | No |
| `goals` | Specify the Maven lifecycle goal. | `package` | Yes |
| `jdkDirectory` | Sets JAVA_HOME to the given path (when `javaHomeOption` is `path`). | `' '` | No (Conditional) |
| `javaHomeOption` | Sets Java Home by JDK version or path. Options: `JDKVersion`, `path`. | `JDKVersion` | No |
| `jdkVersionOption` | JDK version to use. Options: `default`, `1.11`, `1.10`, `1.9`, `1.8`, `1.7`, `1.6`. | `default` | No |
| `mavenDirectory` | Sets the Maven home path (when `mavenVersionOption` is `path`). | `' '` | No (Conditional) |
| `mavenVersionOption` | Sets Maven Home by Maven version or path. Options: `default`, `path`. | `default` | No |
| `mavenSetM2Home` |  (When `mavenVersionOption` is `path`). | `false` | No |
| `publishJUnitResults` | Whether to publish JUnit test results. | `true` | No |
| `testResultsFiles` | Files to include in JUnit test results publishing. | `**/surefire-reports/TEST-*.xml` | No (Conditional) |
| `build_artifact_Path` | Path to the build artifact. | `**/*.jar` | No |
| `PlneArtifact_Name` | Name of the artifact. | `drop` | No |

### SonarQube Analysis Parameters

| Parameter | Description | Default | Required |
|---|---|---|---|
| `project_key` | SonarQube project key. | `''` | Yes |
| `sonar_service_connection` | Name of the SonarQube service connection. | `''` | Yes |
| `sources` | Source directories to analyze. | `$(Build.SourcesDirectory)` | No |
| `java_binaries` | Path to the compiled Java binaries. | `$(Build.SourcesDirectory)/target/classes` | No |
| `exclusions` | Files and directories to exclude from analysis. | `**/target/**/*, **/target/**, **/src/test/**/*, **/src/test/**` | No |
| `jacoco_report_paths` | Path to JaCoCo coverage report. | `**/target/site/jacoco/jacoco.xml` | No |
| `junit_report_paths` | Path to JUnit report. | `$(Build.SourcesDirectory)/target/surefire-reports` | No |
| `tests_location` | Location of the tests. | `src/test` | No |
| `run_sonaqube_build_breaker` | Whether to run the SonarQube build breaker. | `true` | No |

## Jobs

### `MavenBuild`

This job builds the Maven project using the specified parameters.

1.  **Maven@4 Task:** Executes the Maven build with the provided goals, POM file, JDK, and Maven options.  Publishes JUnit results if enabled.
2.  **CopyFiles@2 Task:** Copies the necessary files to the artifact staging directory.
3.  **PublishPipelineArtifact@1 Task:** Publishes the Maven build artifacts to the pipeline.

### `Sonar_prepare_analyze_and_publish`

This job performs SonarQube analysis on the built project.

1.  **DownloadPipelineArtifact@2 Task:** Downloads the Maven build artifacts from the pipeline.
2.  **Bash@3 Task:** Runs bash scripts to verify java version and downloaded artifacts.
3.  **SonarQubePrepare@5 Task:** Prepares the SonarQube analysis by configuring the scanner with the project key, sources, and other properties. Includes paths to binaries, exclusions, JaCoCo reports, and JUnit reports.
4.  **SonarQubeAnalyze@5 Task:** Runs the SonarQube analysis.
5.  **SonarQubePublish@5 Task:** Publishes the SonarQube results.
6.  **sonar-buildbreaker@8 Task:** Checks the SonarQube Quality Gate and breaks the build if the quality gate fails (conditional on `run_sonaqube_build_breaker`).

## Usage

To use this pipeline, you need to:

1.  Define a SonarQube service connection in your Azure DevOps project.
2.  Configure the pipeline parameters, especially the required ones like `project_key` and `sonar_service_connection`.
3.  Commit the YAML file to your repository and create a pipeline in Azure DevOps.

This README provides a comprehensive overview of the pipeline and its parameters, allowing users to easily understand and customize the build and analysis process.  Remember to adjust the default values and paths to match your specific project structure and requirements.

**Add the template to your pipeline:**

   ```yaml
    stages:
    - stage: Build
    jobs:
    - template: new_templates/maven_build_sonarqube_scan/maven_template_v2.yml@Shared_Templates
        parameters:
        sonar_service_connection: 'your service connection to SonarQube Enterprise'
        project_key: 'Your SonarQube project name'




