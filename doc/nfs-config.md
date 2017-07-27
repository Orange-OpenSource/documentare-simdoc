## Install nfs server
 - install nfs package: `sudo apt-get install nfs-server`
 - for instance on `g-z820-cm` machine

## Configure nfs sharing on the nfs server
 - create a directory you want to share on your filesystem, for instance: `/data-simdoc`
 - add the configuration to the nfs config file: `/etc/exports`
 
Configuration example:
```
/data-simdoc 10.194.4.196(ro,sync,no_subtree_check) 10.194.7.164(ro,sync,no_subtree_check)
```

to look up the ip address of a known machine: `host g-z820-cm`

## Mount the nfs shared directory on a client machine

 - create the directory on which you want to mount the network directory, for instance: `/data-simdoc`
 - add the mount in the `/etc/fstab` config file, for instance add the following line: `g-z820-cm:/data-simdoc/ /data-simdoc nfs defaults,user,auto,noatime,intr 0 0`
