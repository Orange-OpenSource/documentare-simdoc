# Simdoc server docker usage

To pull and run the image: `docker run -p 8080:8080 -v /home/jojo/data:/clustering -e JAVA_MEM=10g registry.gitlab.com/orange-opensource/documentare-simdoc:clustering-1.36.0`

 - `-p 8080:8080` to expose container port 8080 to localhost on port 8080
 - `-v /home/jojo/data:/clustering` to mount host directory (i.e. your client computer) '/home/jojo/data' on '/clustering' which is mounted directory name in container (i.e. the docker container which is a virtual machine running on a distant server). All classical mounting rules are applicables here.
 - `-e JAVA_MEM=10g` to start the server with 10 giga bytes of memory

Request example to test it with swagger UI (http://localhost:8080/):
```
{
  "acutSdFactor": 2,
  "debug": true,
  "inputDirectory": "/data/bestioles",
  "outputDirectory": "/data/out",
  "qcutSdFactor": 2
}
```

# Docker survival guide

 - to check if docker works correctly on the machine: `docker run hello-world`
 - to ps: `docker ps`
 - then to kill: `docker kill container-id`
 - then to run a shell in the container: `docker exec -ti container-id bash`
