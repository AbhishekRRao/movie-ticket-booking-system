# Admin Module - Design Patterns & Principles Documentation

## Overview
The Admin Module provides comprehensive management of movies and shows with reporting capabilities. It enforces MVC architecture and implements 4 distinct design patterns with strong adherence to SOLID principles.

---

## Design Patterns Used

### 1. **COMMAND PATTERN** ✅
**Location:** `admin/command/`

**Purpose:** Encapsulates requests as objects, allowing them to be queued, logged, and undone.

**Components:**
- `Command.java` - Abstract command interface
- `AddMovieCommand.java` - Add movie command
- `UpdateMovieCommand.java` - Update movie command
- `DeleteMovieCommand.java` - Delete movie command
- `ShowCommand.java` - Show-specific command interface
- `AddShowCommand.java` - Add show command
- `UpdateShowCommand.java` - Update show command
- `DeleteShowCommand.java` - Delete show command
- `CommandExecutor.java` - Invoker managing undo/redo stacks

**Benefits:**
- Undo/Redo functionality for all operations
- Command history tracking
- Decoupled object creation from execution
- Queue-able operations for batch processing

**Real-world Use Case:**
```java
// Admin adds a movie and can undo it later
Command addMovieCmd = new AddMovieCommand(movie);
commandExecutor.execute(addMovieCmd);  // Adds movie
commandExecutor.undo();                 // Removes movie
commandExecutor.redo();                 // Adds movie again
```

---

### 2. **BUILDER PATTERN** ✅
**Location:** `admin/builder/`

**Purpose:** Constructs complex objects step-by-step with validation and optional fields.

**Components:**
- `MovieBuilder.java` - Fluent builder for Movie objects
- `ShowBuilder.java` - Fluent builder for Show objects

**Benefits:**
- Readable, fluent API for object construction
- Validation at build time
- Handles optional fields elegantly
- Immutable object creation
- No "telescoping constructor" anti-pattern

**Real-world Use Case:**
```java
// Create a complex movie with validation
Movie movie = new MovieBuilder()
    .setTitle("Inception")
    .setGenre("Sci-Fi")
    .setDuration(148)
    .setRating(8.8)
    .setDirector("Christopher Nolan")
    .build();  // Validates and creates object
```

---

### 3. **STATE PATTERN** ✅
**Location:** `admin/state/`

**Purpose:** Allows objects to alter their behavior when internal state changes. Manages movie lifecycle states.

**Components:**
- `MovieState.java` - State interface
- `ActiveMovieState.java` - Movie is active/showing
- `InactiveMovieState.java` - Movie is hidden/paused
- `ArchivedMovieState.java` - Movie is archived
- `MovieStateContext.java` - Context managing state transitions

**State Transitions:**
```
ACTIVE ←→ INACTIVE
  ↓
ARCHIVED
  ↑
(Can go back to ACTIVE)
```

**Benefits:**
- Clear state management without complex conditionals
- State-specific behavior encapsulation
- Prevents invalid state transitions
- Easy to add new states

**Real-world Use Case:**
```java
MovieStateContext context = new MovieStateContext(1, "Avatar");
context.activate();       // Already active
context.deactivate();     // ACTIVE → INACTIVE
context.archive();        // INACTIVE → ARCHIVED
context.activate();       // ARCHIVED → ACTIVE
```

---

### 4. **TEMPLATE METHOD PATTERN** ✅
**Location:** `admin/report/`

**Purpose:** Defines the skeleton of algorithm in base class, letting subclasses implement specific steps.

**Components:**
- `ReportGenerator.java` - Abstract template with algorithm skeleton
- `BookingReportGenerator.java` - Booking history reports
- `RevenueReportGenerator.java` - Financial reports
- `ShowReportGenerator.java` - Show performance reports

**Algorithm Flow:**
```
1. Validate dates
2. Fetch data
3. Process data (specific)
4. Calculate statistics (specific)
5. Format and display (specific)
6. Generate summary (specific)
```

