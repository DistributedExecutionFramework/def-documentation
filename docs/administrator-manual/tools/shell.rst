.. _shell:

=========
DEF Shell
=========

The DEF Shell is a tool for administrators. It can be used for

* monitoring and adapting the infrastructure
* monitoring, starting and aborting calculations

The DEF Shell can be downloaded from the *Resources* tab of the :ref:`web-manager-admin`. The downloaded JAR needs to be executed, f.e. in a Unix terminal with ``java -jar def-shell-<version>-all.jar``.
By pressing on the Tab key it is shown which commands and options are available and how a command can be completed.

First of all it is necessary to connect to the desired service. This can be done with

``service switch --service MANAGER --host <hostname> --port <port> --protocol THRIFT_TCP``

The following services are available:

* Manager
* Cluster
* Worker
* Exec_Logic
* Library
* Scheduler

The appropriate ports for the specific services can be found under :ref:`ports`.

When the connection to the desired service is established, the available commands can be displayed by pressing the Tab key.