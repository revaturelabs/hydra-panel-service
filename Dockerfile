FROM openjdk:8-jdk-alpine
ADD . /PanelRepositoryService
WORKDIR /PanelRepositoryService
CMD ["java", "-jar", "target/PanelRepositoryService-1.jar"]