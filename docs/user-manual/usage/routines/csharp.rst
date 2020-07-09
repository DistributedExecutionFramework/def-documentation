.. _routines-csharp:

==========================
C# Routine Development
==========================

.. role:: csharp(code)
   :language: c#

General
=======

To create :ref:`Routines <routine>` in C# is quite simple:
Add the Routine-API to the project and create a new class inherited from either :code:`ObjectiveRoutine`, :code:`MapRoutine` or :code:`ReduceRoutine`.


.. _objective-routine-csharp:

ObjectiveRoutine
================

#. Create a new solution/project in the favored IDE
#. Add the Routine-API to the project
#. Create a new class and inherit from :csharp:`ObjectiveRoutine`

.. code-block:: c#
    :linenos:

    public class MyObjectiveRoutine : ObjectiveRoutine<MyDataType>
    {

        public static new void Main(String[] args)
        {
            AbstractRoutine.Main(args);
        }

    	protected override Routine()
        {
            // Implementation
        }
    }

4. The generic type, in this case :csharp:`MyDataType`, should be replaced by the return/output :ref:`data-type`. If the correct DataType is not available, please follow the instructions on :ref:`data-type-dev`
#. Implement the Routine logic. To access the input parameters from the :ref:`task` the method :csharp:`GetParameter<T>(string name)` can be used
#. Create ... TODO

PICalc example
--------------

Below find the implementation of the :ref:`getting-started` example, whereby the interface can be read like :csharp:`public double PiCalc(double start, double end, double stepSize)`.

.. code-block:: c#
    :linenos:

    public class PiCalc : ObjectiveRoutine<DEFDouble>
    {
        public static new void Main(String[] args)
        {
            AbstractRoutine.Main(args);
        }

        protected override DEFDouble Routine()
        {
            try
            {
                double start = GetParameter<DEFDouble>("start").Value;
                double end = GetParameter<DEFDouble>("end").Value;
                double stepSize = GetParameter<DEFDouble>("stepSize").Value;

                double sum = 0.0;
                for (double i = start; i < end; i++)
                {
                    double x = (i + 0.5) * stepSize;
                    sum += 4.0 / (1.0 + x * x);
                }

                sum *= stepSize;

                return new DEFDouble
                {
                    Value = sum
                };
            }
            catch (AccessParameterException e)
            {
                throw new RoutineException(e);
            }
        }
    }



.. _map-routine-csharp:

MapRoutine
==========

#. Create a new solution/project in the favored IDE
#. Add the Routine-API to the project
#. Create a new class and inherit from :csharp:`MapRoutine`

.. code-block:: c#
    :linenos:

    public class MyMapRoutine : MapRoutine<SourceType, DestType>
    {
        public static new void Main(String[] args)
        {
            AbstractRoutine.Main(args);
        }

        protected override List<Tuple<string, DestType>> map(SourceType src)
        {
            // Implementation
        }
    }

4. The generic types, in this case :csharp:`SourceType` and :csharp:`DestType`, should be replaced by the real mapping types
#. Implement the map logic, to convert the :csharp:`SourceType` to one or more key-value pairs, with :csharp:`DestType` as value
#. Create ... TODO

DoubleToIntMapper Example
-------------------------

The example below shows how to convert a :csharp:`DEFDouble` to a key-:csharp:`DEFInteger` set:

.. code-block:: c#
    :linenos:

    public class DefaultDoubleIntegerMapper : MapRoutine<DEFDouble, DEFInteger>
    {
        public static new void Main(String[] args)
        {
            AbstractRoutine.Main(args);
        }

        protected override List<Tuple<string, DEFInteger>> map(DEFDouble source)
        {
            var mapping = new List<Tuple<string, DEFInteger>>();
            var mappedValue = new DEFInteger
            {
                value = System.Convert.ToDouble(source.value)
            };
            mapping.add(new Tuple<string, DEFInteger>("DEFAULT", mappedValue));
            return mapping;
        }
    }


.. _reduce-routine-csharp:

ReduceRoutine
=============

Unfortunately the Routine-API supports at the moment no ReduceRoutines.


.. .. _client-routine-csharp:
..
.. ClientRoutine
.. =============
..
.. TODO