**Benefits:**
- Code reuse for common steps
- Enforces consistent report generation
- Easy to add new report types
- Maintains DRY principle

**Real-world Use Case:**
```java
// All reports follow the same workflow
ReportGenerator bookingReport = new BookingReportGenerator(startDate, endDate);
bookingReport.generateReport();  // Uses template method

ReportGenerator revenueReport = new RevenueReportGenerator(startDate, endDate);
revenueReport.generateReport();  // Same flow, different implementation
```

---

## SOLID Principles Implementation

### Single Responsibility Principle (SRP)
Each class has ONE reason to change:
- `MovieBuilder` - Only responsible for building movies
- `BookingReportGenerator` - Only generates booking reports
- `ActiveMovieState` - Only handles active state behavior
- `AddMovieCommand` - Only adds movies

### Open/Closed Principle (OCP)
Open for extension, closed for modification:
- **Command Pattern:** Add new commands without modifying existing ones
- **Report Generation:** Add new report types without changing existing reports
- **State Pattern:** Add new states without modifying state logic

```java
// Easy to add new report type without modifying ReportGenerator
public class CustomerReportGenerator extends ReportGenerator {
    // Implementation specific to customer reports
}
```

### Liskov Substitution Principle (LSP)
Subtypes are substitutable for base types:
- `BookingReportGenerator extends ReportGenerator` - Can be used anywhere `ReportGenerator` is expected
- `AddMovieCommand implements Command` - Can be used in `CommandExecutor`
- `ActiveMovieState implements MovieState` - Interchangeable with other states

```java
// Different reports used identically
ReportGenerator report = new BookingReportGenerator(...);  // or
ReportGenerator report = new RevenueReportGenerator(...);
report.generateReport();  // Same code works for all
```

### Interface Segregation Principle (ISP)
Clients depend on small, focused interfaces:
- `Command` interface - Minimal set of operations
- `MovieState` interface - State-specific operations only
- `ShowCommand` extends `Command` - Show-specific operations segregated

```java
// Segregated interface for show commands
public interface ShowCommand extends Command {
    int getShowId();  // Show-specific method
}
```

### Dependency Inversion Principle (DIP)
Depend on abstractions, not concretions:
- `CommandExecutor` depends on `Command` interface, not concrete commands
- `AdminService` depends on builder interfaces, not specific builders
- `MovieStateContext` depends on `MovieState` interface

```java
// DIP: Executor works with abstraction
public void execute(Command command) {  // Depends on interface
    command.execute();
}
```

---

## Architecture Overview

### MVC Pattern
```
View Layer (AdminView)
    ↓
Controller Layer (AdminController)
    ↓
Service Layer (AdminService)
    ↓
Domain Layer (Model objects)
```

### Class Relationships

**Command Pattern:**
```
ClientCode → CommandExecutor (Invoker)
                ├→ Command (Interface)
                ├→ AddMovieCommand
                ├→ UpdateMovieCommand
                ├→ DeleteMovieCommand
                ├→ AddShowCommand
                ├→ UpdateShowCommand
                └→ DeleteShowCommand
```

**Builder Pattern:**
```
ClientCode → MovieBuilder (Fluent)
             ShowBuilder (Fluent)
             └→ Build() → Movie/Show Objects
```

**State Pattern:**
```
ClientCode → MovieStateContext (Context)
             ├→ ActiveMovieState
             ├→ InactiveMovieState
             └→ ArchivedMovieState
```

**Template Method Pattern:**
```
ClientCode → ReportGenerator (Abstract)
             ├→ BookingReportGenerator
             ├→ RevenueReportGenerator
             └→ ShowReportGenerator
```

---

## File Structure

