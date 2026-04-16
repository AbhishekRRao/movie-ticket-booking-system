JAVAC ?= javac
JAVA ?= java
MVN ?= mvn

SOURCES = Main.java controller/*.java enums/*.java factory/*.java model/*.java singleton/*.java view/*.java notification/*.java admin/*.java admin/builder/*.java admin/command/*.java admin/state/*.java admin/report/*.java

.PHONY: all build run clean maven-build maven-clean

all: build

build:
	$(JAVAC) $(SOURCES)

run: build
	$(JAVA) Main

clean:
	find . -type f -name '*.class' -exec rm -f {} \;
	rm -rf target out

maven-build:
	$(MVN) -DskipTests clean package

maven-clean:
	$(MVN) clean
