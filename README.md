**ToDone** is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

User stories:

 * User can add a todo item to the todo list.
 * User can remove a todo item from the todo list.
 * User can edit a todo item from the todo list.
 * Todo's added by user are persistent and app is able to retrieve them on app restart.
 * User can add notes to a todo item.
 * User can set the priority of a todo item. Priority will be displayed in the main list view.
 * Todo items in list are sorted per their priority.
 * User can set the completeion due date of todo items.
 * UI / UX uses Material Design guidelines.
 * Persistent storage is implemented via CursorAdapter and todo items are stored in Sqlite database.
 * Do not allow saving an empty todo.
 * Show a confirmation dialog when user attempts to navigate out of new todo screen without saving the changes.
 * Show the due date of to-do items in main UI list.
 * Use DialogFragment instead of Activity for todo edit/new screen.
 
Walkthrough of all user stories:

![Video Walkthrough](demo.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## License


    Copyright [2016] [Nayan Kumar K]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
