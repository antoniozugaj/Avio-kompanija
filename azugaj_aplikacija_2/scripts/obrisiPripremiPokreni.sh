#!/bin/bash
echo "DOCKER STOP:"
docker stop azugaj_payara_micro
echo "DOCKER REMOVE:"
docker rm azugaj_payara_micro
echo "DOCKER PRIPREMI:"
./scripts/pripremiSliku.sh
echo "DOCKER POKRENI:"
./scripts/pokreniSliku.sh
docker logs -f  azugaj_payara_micro 
