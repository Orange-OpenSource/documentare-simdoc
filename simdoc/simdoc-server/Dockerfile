FROM registry.gitlab.com/orange-opensource/documentare/documentare-simdoc-ci-docker:master

MAINTAINER Denis Boisset "denis.boisset@orange.com" & Christophe Maldivi "christophe.maldivi@orange.com"

ENV LC_ALL C.UTF-8

# Install our application
COPY *.jar .

# Expose server port
EXPOSE 8080

ADD run.sh run.sh

# Start app
CMD ./run.sh
