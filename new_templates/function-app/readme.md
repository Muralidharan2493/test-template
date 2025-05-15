# Azure Function App Deployment Pipeline

This Azure Pipelines YAML template facilitates the deployment of Azure Function Apps using a zip package. It supports deploying to production or deployment slots, and allows for the configuration of application settings, configuration settings, and slot settings.

## Parameters

The template utilizes the following parameters to customize the deployment:

| Parameter                  | Type    | Required | Default | Description                                                                 |
|----------------------------|---------|----------|---------|-----------------------------------------------------------------------------|
| `serviceConnectionName`    | string  | Yes      |         | The name of the Azure Resource Manager service connection used to authenticate with Azure. |
| `resourceGroupName`        | string  | Yes      |         | The name of the Azure Resource Group where the Function App is located.      |
| `functionAppName`          | string  | Yes      |         | The name of the Azure Function App to deploy.                                |
| `packagePath`              | string  | Yes      |         | The path to the Function App zip package within the build artifacts.         |
| `deploymentSlot`           | string  | No       | ""      | The name of the deployment slot to deploy to. Leave empty to deploy directly to production. |
| `appSettings`              | object  | No       | {}      | A JSON object containing application settings to be applied to the Function App. |
| `configurationSettings`    | object  | No       | {}      | A JSON object containing configuration settings to be applied to the Function App. |
| `slotSettings`             | object  | No       | {}      | A JSON object containing slot-specific settings to be applied to the deployment slot. |
| `deployToSlot`             | boolean | No       | false   | Indicates whether to deploy to a deployment slot. Set to `true` to deploy to the slot specified in `deploymentSlot`. |
| `swapSlots`                | boolean | No       | false   | Indicates whether to swap the deployment slot with the production slot after a successful deployment. This parameter only applies if `deployToSlot` is set to `true`. |
| `enableAppSettings`        | boolean | No       | true    | Indicates if the `appSettings` parameter should be applied.                  |
| `enableConfigurationSettings` | boolean | No    | true    | Indicates if the `configurationSettings` parameter should be applied.        |
| `enableSlotSettings`       | boolean | No       | true    | Indicates if the `slotSettings` parameter should be applied.                 |

## Jobs

The pipeline consists of a single job:

### DeployFunctionApp

This job deploys the Azure Function App.

#### Steps

1. **Download the Function App zip package**
    - Downloads the Function App zip package from the pipeline artifacts.

2. **List the contents of the download directory**
    - Lists the contents of the download directory.

3. **Deploy the Function App**
    - Uses the `AzureFunctionApp@1` task to deploy the Function App, configuring application settings, configuration settings, and slot settings as specified.

4. **Swap the deployment slot with the production slot (optional)**
    - If `deployToSlot` and `swapSlots` are both set to `true`, it swaps the deployment slot with the production slot using the `AzureAppServiceManage@0` task.

## Usage

To use this template in your Azure Pipelines, follow these steps:

1. **Create a YAML pipeline:** In your Azure DevOps project, create a new YAML pipeline.
2. **Copy the template:** Copy the contents of this `azure-pipelines-functionapp-deploy.yml` file into your pipeline's YAML file.
3. **Configure parameters:** Customize the parameters in the `parameters` section of your pipeline YAML file to match your deployment requirements.
4. **Add build artifact:** Ensure that your build pipeline produces a zip artifact named 'functionapp' that contains the function app code.
5. **Run the pipeline:** Run the pipeline to deploy your Function App.

## Example Usage in Azure-pipelines.yml

```yaml
trigger:
  - main

pool:
  vmImage: 'ubuntu-latest'

variables:
  resourceGroupName: 'my-resource-group'
  functionAppName: 'my-function-app'
  deploymentSlot: 'staging'
  packagePath: '$(Build.SourcesDirectory)/functionapp.zip'

stages:
  - stage: Deploy
    jobs:
      - template: azure-pipelines-functionapp-deploy.yml
        parameters:
          serviceConnectionName: 'MyAzureServiceConnection'
          resourceGroupName: '$(resourceGroupName)'
          functionAppName: '$(functionAppName)'
          packagePath: '$(packagePath)'
          deploymentSlot: '$(deploymentSlot)'
          deployToSlot: true
          swapSlots: true
          appSettings:
            WEBSITE_RUN_FROM_PACKAGE: '1'
          configurationSettings:
            key1: 'value1'
          slotSettings:
            slotkey: 'slotvalue'