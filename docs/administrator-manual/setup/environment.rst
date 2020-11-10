=================
Environment
=================

.. _ports:

Ports
=======

Internal Service Ports
------------------------

Service ports follow this schema:

.. code-block::

    400xy
       ||
       |`- RESTful / Thrift HTTP / Thrift TCP
       `-- Service


Default ports:

=========================== ========= ============= ===========
Service                     RESTful   Thrift HTTP   Thrift TCP
=========================== ========= ============= ===========
ManagerService              40000     40001         40002
ClusterService              40010     40011         40012
SchedulerService            40020     40021         40022
WorkerService               40030     40031         40032
LibraryService              40040     40041         40042
CloudCommunicationService   40050     40051         40052
ManagerWebService           40060     40061         40062
ReducerService              40070     40071         40072
ClientRoutineService        40080     40081         40082
=========================== ========= ============= ===========


Logging
---------

======================== ============
Host/Port                Value
======================== ============
Graylogging Port (UDP):  12201
Graylogging Host         10.0.50.56
======================== ============


.. _fhv-env:

FHV environment
=================

Production system
-------------------

A DEF instance is maintained at the FHV. It is only possible to connect to this instance within the FHV network. Following the connection data is listed.

=========================== ============= ======================================================
Component                   Name / Id     IP / URL
=========================== ============= ======================================================
*Manager*                   ``manager1``  ``10.0.50.53``
*Web-Manager*                             `http://10.0.50.53 <http://10.0.50.53/>`_
*Manager Library*                         via Web-Manager: `http://10.0.50.53/manager/library <http://10.0.50.53/manager/library>`_
*Cluster*                   ``cluster1``  ``10.0.50.55``
*Scheduler*                               ``10.0.50.55``
*Worker 1* with *Library*                 ``10.0.50.38``
*Worker 2* with *Library*                 ``10.0.50.45``
*Worker 3* with *Library*                 ``10.0.50.40``
*Worker 4* with *Library*                 ``10.0.50.41``
*Worker 5* with *Library*                 ``10.0.50.46``
*Worker 6* with *Library*                 ``10.0.50.36``
*Worker 7* with *Library*                 ``10.0.50.39``
*Worker 8* with *Library*                 ``10.0.50.50``
*Worker 9* with *Library*                 ``10.0.50.51``
*Worker 10* with *Library*                ``10.0.50.43``
*Worker 11* with *Library*                ``10.0.50.47``
*Reducer 1* with *Library*                ``10.0.50.35``
*Client-Routine-Worker 1*                 ``10.0.50.35``
=========================== ============= ======================================================


Test system
------------

Additionally to the production system a test system is maintained. It is as well only possible to connect to this instance within the FHV network. Following the connection data is listed.

=========================== ============== ======================================================
Component                   Name / Id      IP / URL
=========================== ============== ======================================================
*Manager*                   ``t-manager``   ``10.0.40.155``
*Web-Manager*                              `http://10.0.40.1555 <http://10.0.40.155/>`_
*Manager Library*                          via Web-Manager: `http://10.0.40.155/manager/library <http://10.0.40.155/manager/library>`_
*Cluster*                   ``t-cluster``  ``10.0.40.156``
*Scheduler*                                ``10.0.40.156``
*Worker 1* with *Library*                  ``10.0.40.157``
*Worker 2* with *Library*                  ``10.0.40.158``
*Reducer 1* with *Library*                 ``10.0.40.156``
*Client-Routine-Worker 1*                  ``10.0.40.155``
=========================== ============== ======================================================

Physical servers
-----------------

The hardware that is used for the DEF instance at the FHV is located in room E107 (access needs to be activated by facility management).
There are two physical servers with hostnames

* ``jrz-enfilo-def-host01.hostingcenter.uclv.net``
* ``jrz-enfilo-def-host02.hostingcenter.uclv.net``

To create new users the following commands can be used:

``sudo useradd <username>``
``sudo passwd <username>``

Virtualisation environment
^^^^^^^^^^^^^^^^^^^^^^^^^^^

TODO: OpenStack Env.


Graphical Prozessing Unit (GPU)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

On host ``jrz-enfilo-def-host02.hostingcenter.uclv.net`` the GPU Nvidia Tesla K80 is built in. It was intended to virtualise the processing power of the GPU so every worker of the DEF instance can use the GPU.
Unfortunately the Tesla K80 doesn't support GPU virtualisation, so only two workers have access to the GPU.

