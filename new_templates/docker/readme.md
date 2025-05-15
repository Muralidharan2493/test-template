# Docker Build and Push to JFrog Artifactory

This Azure Pipeline template is designed to build and push Docker images to JFrog Artifactory.

## Parameters

The template utilizes the following parameters to customize the deployment:

| Parameter              | Type    | Required | Default                        | Description                                                                 |
|------------------------|---------|----------|--------------------------------|-----------------------------------------------------------------------------|
| `dockerfilePath`       | string  | Yes      | `Dockerfile`                   | Path to the Dockerfile.                                                     |
| `imageName`            | string  | Yes      | `my-docker-image`              | Name of the Docker image.                                                   |
| `imageTag`             | string  | Yes      | `$(Build.BuildId)`             | Tag for the Docker image.                                                   |
| `jfrogConnection`      | string  | Yes      | `scm-reporting`                | Name of your JFrog Artifactory service connection.                          |
| `jfrogRegistry`        | string  | Yes      | `my-docker-registry.jfrog.io`  | Your JFrog Artifactory Docker registry URL.                                 |
| `jfrogRepo`            | string  | Yes      | `my-docker-repo`               | The target repository in Artifactory.                                       |
| `buildContext`         | string  | Yes      | `.`                            | Context for the Docker build.                                               |
| `additionalBuildArgs`  | string  | No       | `''`                           | Additional Docker build arguments.                                          |
| `additionalPushArgs`   | string  | No       | `''`                           | Additional Docker push arguments.                                           |

## Jobs

### BuildAndPushDockerImage

This job builds and pushes the Docker image to JFrog Artifactory.

#### Steps

1. **Build Docker Image**
    - Uses the `Docker@2` task to build the Docker image.

    ```yaml
    - task: Docker@2
      displayName: 'Build Docker Image'
      inputs:
        repository: '${{ parameters.jfrogRegistry }}/${{ parameters.jfrogRepo }}/${{ parameters.imageName }}'
        command: 'build'
        Dockerfile: ${{ parameters.dockerfilePath }}
        tags: ${{ parameters.imageTag }}
    ```

2. **Push Image to JFrog**
    - Uses the `ArtifactoryDocker@1` task to push the Docker image to JFrog Artifactory.

    ```yaml
    - task: ArtifactoryDocker@1
      displayName: 'Push Image to Jfrog'
      inputs:
        command: 'push'
        artifactoryService: 'scm-reporting'
        targetRepo: ${{ parameters.jfrogRepo }}
        imageName: '${{ parameters.jfrogRegistry }}/${{ parameters.jfrogRepo }}/${{ parameters.imageName }}:${{ parameters.imageTag }}'
        collectBuildInfo: true
        buildName: '$(Build.DefinitionName)'
        buildNumber: '$(Build.BuildNumber)'
        includeEnvVars: true
    ```

## Usage

To use this template, include it in your Azure Pipeline YAML file and provide the necessary parameters.

```yaml
resources:
  repositories:
    - repository: templates
      type: git
      name: Shared_Templates

extends:
  template: new_templates/docker/docker_build.yml@templates
  parameters:
    dockerfilePath: 'path/to/Dockerfile'
    imageName: 'your-image-name'
    imageTag: 'your-image-tag'
    jfrogConnection: 'your-jfrog-connection'
    jfrogRegistry: 'your-jfrog-registry'
    jfrogRepo: 'your-jfrog-repo'
    buildContext: 'your-build-context'
    additionalBuildArgs: '--build-arg key=value'
    additionalPushArgs: '--push-arg key=value'