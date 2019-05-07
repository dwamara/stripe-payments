FROM dwamara/dwitech-quarkus:0.14.0

LABEL maintainer="Daniel Wamara, dwitech.com"

ENV MODULE_NAME stripe-payments

COPY target/${MODULE_NAME}-runner.jar ${DEPLOY_DIR}
COPY target/lib/*.jar ${INSTALL_DIR}/

ENTRYPOINT java -jar -Dquarkus.http.host=0.0.0.0  ${DEPLOY_DIR}/${MODULE_NAME}-runner.jar

EXPOSE 8080