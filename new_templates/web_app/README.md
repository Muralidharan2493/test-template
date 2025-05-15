# Web App Build and Deployment Templates

This repository contains templates for building and deploying a Python web application to Azure as a Linux Web App.

## Build Template

The build template (`web_app_build.yml`) is used to build the Python project. It performs the following steps:

1. **Use Python Version**: Sets up the specified Python version.
2. **Install Requirements**: Creates a virtual environment, upgrades `pip`, and installs the required packages from `requirements.txt`.
3. **Archive Files**: Archives the project files into a ZIP file.
4. **Upload Package**: Uploads the archived package as a build artifact.

### Parameters

- `vmImageName`: The VM image to use (default: `ubuntu-latest`).
- `projectRoot`: The root directory of the project (default: `$(System.DefaultWorkingDirectory)`).
- `pythonVersion`: The Python version to use (default: `3.11`).
- `Artifact_Name`: The name of the artifact or package
## Deployment Template

The deployment template (`web_app_deploy.yml`) is used to deploy the built package to Azure. It performs the following steps:

1. **Use Python Version**: Sets up the specified Python version.
2. **Deploy Azure Web App**: Deploys the archived package to the specified Azure Web App.

### Parameters

- `azureServiceConnectionId`: The Azure service connection ID. #Mandatory 
- `webAppName`: The name of the Azure Web App. #mandatory
- `vmImageName`: The VM image to use (default: `ubuntu-latest`). #optional
- `environmentName`: The name of the environment. #mandatory
- `pythonVersion`: The Python version to use (default: `3.11`). #optional
- `dependent_on`: The stage/job that this deployment depends on. #mandatory
- `deployToSlotOrASE`: Whether to deploy to a slot or App Service Environment (default: `false`). #optional
- `slotName`: The name of the deployment slot. #optional
- `resourceGroupName`: The name of the resource group. #optional
## Usage

To use these templates, include them in your Azure Pipelines configuration and provide the necessary parameters.

### Example

```yaml
resources:
  repositories:
  - repository: Shared_Templates
    type: github
    endpoint: WoolworthsCORP
    ref: refs/heads/feature/webapp_template_CFT-21907
    name: WoolworthsCORP/Shared_Templates


- template: new_templates/web_app/web_app_build.yml@Shared_Templates
  parameters:
    vmImageName: 'ubuntu-latest'
    projectRoot: '$(System.DefaultWorkingDirectory)'
    pythonVersion: '3.11'


- template: new_templates/web_app/web_app_deploy.yml@Shared_Templates
  parameters:
    azureServiceConnectionId: '<your-azure-service-connection-id>'
    webAppName: '<your-web-app-name>'
    environmentName: 'production'
    pythonVersion: '3.11'
```
Replace the placeholders with your actual values.

### Full Pipeline Example

```yaml
resources:                          # To pull the shared templates to be available for current pipeline.
    repositories:
    - repository: Shared_Templates
        type: github
        endpoint: WoolworthsCORP
        ref: refs/heads/feature/webapp_template_CFT-21907
        name: WoolworthsCORP/Shared_Templates

trigger:
- main
pool:
    vmImage: 'ubuntu-latest'
variables:
    azureServiceConnectionId: 'b2e01409-9f17-48be-b8ce-ccb5c5dd4574' # Azure Service Connection
    webAppName: 'mywebtestapp2' # Web App name
    vmImageName: 'ubuntu-latest' # VM Image name
    projectRoot: $(System.DefaultWorkingDirectory) # Project root
    pythonVersion: '3.11' # Python version
    dev_environment: 'mywebtest' # Environment name
    test_environment: 'test_environment'
    Artifact_Name: Demo_Package
stages: 
- template: 'new_templates/web_app/web_app_build.yml@Shared_Templates'
    parameters:
        vmImageName: $(vmImageName)
        projectRoot: $(projectRoot)
        pythonVersion: $(pythonVersion)
        
- template: new_templates/web_app/web_app_deploy.yml@Shared_Templates
    parameters:
        azureServiceConnectionId: $(azureServiceConnectionId)
        webAppName: $(webAppName)
        vmImageName: $(vmImageName)
        environmentName: mywebtest
        pythonVersion: $(pythonVersion)

- template: new_templates/web_app/web_app_deploy.yml@Shared_Templates
    parameters:
        azureServiceConnectionId: $(azureServiceConnectionId)
        webAppName: $(test_environment)
        vmImageName: $(vmImageName)
        environmentName: test_environment
        pythonVersion: $(pythonVersion)
```

## Contributing

Contributions are welcome! Please submit a pull request or open an issue to discuss any changes.