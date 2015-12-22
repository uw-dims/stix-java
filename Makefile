# Makefile for installing Tupelo components into the /opt/dims
# filesystem structure.  Components here means 
# 
# 1: Java JAR files
#
# 2: Shell scripts driving those JARs.

SHELL=/bin/bash

include $(DIMS)/etc/Makefile.dims.global

JARDIR=$(DIMS)/jars/stix

BINDIR=$(DIMS)/bin

OWNER = dims
GROUP = dims
MODE  = 755

INSTALL=install -g $(GROUP) -o $(OWNER) -m $(MODE)

#HELP vars - show variables used in this Makefile
.PHONY: vars
vars:
	@echo JARDIR  is $(JARDIR)
	@echo BINDIR  is $(BINDIR)
	@echo OWNER   is $(OWNER)
	@echo GROUP   is $(GROUP)
	@echo MODE    is $(MODE)
	@echo INSTALL is $(INSTALL)

#HELP install - install jars and binaries
.PHONY: install
install: install-jars install-bin

#HELP install-jars - install jar files
.PHONY: install-jars
install-jars: package installdirs
	@$(INSTALL) cli/target/*.jar $(JARDIR)

#HELP install-bin - install runner scripts
.PHONY: install-bin
install-bin: installdirs
	@$(INSTALL) cli/stix.* $(BINDIR)

#HELP package - build maven package
.PHONY: package
	mvn package


#HELP installdirs - create directories for installation
.PHONY: installdirs
installdirs:
	[ -d $(JARDIR) ] || \
	(mkdir -p $(JARDIR); \
	chown dims:dims $(JARDIR); \
	chmod 755 $(JARDIR); \
	echo "Created $(JARDIR) (dims:dims, mode 755)")
	[ -d $(BINDIR) ] || \
	(mkdir -p $(BINDIR); \
	chown dims:dims $(BINDIR); \
	chmod 755 $(BINDIR); \
	echo "Created $(BINDIR) (dims:dims, mode 755)")

