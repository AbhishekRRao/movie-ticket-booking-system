SHELL := powershell.exe
.SHELLFLAGS := -NoProfile -ExecutionPolicy Bypass -Command

JAVAC ?= javac
JAVA ?= java
MVN ?= mvn

SOURCES = Main.java controller/*.java enums/*.java factory/*.java model/*.java singleton/*.java view/*.java

.PHONY: all build run clean maven-build maven-clean

all: build

build:
	$(JAVAC) $(SOURCES)

run: build
	$(JAVA) Main

clean:
	Get-ChildItem -Path . -Recurse -Filter *.class | Remove-Item -Force
	if (Test-Path target) { Remove-Item -Recurse -Force target }
	if (Test-Path out) { Remove-Item -Recurse -Force out }

maven-build:
	$(MVN) -DskipTests clean package

maven-clean:
	$(MVN) clean
