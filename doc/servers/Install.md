The goal of this documentation is to explain how to configure your environment and install the simdoc servers.

# Installation on Debian or Ubuntu systems

This documentation is tested in a Debian stretch version.

## Download & Install graphviz debian package

As we need to modify graphviz for our needs, we have a dedicated repository to build the debian package: https://gitlab.com/Orange-OpenSource/documentare/documentare-graphviz

To download the last package version:
    - go to the Pipelines tab: https://gitlab.com/Orange-OpenSource/documentare/documentare-graphviz/pipelines
    - download the last "passed" job artifacts (icon on the right)
    - unzip it, you should have the debian package, for instance: `graphviz_2.38.0-18_amd64.deb`

NB, if you are missing the `sudo` tool:
 - install it: `apt-get install sudo`
 - logged as root (`su`), give current user the sudo rights: `adduser titi sudo`
 - log out, and relog so that modification will be taken into account


Install the debian package: `sudo dpkg -i graphviz_2.38.0-18_amd64.deb`

You may have such error:
```
dpkg: dependency problems prevent configuration of graphviz:
 graphviz depends on libann0; however:
  Package libann0 is not installed.
 graphviz depends on libgvc6; however:
  Package libgvc6 is not installed.
 graphviz depends on libgvpr2; however:
  Package libgvpr2 is not installed.
```

It means that some graphviz dependencies are missing.

Install the missing dependencies: `sudo apt-get -f install` (-f stands for "fix broken")

Now graphviz should be installed. You can give it a try with for instance: `sfdp -?`

## Download & Install simdoc debian package

 - download the last simdoc release package, go to this page: https://github.com/Orange-OpenSource/documentare-simdoc/releases/tag/1.51.0
 - download the tarball file: `documentare-simdoc-x.y.z.tar`
 - if you want to download it with `wget` for instance: do a right click on the file name, and select `Copy Link Location` to retrieve a direct link
  - untar the file: `tar xvf documentare-simdoc-x.y.z.tar`
  - you should find a file like this: `simdoc_x.y.z_amd64.deb`

Install the simdoc debian package: `sudo dpkg -i simdoc_1.51.0_amd64.deb`

To install missing dependencies: `sudo apt-get -f install` (-f stands for "fix broken")

Now simdoc servers should be installed.

## Run simdoc servers

### Start the clustering server

Start: `sudo service clustering-server start`
Check status: `sudo service clustering-server status`

To inspect / change starter script, have a look at the following file: `/etc/init.d/clustering-server`

### Start the mediation server

Start: `sudo service mediation-server start`
Check status: `sudo service mediation-server status`

To inspect / change starter script, have a look at the following file: `/etc/init.d/mediation-server`

### Uninstall simdoc server
`sudo apt-get purge simdoc`

# Create a debian VM with virtualbox

You can find debian ISO images here: https://cdimage.debian.org/debian-cd/current/amd64/iso-cd/

The stable debian version should be preferred
The `netinst` version is smaller and more convenient for a VM installation.
 
 If you don't want to be annoyed by proxy issues, you should do the installation directly through a livebox for instance.
