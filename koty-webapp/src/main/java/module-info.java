module koty.webapp {

    requires spring.context;
    requires spring.beans;
    requires koty.service;
    requires koty.domain;
    requires spring.web;
    requires org.hibernate.validator;
    requires java.validation;
    uses pl.kobietydokodu.service.JdbcKotDAO;

}