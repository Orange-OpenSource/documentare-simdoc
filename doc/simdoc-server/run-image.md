# Simdoc server docker usage

To pull and run the image: `docker run -p 8080:8080 -v /home/jojo/data:/data registry.gitlab.com/orange-opensource/documentare-simdoc:clustering-1.36.0`

 - -p 8080:8080 to expose container port 8080 to localhost on port 8080
 - -v /home/jojo/data:/data to mount host directory '/home/jojo/data' in container on '/data'

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
