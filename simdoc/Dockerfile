FROM registry.gitlab.com/orange-opensource/documentare/documentare-simdoc-ci-docker:master

MAINTAINER Denis Boisset "denis.boisset@orange.com" & Christophe Maldivi "christophe.maldivi@orange.com"

ENV LC_ALL C.UTF-8

RUN mkdir clustering-server
COPY clustering-server/target/*.jar clustering-server/.

RUN mkdir mediation-server
COPY mediation-server/target/*.jar mediation-server/.

# Expose server port
EXPOSE 1958
EXPOSE 2407

ADD run.sh run.sh

# Start app
CMD ./run.sh
