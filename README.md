
## Build And Run (Maven Only)

Run all commands from the project root.

### 1. Compile

```bash
mvn -DskipTests clean compile
```

### 2. Run The Application

```bash
mvn -DskipTests exec:java
```

### 3. Package Jar (Optional)

```bash
mvn -DskipTests package
```

```bash
mvn -DskipTests exec:java -e
```