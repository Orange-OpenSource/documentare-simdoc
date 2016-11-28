# Build graphviz for Simdoc, on ubuntu Trusty

# Install missing dependencies

sudo apt-get install freeglut3-dev libann-dev libexpat1-dev libfontconfig1-dev libgts-dev libfreetype6-dev libgd-tools libgd-dev libgdk-pixbuf2.0-dev libxrender-dev libgtk2.0-dev librsvg2-2.0-cil-dev libpopplerkit-dev libgs-dev

# Configure and build

./configure --prefix=/opt/local/ && make install

