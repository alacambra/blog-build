#!/bin/bash
SCRIPT_DIR=$(dirname $0)

docker build -t 'alacambra/openshift-solr:7.7' -f ${SCRIPT_DIR}/Dockerfile ${SCRIPT_DIR}
