FROM node:16.0-alpine3.12 as frontend
WORKDIR /frontend
COPY frontend .
RUN npm ci
RUN npm run-script build
FROM maven:3.6.3-jdk-11 as backend
WORKDIR /backend
COPY backend .
RUN mkdir -p src/main/resources/static
COPY --from=frontend /frontend/build src/main/resources/static
RUN mvn clean verify
FROM openjdk:14-jdk-alpine
COPY --from=backend /backend/target/hsl-rebase-backend-1.0.0.jar ./app.jar
EXPOSE 8080
RUN adduser -D user
USER user
CMD [ "sh", "-c", "java -Dserver.port=$PORT -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]