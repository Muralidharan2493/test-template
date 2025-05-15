# Azure Key Vault Template

This Azure DevOps template retrieves all secrets from a specified Azure Key Vault and makes them available as environment variables in your pipeline.

## Pre-Requisite

Make sure you are using Self hosted agent which can connect and retrieve the secrets

## Parameters

| Parameter | Type | Description | Default |
|---|---|---|---|
| `keyVaultName` | string | **Required:** The name of the Azure Key Vault containing the secrets. | '' |
| `serviceConnection` | string | **Required:** The name of the Azure service connection that has access to the Key Vault. | '' |
| `runAsPreJob` | boolean | **Optional:** Specifies whether to run this task as a pre-job. | `false` |
| `secretsFilter` | string | **Optional:** Specifies which secret to retrieve. | `*` |

## Usage

1. **Create a new YAML pipeline or edit an existing one.**

2. **Add the template to your pipeline:**

   ```yaml
    stages:
    - stage: Retrieve
      displayName: 'Retrieve key vault secrets'
      jobs:
        - template: new_templates/keyvault/azure_keyvault.yml@Shared_Templates
          parameters:
            keyVaultName: 'your key vault name'
            serviceConnection: 'your service connection to azure key vault'
            secretsFilter: 'secret1,secret2'
