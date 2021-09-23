# Custom Spring Test-Auto-Configuration for embedded SQLite test databases

As described in the [Spring-Boot Features documentation][1] you can use 
`@AutoConfigureTestDatabase` to overwrite an existing production datasource
with a datasource of an embedded database. In most cases you would use
`@DataJpaTest`, `@DataJdbcTest` or `@JdbcTest`, respectively, which contain
`@AutoConfigureTestDatabase`.

`sqlite-test-database-autoconfigure` provided the auto-configuration for
an embedded SQLite database to use in Tests.

## Usage

Add the following dependencies to your project additionally to your Spring
and Spring-Boot dependencies.

```xml
<dependencies>
    <dependency>
        <groupId>org.nuvito.spring.test</groupId>
        <artifactId>sqlite-test-database-autoconfigure</artifactId>
        <version>${sqlite-test-database.version}</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.xerial</groupId>
        <artifactId>sqlite-jdbc</artifactId>
        <version>${sqlite.version}</version>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

If your Tests are annotated with `@DataJpaTest`, `@DataJdbcTest` or `@JdbcTest`, 
respectively, the embedded SQLite datasource will be used for those tests.

You can disable the usage of an embedded database by setting the property
`spring.test.database.replace=NONE` or using 
`@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)`
as annotation of your tests.

[1]: https://docs.spring.io/spring-boot/docs/2.5.5/reference/html/features.html#features.testing.spring-boot-applications.autoconfigured-spring-data-jpa