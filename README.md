# Java GUI Chat Application

A client-server chat/messaging application built with Java, featuring real-time broadcasting, private messaging, group conferences, and file sharing

Developed as a Preliminary Group Project for Programming 3 (CS 222) at Saint Louis University, Baguio City.

---

## Team Members

- Arellano, Mark Gian
- Balagtey, Gregg Andres
- Bosaing, Ryeth Ezryhee
- Chegyem, Roger
- Marquez, John
- Surro, Jaymee Sofia

**Instructor:**  Roderick Makil


---

## Overview

The application follows a client-server architecture where a server application manages all users, sessions, and message routing. Clients connect to the server through a GUI interface to send messages, join group conferences, and share files.

---

## Features

### Login, Session Management, and Security
- Users must log in before performing any action
- Only one active session is allowed per user at a time (no multiple logins)
- Server-side user management: add, ban, unban, and delete users

### Broadcast Messaging
- Logged-in users can send a message to all other currently online users
- Broadcast messages are not stored; offline users will not see missed broadcasts upon login

### Private Messaging
- Users can send private messages to any other user
- Messages sent to offline users are stored in an XML file on the server and delivered upon the recipient's next login
- Delivered offline messages are removed from the server after being shown to the recipient

### Bookmarking
- Users can bookmark frequently contacted users or groups for quick access
- Bookmarked contacts and groups appear at the top of the user/group list
- Bookmarks can be removed and persist across server restarts via XML storage

### Group Conferences
- Any user can create a group conference and invite other users or groups
- Invited members may also invite additional users
- Only the conference creator can remove/kick members or delete the conference
- Group conferences persist across server restarts via XML storage

### Contact and Group Search
- Search functionality available for finding users and groups when broadcasting, messaging, bookmarking, or sending conference invitations

### File Sharing
- Users can send files to other users or within group conferences

### User Profile Editing
- Users can update their profile details including their password
- Usernames are unique and cannot be duplicated

### Status Updates
- Users can set their status: Online, Idle, Away from Keyboard (AFK), Busy, and others

---

## Data Storage

All persistent data is stored in XML files using the **Java API for XML Processing (JAXP)**, specifically the **SAX** and **DOM** APIs:

| Data | Storage |
|---|---|
| User accounts | XML file managed by the server |
| Offline private messages | XML file, read on server startup and written on shutdown |
| Bookmarks | XML file, persists across restarts |
| Group conferences | XML file, persists across restarts |

---

## Tech Stack

| Component | Technology |
|---|---|
| Language | Java |
| GUI | Java Swing / AWT |
| Networking | Java Sockets |
| XML Processing | JAXP (SAX + DOM) |
| Architecture | Client-Server |

---

## License

This project was created for academic purposes at Saint Louis University. All rights reserved by the respective authors.
