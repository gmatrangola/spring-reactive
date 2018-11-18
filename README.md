# Spring Reactive Lab Starter

This project contains the setup for the Spring Reactive Labs. It is divided into two submodules, indicator and global. Indicator is used for the first labs and global is used in the final lab. Simply clone this repo and import into your Editor. I'll be using IntelliJ in the demos.

Each module contains a `build.gradle` file that decleares all the dependencies required to build the labs.

This repo also contains a copy of the world-wide technology indicators from the World Bank used under the educational Copyright Clause. See: https://datacatalog.worldbank.org/search?sort_by=field_wbddh_modified_date&sort_order=DESC&f%5B0%5D=field_license_wbddh%3A1335 

## Setup

Please have the following setup and running on your development environment before class so that we can jump right into the labs.

1. Java 8

2. IntelliJ or Eclipse (I will demo in IntelliJ)

3. Docker: <https://docs.docker.com/install/> for whatever platform you are running. Stable build.

4. Docker Cassandra image

   ```bash
   > docker pull cassandra:latest
   
   > docker run --name spring-cassandra -p 7000-7001:7000-7001 -p 7199:7199 -p 9042:9042 -p 9160:9160 -d cassandra:latest
   ```


## Lab 1 - Web on Reactive

- Create a data model for the Indicator
  - Use Lombok Annotations to reduce boilerplate Java Code
- Create 2 RequestMappings 
  - Use `Mono<T>` Return the Patent Application Indicator for BGR, IP.PAT.RESD
  - Use `Flux<T>` Return the Patent Application Indicator and High Tech exports indicator (IP.PAT.RESD and TX.VAL.TECH.CD)
- Use Browser or http client test

Helper: WebFlux Documentation

- <https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-controller> 
- <https://grokonez.com/reactive-programming/reactor/reactor-create-flux-and-mono-simple-ways-to-create-publishers-reactive-programming> 

## Lab 2 NOSQL Cassandra Repository

- Use provided docker running Cassandra
- Load the reference data from csv into Cassandra
- Add Cassandra Annotations to the Indicator Class
  - Use Cassandra Annotations for @PrimaryKeyColumn and @Indexed
  - Add Index for country and indicatorCode
- Create Cassandra Repository for the indicator
  - Add the findAllByCountryCode 
  - Add find for Index ID
- Copy CassandraConfig.java and add fields to application.properties
- Add @Autowired reference to the Repository in the Controller
- Update WorldController
  - Add Mapping to find all Indicators and return a Flux<Indicator>
  - Add Mapping that gets by country code
  - Add Mapping for Index ID
  - Add Mapping to get on Mono for Index and Country code
- Create a http or run in browser all new GetMappings

### CassandraConfig.java

```java
@Configuration
@EnableReactiveCassandraRepositories(basePackages = "com.matrangola.reactive.data.repository")
public class CassandraConfig extends AbstractCassandraConfiguration {

    public static final String KEYSPACE = "world";

    @Override
    protected String getKeyspaceName() {
        return KEYSPACE;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    /*
     * Automatically creates a Keyspace if it doesn't exist
     */
    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        CreateKeyspaceSpecification specification = CreateKeyspaceSpecification
                .createKeyspace(KEYSPACE).ifNotExists()
                .with(KeyspaceOption.DURABLE_WRITES, true).withSimpleReplication();
        return Arrays.asList(specification);
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[] { "com.matrangola.reactive.data.model" };
    }
}
```

### application.properties

```properties
#
# Cassandra Config
#
spring.data.cassandra.keyspace-name=world
spring.data.cassandra.contact-points=localhost
spring.data.cassandra.prot=9042
```

### Docker

