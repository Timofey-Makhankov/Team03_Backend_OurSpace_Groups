# README

## Starter Project Spring Boot

### Clone the Project

```
git clone https://github.com/tseLha07/Team03_Backend_OurSpace_Groups.git
```

### Open Project in Intellij
![Open Button on Top of the Intellij GUI](./Images/Screenshot%202023-09-07%20110800.png)

### Run the Project with BootRun

On the Top Right press the Gradle Tab (Elephant Icon) and under Tasks/application press "bootRun"

![Press bootRun Button](./Images/Screenshot%202023-09-07%20111017.png)

### Docker command
```
docker run --name postgres_db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres
```

### Troubleshooting

If it tells you, that port 8080 is in use. It might help to disable Mobile Hotspot on host device

```
org.postgresql.util.PSQLException: ERROR: relation "role_authority" does not exist
```
Simply restart the application. Hibernate sometimes does not initialize the tables fast enough an causes thios error. restarting the application fixes this.