=================================
Factory Simulation & Optimization
=================================

In this example a factory with :math:`m` identical machines will be optimized in terms of profitability.
This example consists of two main parts:

* Simulation: Factory simulation with a specified setup
* Optimizer: Runs factory simulations to find the most efficient setup


Definitions
===========

========= ========================
Var       Definition
========= ========================
:math:`m` number of machines
:math:`r` number of repair mans
:math:`p` number of produced parts
========= ========================

The profitability will be defined as follows:

* Every produced part will create :math:`c_p` € profit
* Every machine costs :math:`c_m` € per year
* Every repair man costs :math:`c_r` € per year
* :math:`Profit = c_p * p - (c_m * m + c_r * r)`


.. factory-simulation:

Factory Simulation
==================
The factory simulation is borrowed from `SimPy - Machine Shop <https://simpy.readthedocs.io/en/latest/examples/machine_shop.html>`_ with some modifications.

In short, a factory with :math:`m` identical machines produces some parts. Every part is done in a specified average time and a machine can crash at any time. The factory employed a specified number of repair mans which are responsible to repair the machines.

The following parameters can be defined for the simulation:

=========== ==== ==================
Parameter   Unit Description
=========== ==== ==================
m                Number of machines
r                Number of repair mans
ptMean      min  Process time of a part - normal distributed, mean part
ptSigma     min  Process time of a part - normal distributed, sigma part
mttf        min  Mean time to fail (crash of a machine)
repairTime  min  Repair time of a machine
weeks       week Simulation duration
=========== ==== ==================

The factory simulation will be produce the following output:

* Number of produced parts (:math:`p`) for every machine (:math:`m_i`)
* Number of fails/crashes for every machine (:math:`m_i`)



Optimizer
=========

**TODO**


Deployment Idea
===============

A simulation run with a specified setup (Parameters) is stochastic, because of process times and machine fails, and produces therefore every time a different result.
This means, two simulation runs with the same setup ends in two different result.

The idea is to run a simulation with a specified setup :math:`x` times to get a representative result.
In terms of DEF (see :ref:`understanding_def`), one simulation run is a :ref:`task` and :math:`x` :ref:`task` will be a :ref:`job` with a :ref:`reduce_routine`.



Steps
=====

#. Create needed :ref:`datatype`
#. Create a :ref:`objective_routine` for the factory simulation (name: *FactorySimulation*)
#. Upload the *FactorySimulation* routine
#. Test the new uploaded *FactorySimulation* routine
#. Create a :ref:`reduce_routine` to combine all single simulation results
#. Create a :ref:`client` to run the optimizer


Create :ref:`Datatypes <datatype>`
----------------------------------

First, all needed :ref:`Datatypes <datatype>` should be created.
All of the input parameters are scalar values and we can reuse the existing and predefined ``DEF*`` :ref:`Datatypes <datatype>`.
The result of the simulation is a special structure and therefore the following :ref:`datatype` should be created with the name ``FactorySimulationResult``.

.. literalinclude:: factory-simulation/datatype.thrift

=============== ========= ===============
Parameter       Direction :ref:`datatype`
=============== ========= ===============
``m``           in        ``DEFInteger``
``r``           in        ``DEFInteger``
``ptMean``      in        ``DEFDouble``
``ptSigma``     in        ``DEFDouble``
``mttf``        in        ``DEFDouble``
``repairTime``  in        ``DEFDouble``
``weeks``       in        ``DEFInteger``
``result``      out       ``FactorySimulationResult``
=============== ========= ===============


Create :ref:`objective_routine` *FactorySimulation*
---------------------------------------------------

The factory simulation is based on `SimPy <https://simpy.readthedocs.io/en/latest>`_ and the programing language Python.
To create an :ref:`objective_routine` for Python the template project can be used: :download:`Python Routine Template <../resources/python_routine_template.zip>`.

#. Download and unzip the template
#. Rename the unzipped directory to ``factory_simulation``
#. Generate and download the :ref:`datatype` ``FactorySimulationResult`` for Python (see **TODO**)
#. Copy the downloaded ``FactorySimulationResult`` (placed in ``ttypes.py``) into the new directory ``factory_simulation``
#. Rename the file ``demo_routines.py`` to ``factory_simulation_routine.py`` and rename the class name from ``DemoObjectiveRoutine`` to ``FactorySimulation``
#. Also update the reference's in ``__main__.py``
#. :download:`Download <factory-simulation/machine_shop.py>` and place the modified `Machine Shop Example <https://simpy.readthedocs.io/en/latest/examples/machine_shop.html>`_ into ``factory_simulation``
#. Bring all together in the ``factory_simulation_routine.py``:

   * Fetch input parameters (see table above)
   * Run the simulation (``machine_shop.py``)
   * Convert the result of the simulation and return it with the new created ``FactorySimulationResult`` :ref:`datatype`

.. literalinclude:: factory-simulation/factory_simulation_routine.py

At the end the the directory ``factory_simulation`` should contain the following files:

   - ``build_routine_binary.sh``
   - ``factory_simulation_routine.py``
   - ``machine_shop.py``
   - ``__main__.py``
   - ``ttypes.py``


Upload :ref:`objective_routine` *FactorySimulation*
---------------------------------------------------

Before uploading a :ref:`routine` all necessary should be packed to supported format (also known as routine binary).
In case of Python its a simple ``zip`` file, which can be generated by executing the provided shell script ``build_routine_binary.sh``:

    $ sh build_routine_binary.sh

TODO: Create Routine on WebManager