```bash
> docker run --name spring-cassandra -p 7000-7001:7000-7001 -p 7199:7199 -p 9042:9042 -p 9160:9160 -d cassandra:latest
> docker start spring-cassandra

# Start Web Service NOW!

> docker cp data/scitech.csv spring-cassandra:/tmp/
> docker exec -ti COPY world.indicator (countryname,countrycode,indicatorname,indicatorcode,year1970,year1980,year1990,year2000,year2010,year2016,year2017) FROM 'tmp/scitech.csv' WITH HEADER = TRUE
> docker exec -ti spring-cassandra cqlsh localhost
cqlsh> select * from world.indicator where countrycode = 'BGR';

 indicatorcode     | countrycode | countryname | indicatorname                                                             | year1970 | year1980 | year1990 | year2000   | year2010   | year2017   | year2018
-------------------+-------------+-------------+---------------------------------------------------------------------------+----------+----------+----------+------------+------------+------------+------------
    BX.GSR.ROYL.CD |         BGR |    Bulgaria | Charges for the use of intellectual property, receipts (BoP, current US$) |     null |     null |     null | 3.5222e+06 |  1.832e+07 |  5.043e+07 |   4.92e+07
 SP.POP.SCIE.RD.P6 |         BGR |    Bulgaria |                                   Researchers in R&D (per million people) |     null |     null |     null | 1184.79947 | 1482.18709 |       null |       null
 SP.POP.TECH.RD.P6 |         BGR |    Bulgaria |                                   Technicians in R&D (per million people) |     null |     null |     null |  479.09446 |  500.04745 |       null |       null
       IP.TMK.TOTL |         BGR |    Bulgaria |                                             Trademark applications, total |      576 |      495 |     4830 |       8982 |       6921 |       5951 |       null
 GB.XPD.RSDV.GD.ZS |         BGR |    Bulgaria |                           Research and development expenditure (% of GDP) |     null |     null |     null |    0.49885 |    0.56387 |       null |       null
       IP.PAT.RESD |         BGR |    Bulgaria |                                            Patent applications, residents |     1908 |     3297 |      198 |        231 |        243 |        230 |       null
 TX.VAL.TECH.MF.ZS |         BGR |    Bulgaria |                       High-technology exports (% of manufactured exports) |     null |     null |     null |    2.87184 |    7.91305 |    7.95768 |       null
       IP.TMK.NRES |         BGR |    Bulgaria |                                Trademark applications, direct nonresident |      506 |      453 |     4448 |       5726 |       2613 |       1717 |       null
    IP.JRN.ARTC.SC |         BGR |    Bulgaria |                                 Scientific and technical journal articles |     null |     null |     null |       null |     2599.1 |     2558.7 |       null
       IP.PAT.NRES |         BGR |    Bulgaria |                                         Patent applications, nonresidents |      886 |      804 |      331 |        709 |         17 |         11 |       null
    TX.VAL.TECH.CD |         BGR |    Bulgaria |                                     High-technology exports (current US$) |     null |     null |     null | 7.8619e+07 | 8.0246e+08 |  1.186e+09 |       null
    BM.GSR.ROYL.CD |         BGR |    Bulgaria | Charges for the use of intellectual property, payments (BoP, current US$) |     null |     null |     null | 9.8608e+06 | 1.1424e+08 | 1.8351e+08 | 1.7657e+08
       IP.TMK.RESD |         BGR |    Bulgaria |                                   Trademark applications, direct resident |       70 |       42 |      382 |       3256 |       4308 |       4234 |       null

```

## Lab 3 - SSE

- Create a RequestMapping that returns simulated live update
- @GetMapping(value = "/updates/{countryCode}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
- Use Flux.interval
- Create a RequestMapping that returns a Mono<Indicator> and simulates a long-running calculation by calling Sleep and finally published an SSE Event

## Lab 4 - Consumer Microservice

- <http://start.spring.io> : Create a Web Project Using Gradle, Spring Boot, Reactive Web, Lombok and DevTools.
- Open in IDE
- Add implementation('io.projectreactor.addons:reactor-extra')  and implementation('io.dropwizard.metrics:metrics-core:3.2.6') to build.gradle
- Create Controller Class with @RestController and @RequestMapping
- Create method that is a @GetMapping with path variable code that returns Mono<Double>
- Use MathFlux to return the global sum of the year 2017 of the specified indicator 
- Add a method that returns the global average for the specified Indicator for the year 2017