#!/bin/bash
NETWORK=azugaj_mreza_1

docker run -it -d \
  -p 8070:8080 \
  --network=$NETWORK \
  --ip 200.20.0.4 \
  --name=azugaj_payara_micro \
  --hostname=azugaj_payara_micro \
  azugaj_payara_micro:6.2023.4 \
  --deploy /opt/payara/deployments/azugaj_aplikacija_2-1.0.0.war \
  --contextroot azugaj_aplikacija_2 \
  --noCluster &

wait
