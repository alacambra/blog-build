#!/bin/bash
SCRIPT_DIR=$(dirname $0)

docker build -t 'alacambra/openshift-solr:8.2.0' -f ${SCRIPT_DIR}/Dockerfile ${SCRIPT_DIR}
