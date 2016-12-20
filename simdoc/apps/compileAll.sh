#!/bin/sh

(cd LineDetection/ && ./gradlew --stop)
(cd LineDetection/ && ./gradlew shadowJar) && (cd Ncd && ./gradlew shadowJar)  && (cd PrepClustering && ./gradlew shadowJar) && (cd SimClustering/ && ./gradlew shadowJar) && (cd Graph && ./gradlew shadowJar) && (cd Multisets && ./gradlew shadowjar) && (cd Ocr && ./gradlew shadowjar)
