Please checkout master branch.
App functionality.
Write a message, attach a video or photo. It stores in room database.
Actions.
1)Add a message
2)Search messages
3)Delete a message
4)Edit a message
5)Remove all messages
6)play video directly in application
7)Media preview on top of editText with remove option

Currently only one image or video media is Item possible to send alongside the text. 

By default app fetches 30 messages on per page.
Search is performed by it's text. Click on Top right Search icon.
Delete all messages icon appears on top right corner, if there more than 1 item
Edit and delete icons appear, after long pressing on message item (if a message contains only video or photo there are available only delete action).
Messages Grouped by date.

App contains 6 modules
:app
:database
:domain
:message-core
:message-data
:features/message-main

tech Stack.
Kotlin, MVVM, Dagger hilt, Coil Image Loader, Coroutines, Room Database, PhotoPicker Api, Jetpack Compose, paging
