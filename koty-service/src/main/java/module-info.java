module koty.service {

    requires koty.domain;
    requires java.sql;
    requires java.persistence;
    requires spring.beans;
    requires spring.context;
    requires java.transaction;
    uses pl.kobietydokodu.domain.Kot;
    exports pl.kobietydokodu.service;
}