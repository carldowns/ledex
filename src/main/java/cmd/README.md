
# Command / Event System

A general mechanism for handling persisted workflow within this system.

## Primary Use Cases

* Manage workflow / interactions for admins, buyers, suppliers and other service providers
* Track user and system interactions / history

## WorkFlow / Funnel Capabilities

* Automatically respond to users in an intuitive and personal manner
* Automatically transition (funnel) from lead to visitor
* Automatically transition (funnel) from visitor to member
* Automatically transition (funnel) from member to client
* Automatically transition (funnel) from lead to supplier
* Automatically transition (funnel) from lead to shipper

## Workflow / UI Capabilities
For UI presentation purposes, flow of control, etc., we pull both Command history and pending conditions to
drive what appears to the UI user.

* 'Here is that quote you started but never finished'
* 'Here are the last 10 queries you run'
* 'You need to get a membership'
* 'Your membership is pending'
* 'You have been approved and
* 'Here is an email'
* etc.

##Command Persistence

Commands serve as a durable record of everything the system does, when and form whom it was done.  Command data is
persisted in Json form.  When a state change is affected, the Command state and data is updated in the store.

## Command Owner

Command instances are owned by an 'Owner'.  Most will be owned by admin user at first, but these are also attributable to
visitors (cookie), members, vendors and so on.  There is a 'system' user as well.

Command instances are timestamped.

## Events

Command objects can both send and receive Events.  Events are used to coordinate activity between dependent commands.
Events also enable the system to respond to outside stimuli as well as internal transitions and conditions.

In the simplest case, a REST request or cmd line invocation is considered an Event that kicks off a command.  For
workflow use cases, Commands can create a stepwise progression using a Command --> Event --> Command linkage.

Command objects simply issue an Event to spawn child Command instances.  They can fire-and-forget the event, or register
to receive an event back that matches the event they sent (an ACK of sorts).

In the fire-and-forget case, a command simply starts child commands with no concern over how the get along or
even whether they complete satisfactorily.  In this situation, the parent and child can continue on separate threads.

In the waiting case, the parent Command expects to receive events back from it child/children.  It WAITS for the
Event to confirm that children cmd objects finish what they are supposed to do.  In this way,  a Command can issue
several concurrent child commands that can be executed at the same time if needed.  Thus command processing is always
event driven.

## Time Events

Command instances that issue an Event to child cmd will wake each time they receive a Child event.  They will wait
forever if no events come, so it will be useful to also issue a Time Event for themselves to figure out that they
need to clean up, retry, or whatever.

* 'wake up in 30 minutes'
* 'wake up every weekday hour'
* 'wake when someone clicks on the special'
* etc.

## Command Aborting

To clean up, a Command can send an Event to its children cmd objects to get them to ABORT.

## Concurrency Control

Distributed mutex control will be needed for Command / Event paired processing.  This is required because
parent commands can receive multiple events from many children.  We need to guarantee that any given command is
only processing one event at a time.  The mechanism to manage this may be Zookeeper or perhaps an ACID store mutex
based on the Command row itself using a select-for-update mnemonic.

## Archival

Because this system tracks a granular history, an archival mechanism will eventually be needed to move old
commands off at periodic intervals to archive tables.





