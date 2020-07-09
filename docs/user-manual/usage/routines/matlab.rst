.. _routines-matlab:

==========================
MATLAB Routine Development
==========================

.. role:: matlab(code)
   :language: Matlab

General
=======

The MATLAB Routine-API is backed from the Java Routine-API, because MATLAB provides and runs a JVM instance within every MATLAB instance.


.. _objective-routine-matlab:

ObjectiveRoutine
================

#. Create a new project in MATLAB
#. Copy the Routine-API files to this project (e.g. DEF folder)
#. Create a new file with following function convention

.. code-block:: Matlab
    :linenos:

    function result = my_function(pl)
        # Implementation
    end

4. Implement the Routine logic. To access the input parameters from the :ref:`task` the function :matlab:`TODO` ...

PICalc example
--------------

Below find the implementation of the :ref:`getting-started` example, whereby the interface can be read like :matlab:`pi_calc(start, end, step_size)`.

.. code-block:: Matlab
    :linenos:

    function result = pi_calc(pl)
        # Implementation
    end

.. _map-routine-matlab:

MapRoutine
==========

Unfortunately the Routine-API supports at the moment only :ref:`Objective Routines <objective-routine-matlab>`.

.. _reduce-routine-matlab:

ReduceRoutine
=============

Unfortunately the Routine-API supports at the moment only :ref:`Objective Routines <objective-routine-matlab>`.

.. .. _client-routine-matlab::
..
.. ClientRoutine
.. =============
..
.. TODO
