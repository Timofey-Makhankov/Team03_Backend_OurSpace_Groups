# üK 233 Backend & Database

This is the backend Repo of the Project. We are using Springboot and Java as our primary Language. The Database is a docker image with the default configurations. The Testing will be done with postman, a HTTP Request Client.

## Prerequisites

You should have these software installed:

- [Docker](https://docs.docker.com/engine/install/)
- an IDE, in this project we use [Intellij](https://www.jetbrains.com/help/idea/installation-guide.html)

### Docker command
```
docker run --name postgres_db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres
```

The Docker Container should start automatically. You can verify with:

```
docker ps
```

There should be able to see with the name "postgres_db"

### Clone the Project

```
git clone https://github.com/tseLha07/Team03_Backend_OurSpace_Groups.git
```

### Open Project in Intellij
![Open Button on Top of the Intellij GUI](./Images/Screenshot%202023-09-07%20110800.png)

### Run the Project with BootRun

On the Top Right press the Gradle Tab (Elephant Icon) and under Tasks/application press "bootRun"

![Press bootRun Button](./Images/Screenshot%202023-09-07%20111017.png)

### Troubleshooting

If it tells you, that port 8080 is in use. It might help to disable Mobile Hotspot on host device

```
org.postgresql.util.PSQLException: ERROR: relation "role_authority" does not exist
```
Simply restart the application. Hibernate sometimes does not initialize the tables fast enough an causes this error. restarting the application fixes this.

### Testing - Postman

You can find the collection and the environment in the repo under the "pm" folder. import both json files and after running springboot, you can run the "Testing Suit Folder"

