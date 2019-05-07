#!/bin/sh

NAME=stripe-payments
REPO=dwitech-repo:8082

VERSION=$(date "+%Y%m%d_%H%M%S")

mvn clean package && docker build -t ${REPO}/${NAME}:${VERSION} .
docker rm -f ${NAME} || true && docker run -d -p 10301:8080 --name ${NAME} ${REPO}/${NAME}:${VERSION}
