FROM openjdk:11
ARG JAR_FILE=target/*.jar
ARG AGENT_JAR=dd-java-agent.jar
COPY ${JAR_FILE} app.jar
COPY ${AGENT_JAR} dd-java-agent.jar
ENTRYPOINT ["java","-javaagent:/dd-java-agent.jar", "-Ddd.agent.host=datadog-agent", "-Ddd.agent.port=8126", "-Ddd.profiling.enabled=true","-XX:FlightRecorderOptions=stackdepth=256","-Ddd.logs.injection=true","-Ddd.trace.sample.rate=1","-Ddd.service=DatadogRecruitmentApplication","-Ddd.env=development","-Ddd.logs.injection=true","-jar", "/app.jar"]
