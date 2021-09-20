# kurs-java-koty91
Simple web application (spring mvc dispatcher servlet xml configured) connected with local oracle database table using entityManagerFactory 
(object of the LocalContainerEntityManagerFactoryBean type)and the transactionManager (object of the JpaTransactionManager type) 
both defined as beans in application-servlet.xml and methods @Transactional annatated in DAO class. 
User may see the list of objects (cats) from the table , the properties of the chosen one and add the new object (cat) assisted with hibernate form validation.
