.. _routines-python:

==========================
Python Routine Development
==========================

.. role:: python(code)
   :language: python

General
=======

To develop a Routine in Python (> 3.6), the Routine-API, which can be fetched from the *Resources* tab of the :ref:`web-manager`, must be installed:

.. code-block:: bash

    unzip client-routine-api-x.x.x.zip
    cd python
    pip install . --user --upgrade

The flag :code:`--user` is optional. By leaving out the API will be installed system wide instead of the user environment.


.. _objective-routine-python:

ObjectiveRoutine
================

To create an :ref:`objective-routine` in Python is quite simple, just create a Python class and inherit it from :python:`ObjectiveRoutine`.

#. Create a new project in the favored IDE
#. Download :download:`Python Routine Template <../../resources/python_routine_template.zip>`
#. Unzip the template and copy all files to previous created project
#. Rename the file :code:`demo_routines.py` to a proper name
#. Rename the class :python:`DemoObjectiveRoutine` to a new proper class name
#. Both new names, file and class name, should also be updated in the file :code:`__main__.py`
#. Implement the Routine logic. The input parameters from the :ref:`task` can be fetched by calling the method :python:`self.__get_parameter__(name, MyDataType())`. If the correct DataType not exists, please follow the instructions on :ref:`data-type-dev`

.. code-block:: python
    :linenos:

    from def_api.routine.base import ObjectiveRoutine

    class MyObjectiveRoutine(ObjectiveRoutine):
        def __run__(self):
            # Fetch input parameters
            # Routine logic
            # Return result

8. After implementing the logic, the ObjectiveRoutine must be packed to a single executable binary.
   The delivered Shell script :code:`build_routine_binary.sh` within the template create such a executable binary.
   This script will create a zip file of the folder content and rename it afterwards to :code:`.pyz`.
   Therefore a Windows user can instead of running the script, mark all of the folder content, add it to a zip file and rename it to :code:`<foldername>.pyz`.

PICalc example
--------------

Below the translated example from the :ref:`getting-started` to calculate :math:`\pi` in Python:

.. code-block:: python
    :linenos:

    from def_api.routine.base import ObjectiveRoutine
    from def_api.ttypes import DEFDouble
    from numpy.ma import arange


    class PICalc(ObjectiveRoutine):
        def __run__(self):
            # Fetch parameters
            start = self.__get_parameter__('start', DEFDouble()).value
            end = self.__get_parameter__('end', DEFDouble()).value
            step_size = self.__get_parameter__('stepSize', DEFDouble()).value

            # Calculate PI (part)
            pi = 0.0
            for i in arange(start, end, 1.0):
                x = (i + 0.5) * step_size
                pi = pi + (4.0 / (1.0 + x * x))

            pi = pi * step_size

            # Create result datatype and return it
            result = DEFDouble(value=pi)
            return result


.. _map-routine-python:

MapRoutine
==========

Unfortunately the Routine-API supports at the moment only :ref:`Objective Routines <objective-routine-python>`.


.. _reduce-routine-python:

ReduceRoutine
=============

Unfortunately the Routine-API supports at the moment only :ref:`Objective Routines <objective-routine-python>`.


.. .. _client-routine-python:
..
.. ClientRoutine
.. =============
..
.. Unfortunately the Routine-API supports at the moment only :ref:`Objective Routines <objective-routine-python>`.
