# Github API User Repositories

This project is a Spring Boot application that consumes the Github API to retrieve a list of non-fork repositories for a
specific
user.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Setup](#setup)
- [Usage](#usage)
- [Tests](#tests)

## Features

- Fetches user repositories from Github API.
- Filters out forked repositories.
- Returns data in JSON format.
- Handles errors and exceptions gracefully with clear messages.

## Technologies

- Java 17
- Spring Boot
- Spring Web
- Gson

## Setup

Clone the project to your local machine and open it in your preferred Java IDE. The project uses Gradle for dependency
management.

```bash
git clone https://github.com/denisklebanovich/github-inspector.git
```

You can also set up github token in `application.properties` file to increase the rate limit of Github API.
You can either set it as an environment variable and then use it in the file:
```properties
github.api.token=${GITHUB_API_TOKEN}
```
or set it directly in the file:
```properties
github.api.token=your_token
```
To generate a token, follow the
instructions [here](https://docs.github.com/en/github/authenticating-to-github/keeping-your-account-and-data-secure/creating-a-personal-access-token).

## Usage

The main endpoint of this application is `/github/users/{username}/repos`.

Send a GET request to this endpoint with the username of the Github user you wish to get repositories for.

```bash
curl -X GET http://localhost:8080/github/users/{username}/repos
```

The response will be a JSON array containing the repositories of the user and their branches. For example:

```json
[
  {
    "name": "repo1",
    "owner": "user1",
    "branches": [
      {
        "name": "branch1",
        "lastCommitSha": "sha1"
      },
      {
        "name": "branch2",
        "lastCommitSha": "sha2"
      }
    ]
  }
]
```

If the error occurs, the response will be a JSON object with the error message. For example:

```json
{
  "status": "404",
  "message": "User not found"
}
```

## Tests

This application comes with unit tests written using Junit and Mockito. You can run them through your IDE or using
Gradle from the command line with the following command:

```bash
./gradlew test
```