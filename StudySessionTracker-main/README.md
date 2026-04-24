# Study Session Tracker

A comprehensive Java Swing application for tracking and managing study sessions with goal setting, progress monitoring, and detailed statistics.

## Features

- **Timer Management**: Start, pause, and stop study sessions with real-time tracking
- **Subject Management**: Add and remove study subjects with validation
- **Session History**: View detailed history of all study sessions with timestamps
- **Statistics Panel**: Comprehensive statistics including total study time and subject-wise breakdown
- **Goal Tracking**: Set daily and weekly study goals with visual progress bars
- **Data Persistence**: Automatic saving and loading of subjects, sessions, and goals

## Requirements

- Java 8 or higher
- Git

## Installation

1. Clone this repository:
   ```bash
   git clone <repository-url>
   cd StudySessionTracker
   ```

2. Compile the application:
   ```bash
   javac *.java
   ```

3. Run the application:
   ```bash
   java StudySessionTracker
   ```

## Usage

### Adding Subjects
1. Navigate to the "Subjects" tab
2. Click "Add Subject"
3. Enter a subject name
4. The subject will appear in the dropdown for timer sessions

### Starting a Study Session
1. Select a subject from the dropdown in the "Timer" tab
2. Optionally add notes for the session
3. Click "Start" to begin timing
4. Use "Pause/Resume" as needed
5. Click "Stop" to save the session

### Setting Goals
1. Navigate to the "Goals" tab
2. Set your daily and weekly study time goals (in minutes)
3. Click "Save Goals"
4. Progress bars will update automatically as you complete sessions

### Viewing Statistics
1. Navigate to the "Statistics" tab
2. Click "Refresh Statistics" to see the latest data
3. View overall statistics and subject-wise breakdowns

## Data Files

The application stores data in the following files:
- `subjects.dat` - Subject information
- `sessions.dat` - Study session history
- `goals.dat` - Goal settings

*Note: These files are created automatically and are excluded from Git via .gitignore*

## Error Handling

The application includes comprehensive error handling for:
- File I/O operations
- Invalid user input
- Data validation
- Null pointer exceptions

## Technical Details

- **Language**: Java
- **UI Framework**: Swing
- **Data Storage**: Java Serialization
- **Architecture**: MVC pattern with separate managers for subjects and sessions

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is open source and available under the MIT License.
