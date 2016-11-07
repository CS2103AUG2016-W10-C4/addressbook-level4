# TestScript

## Basic functionality tests

### Loading sample data:

Input: **`load`** `SampleData.xml`  
Output: All tasks from `SampleData.xml` will be loaded into the Uncle Jim. Command feedback will display`file loaded from SampleData.xml`

### Viewing help

Input: **`help`**  
Output: Help display window will be opened

### Adding Todo Items

#### Adding floating tasks

Input: **`add`** `New Task `
Output: A new task will be added, with no time parameter and it will be highlighted blue.

#### Adding deadlines

Type: Overdue
Input: **`add`** `Overdue Deadline /d yesterday 2359`  
Output: A new to-do item will be created, highlighted as red since the deadline has passed. A `task` tag

Type: Upcoming
Input: **`add`** ` Upcoming Deadline /d tomorrow 2359`  
Output: A new to-do item will be created, highlighted as blue. A `task` tag will be added on the item. 

#### Adding events

Type: Upcoming
Input: **`add`** ` Upcoming Event /d tomorrow 4pm to 6pm`  
Output: A new to-do item will be created, highlighted as blue. An `event` tag will be added on the item. 
 
Type: Ongoing
Input: **`add`** ` Ongoing Event /d now to 11pm`  
Output: A new to-do item will be created, highlighted as green. An `event` tag will be added on the item. 

Type: Over
Input: **`add`** ` Yesterday's Event /d yesterday from 2pm to 4pm`  
Output: A new to-do item will be created, highlighted as blue. An `event` tag will be added on the item. Within a minute, since the event is over, the event will be faded, to signify it is already over. 

#### Adding pinned tasks

Input: **`add`** ` Important Event /d tomorrow 4pm to 6pm /p`  
Output:A new to-do item will be created, highlighted as blue. An `event` tag will be added on the item. It will also have a star at the right end, which is white when the event is selected and gold when the event is not selected.

#### Adding tasks with location
Input: **`add`** ` Grand Dinner /d tomorrow 6pm to 8pm /l Marina Bay Sands Grand Ballroom`  
Output:A new to-do item will be created, highlighted as blue. An `event` tag will be added on the item. It's location tag will be displayed as well as shown below.

#### Adding tasks with description

Input: **`add`** ` Buy New Shoes /d tomorrow 5pm /m Remember new Nike's are on sale at Queensway!`  
Output:A new to-do item will be created, highlighted as blue. An `task` tag will be added on the item. It will have the additional ` ...` tag as highlighted below to indicate has more information. This description can be displayed by using the `show` command as described later on.

#### Adding tasks with tags

Input: **`add`** ` V0.5 Demo /d Wednesday 10 am /t CS2103T, Project`  
Output:A new to-do item will be created, highlighted as blue. An `event` tag will be added on the item. It will have additional tags `CS2103T` and `Project`

### Editing tasks

#### Editing task titles

Input: **`edit`** ` 2 V0.5 Submission `  
Output:The second event on the current list will be highlighted as blue. It's title will be changed to V0.5 Submission.

#### Editing task deadlines

Input: **`edit`** ` 3 /d today 4pm`  
Output:The second item on the list will be highlighted as blue. The deadline will be changed from whatever time it was to today at 4pm.

#### Editing task locations

Input: **`edit`** ` 4 /l Botanic Gardens`  
Output:The 4th item on the current list will be highlighted as blue. It's location will be changed from its current location to Botanic Gardens. 

#### Editing task descriptions

Input: **`edit`** ` 5 /m New Description`  
Output:The 4th item on the current list will be highlighted as blue. It's description will now be changed to `New Description` 

#### Toggling pinning

Input: **`edit`** ` 6 /p`  
Output: The 6th item on the list will be now be highlighted blue, with a star at the end to show it is pinned. It will now be at the top with the rest of the pinned tasks. 


### Deleting To-do items

Input: **`delete`** 2  
Output: The second item on the list will be removed.

### Completing To-do items

Input: **`complete`** 3  
Output: The third item on the list will be marked as complete. It will also be highlighted blue.

Input: 

1. **`complete`** 4
2. **`complete`** 4
3. 
Output: The fourth item on the list will be marked as complete after (1), and then it will be marked as incomplete. It will remain highlighted blue throughout

### Pinning To-do items

Input: **`pin`** `2`  
Output: The second task will be pinned to the top of the list and it will be highlighted blue. 

## Advanced functionality tests

### Command Preview Tests

#### Triggering Command Preview 

Input: **`c`** (do not press enter)  
Output: The app will show the command formats of both the `clear` and `complete` command. 

#### Disambiguation 

Input: **`c`** + <kbd>Enter</kbd>  
Output: The app ask the user to disambiguate. 

### View Tests

Views refer to the list of filters shown at the top of the app. 

#### View Completed Tasks 

Input: **`view`** `completed`  
Output: The app will switch to completed view, showing only completed tasks and events.

#### Shortcut for Views 

Input: **`view`** `c`  
Output: The app will switch to completed view. Note that the view shortcut is the underlined letter in the view name. 

### Show Command Tests

Input : **`show`** `6`
Output: If the To-do item selected is a task with a description it will be expanded and all the details will be shown. 

## Errors

### Incorrect index 

Input: `pin 200` (assuming task no. 200 does not exist)  
Output: The CommandFeedbackView will print the string: `There is no task no. 200`

### Incorrect Command

Input: `ghhdfkk`
Output: The CommandFeedbackView will print the string: `ghhdfkk doesn't look like any command we know` 

### Missing Parameters
Input: `delete`
Output: CommandErrorView will specify under field problems:
`index The index parameter is required`

