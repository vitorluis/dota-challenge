# Bayes Java Dota Challenge

This is the [task](TASK.md).

Hello All, I'm presenting here my solution to the Dota Challenge.

## Architecture

### Hexagonal Architecture

I have chosen the Hexagonal Architecture (AKA Ports and Adapters) for this project. It is a very modern
architecture, heavily used on Microservices environment. Read
more [here](https://medium.com/idealo-tech-blog/hexagonal-ports-adapters-architecture-e3617bcf00a0).

### Domain-driven design (DDD)

Ports and Adapters go really well with domain-driven design. It is heavily used to slice down huge domains into smaller
independent parts. Read more
about it [here](https://martinfowler.com/bliki/DomainDrivenDesign.html). In this project, even though is small, I
heavily used the [entities](https://blog.jannikwempe.com/domain-driven-design-entities-value-objects) concept, where
the entity holds all the business logic and have its own identity.

### Static factories

As Joshua Block says: `"Consider static factory methods instead of constructors"`. So that's what
I do here in order to construct domain entities. Little bit more about
that [here](https://www.baeldung.com/java-constructors-vs-static-factory-methods#advantages-of-static-factory-methods-over-constructors)
.

## Other Features

### Postgres

I have used Postgres as a Database here, the credentials are directly in the application.yaml file (Ideally it should be
injected via a Config service or Env Variables, but it's out of this challenge context).

### Flyway

In order to create the database schema, I have used here [Flyway](https://flywaydb.org/). Flyway manages database
migrations making
it easier to create/update your database schema.

### OpenAPI

In the original files it came a Swagger configuration, but Springfox is actually deprecated, so I have replaced for
OpenAPI.

### Unit Tests & Code Coverage

The whole codebase is unit tested. JaCoCo is included as well so we can see the code coverage percentage.

## Final Notes

I have left few comments on the code, please check them out. :)

Once again, thank you for the opportunity to make this challenge.