```
admin/
├── command/
│   ├── Command.java                    [Abstract Command]
│   ├── AddMovieCommand.java           [Concrete Command]
│   ├── UpdateMovieCommand.java        [Concrete Command]
│   ├── DeleteMovieCommand.java        [Concrete Command]
│   ├── ShowCommand.java               [Specialized Interface]
│   ├── AddShowCommand.java            [Concrete Command]
│   ├── UpdateShowCommand.java         [Concrete Command]
│   ├── DeleteShowCommand.java         [Concrete Command]
│   └── CommandExecutor.java           [Invoker]
├── builder/
│   ├── MovieBuilder.java              [Fluent Builder]
│   └── ShowBuilder.java               [Fluent Builder]
├── state/
│   ├── MovieState.java                [State Interface]
│   ├── ActiveMovieState.java          [Concrete State]
│   ├── InactiveMovieState.java        [Concrete State]
│   ├── ArchivedMovieState.java        [Concrete State]
│   └── MovieStateContext.java         [Context]
├── report/
│   ├── ReportGenerator.java           [Abstract Template]
│   ├── BookingReportGenerator.java    [Concrete Report]
│   ├── RevenueReportGenerator.java    [Concrete Report]
│   └── ShowReportGenerator.java       [Concrete Report]
├── AdminService.java                   [Service Layer]
├── AdminController.java                [Controller Layer]
└── AdminView.java                      [View Layer]
```

---

## Usage Examples

### Example 1: Adding a Movie with Command Pattern
```java
// Create builder
MovieBuilder builder = new MovieBuilder()
    .setTitle("The Matrix")
    .setGenre("Sci-Fi")
    .setDuration(136)
    .setRating(8.7);

// Add movie (internally uses AddMovieCommand)
adminService.addMovie(builder);

// Undo if needed
adminService.undoLastOperation();

// Redo if needed
adminService.redoLastOperation();
```

### Example 2: Scheduling a Show with Builder Pattern
```java
// Create show with all validations
ShowBuilder builder = new ShowBuilder()
    .setMovie(movieObject)
    .setShowTime(LocalDateTime.now().plusDays(1))
    .setAuditorium("Hall A")
    .setTotalSeats(150)
    .setBasePrice(250.0)
    .setFormat("3D");

// Schedule show (internally uses AddShowCommand)
adminService.addShow(builder);
```

### Example 3: Managing Movie State
```java
// Get state context for a movie
MovieStateContext stateContext = adminService.getMovieState(movieId);

// Transition states
stateContext.deactivate();  // Active → Inactive
stateContext.archive();     // Inactive → Archived
stateContext.activate();    // Archived → Active
```

### Example 4: Generating Reports
```java
// Generate different types of reports efficiently
adminService.generateBookingReport(startDate, endDate);
adminService.generateRevenueReport(startDate, endDate);
adminService.generateShowReport(startDate, endDate);
```

---

## Design Benefits Summary

| Pattern | Benefit |
|---------|---------|
| **Command** | Undo/Redo, Command queuing, Audit trail |
| **Builder** | Readable code, Validation, Optional fields |
| **State** | Clear lifecycle, Prevent invalid transitions |
| **Template Method** | Code reuse, Consistent algorithms |

---

## Integration with Existing System

*Patterns already used in the system:*
- Singleton Pattern (BookingManager, MovieCatalog, etc.)
- Observer Pattern (Notification system)
- Factory Pattern (UserFactory)
- Strategy Pattern (Payment gateways)
- MVC Pattern (Overall architecture)
- Adapter Pattern (Payment processors)

*New patterns in Admin Module:*
- **Command Pattern** ✅
- **Builder Pattern** ✅
- **State Pattern** ✅
- **Template Method Pattern** ✅

**Total SOLID Principles:** All 5
- Single Responsibility ✅
- Open/Closed ✅
- Liskov Substitution ✅
- Interface Segregation ✅
- Dependency Inversion ✅

---

## Future Enhancements

1. **Strategy Pattern for Pricing:** Different pricing strategies for shows (weekend premium, student discount, etc.)
2. **Decorator Pattern:** Add features to movies (subtitles, audio formats)
3. **Proxy Pattern:** Lazy loading of reports
4. **Memento Pattern:** Save complete state snapshots
5. **Iterator Pattern:** Traverse reports and command history

---

Generated: Admin Module v1.0
Design Patterns: 4 | SOLID Principles: 5 | Classes: 25+
