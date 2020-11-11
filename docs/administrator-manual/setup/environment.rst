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

All information about the DEF instance at the FHV can be found `here <https://fhvorarlberg-my.sharepoint.com/:w:/g/personal/rosa_fhv_at/EcbSAjMCLx1FixQl_bTfntAB4UUIfb0gqyK4emwCRaZr8w?e=eseWfm>`_.
