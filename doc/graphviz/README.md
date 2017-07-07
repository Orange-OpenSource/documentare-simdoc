# Graphviz & Triangulation feature
It is often not sufficient to install the default graphviz package on linux, since most distributions are building graphviz without the "triangulation" feature provided by the `gts` library (The GNU Triangulated Surface library).

# Graphviz on Debian/Ubuntu
To enable the triangulation feature in graphviz, we rebuild our own debian package for that.

Please see here: https://gitlab.com/Orange-OpenSource/documentare/documentare-graphviz

You can find the package in the pipeline artifacts, for instance: https://gitlab.com/Orange-OpenSource/documentare/documentare-graphviz/-/jobs/21454333/artifacts/browse

# Graphviz on mac
Homebrew & Macports are providing a graphviz package, but:
 - homebrew package does not provide the triangulation feature
 - Macports is ok, but you need to install the `gts` library first