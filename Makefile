MVN ?= mvn

.PHONY: all build run test package clean verify db-check

all: build

build:
	$(MVN) -q -DskipTests clean compile

run:
	$(MVN) -q -DskipTests exec:java

test:
	$(MVN) -q test

package:
	$(MVN) -q -DskipTests package

verify:
	$(MVN) -q -DskipTests verify

db-check:
	@powershell -NoProfile -Command "if (Test-Path movie_ticket_booking.db) { Write-Output 'SQLite DB found: movie_ticket_booking.db' } else { Write-Output 'SQLite DB not found yet. Run: make run' }"

clean:
	$(MVN) -q clean
	@powershell -NoProfile -Command "if (Test-Path movie_ticket_booking.db) { Remove-Item movie_ticket_booking.db -Force }"
