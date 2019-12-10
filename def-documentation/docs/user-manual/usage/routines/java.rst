.. _routines-java:

========================
Java Routine Development
========================

.. role:: java(code)
   :language: java

General
=======

To create :ref:`Routines <routine>` in Java is quite simple:
Add the Routine-API to the project and create a new class inherited from either :code:`ObjectiveRoutine`, :code:`MapRoutine` or :code:`ReduceRoutine`.


.. _objective-routine-java:

ObjectiveRoutine
================

#. Create a new project in the favored IDE
#. Add the Routine-API to the project
#. Create a new class and inherit from :java:`ObjectiveRoutine`

.. code-block:: java
    :linenos:

    public class MyObjectiveRoutine extends ObjectiveRoutine<MyDataType> {

    	@Override
    	protected MyDataType routine() throws RoutineException {
            // Implementation
        }
    }

4. The generic type, in this case :java:`MyDataType`, should be replaced by the return/output :ref:`data-type`. If the correct DataType is not available, please follow the instructions on :ref:`data-type-dev`
#. Implement the Routine logic. To access the input parameters from the :ref:`task` the method :java:`T getParameter(String name, Class<T> cls)` can be used
#. Create a *fat JAR* (see `What is a fat JAR <https://stackoverflow.com/questions/19150811/what-is-a-fat-jar>`_), which include all dependencies

PICalc example
--------------

Below find the implementation of the :ref:`getting-started` example, whereby the interface can be read like :java:`public double PICalc(double start, double end, double stepSize)`.

.. code-block:: java
    :linenos:

    public class PICalc extends ObjectiveRoutine<DEFDouble> {
        @Override
        protected DEFDouble routine() throws RoutineException {
            try {
                double start = getParameter("start", DEFDouble.class).getValue();
                double end = getParameter("end", DEFDouble.class).getValue();
                double stepSize = getParameter("stepSize", DEFDouble.class).getValue();

                double sum = 0.0;
                for (double i = start; i < end; i++) {
                    double x = (i + 0.5) * stepSize;
                    sum += 4.0 / (1.0 + x * x);
                }
                sum *= stepSize;

                return new DEFDouble(sum);

            } catch (AccessParameterException e) {
                throw new RoutineException(e);
            }
        }
    }


.. _map-routine-java:

MapRoutine
==========

#. Create a new project in the favored IDE
#. Add the Routine-API to the project
#. Create a new class and inherit from :java:`MapRoutine`

.. code-block:: java
    :linenos:

    public class MyMapRoutine extends MapRoutine<SourceType, DestType> {
        public MyMapRoutine() {
            super(SourceType.class);
    	}

        @Override
        protected List<ITuple<String, DestType>> map(SourceType src) {
            // Implementation
        }
    }

4. The generic types, in this case :java:`SourceType` and :java:`DestType`, should be replaced by the real mapping types
#. Implement the map logic, to convert the :java:`SourceType` to one or more key-value pairs, with :java:`DestType` as value
#. Create a *fat JAR* (see `What is a fat JAR <https://stackoverflow.com/questions/19150811/what-is-a-fat-jar>`_), which include all dependencies

DoubleToIntMapper Example
-------------------------

The example below shows how to convert a :java:`DEFDouble` to a key-:java:`DEFInteger` set:

.. code-block:: java
    :linenos:

    public class DefaultDoubleIntegerMapper extends MapRoutine<DEFDouble, DEFInteger> {
        public DefaultDoubleIntegerMapper() {
            super(DEFDouble.class);
        }

        @Override
        protected List<ITuple<String, DEFInteger>> map(DEFDouble source) {
            List<ITuple<String, DEFInteger>> mapping = new LinkedList<>();
            DEFInteger mappedValue = new DEFInteger(Double.valueOf(source.getValue()).intValue());
            mapping.add(new Tuple<>(DEFAULT_KEY, mappedValue));
            return mapping;
        }
    }


.. _reduce-routine-java:

ReduceRoutine
=============

The implementation of the :ref:`reduce-routine` is more complex then Objective- or MapRoutine, because the underlying process is more complex:

- Every time a result from a :ref:`task` is available it will be immediately forwarded the to the ReduceRoutine and passed to the method :java:`reduceValue(String key, T value)`
- After all Tasks are done and therefore all results passed to the ReduceRoutine the method :java:`List<ITuple<String, T>> finalizeReduce()` will be called to finalize the reduce operation and fetch the reduced results

#. Create a new project in the favored IDE
#. Add the Routine-API to the project
#. Create a new class and inherit from :java:`ReduceRoutine`

.. code-block:: java
    :linenos:

    public class MyReduceRoutine extends ReduceRoutine<MyDataType> {
        public MyMapRoutine() {
            super(MyDataType.class);
    	}

        public MyReduceRoutine() {
            super(MyDataType.class);
        }

        @Override
        protected void reduceValue(String key, MyDataType value) {
            // Implementation
        }

        @Override
        protected List<ITuple<String, MyDataType>> finalizeReduce() {
            // Implementation
        }
    }

4. The generic type, in this case :java:`MyDataType`, should be replaced by the return/output :ref:`data-type`. If the correct DataType is not available, please follow the instructions on :ref:`data-type-dev`
#. Implement the reduce logic by implementing both methods as described above
#. Create a *fat JAR* (see `What is a fat JAR <https://stackoverflow.com/questions/19150811/what-is-a-fat-jar>`_), which include all dependencies

Sum example
-----------

Below find the implementation of the :ref:`getting-started` example, which sum up all Task results regarding to the key.
The key will be provided/created by the MapRoutine.

.. code-block:: java
    :linenos:

    public class DoubleSumReducer extends ReduceRoutine<DEFDouble> {
        private final Map<String, DEFDouble> results;

        public DoubleSumReducer() {
            super(DEFDouble.class);
            results = new HashMap<>();
        }

        @Override
        protected void reduceValue(String key, DEFDouble value) {
            if (results.containsKey(key)) {
                double sum = results.get(key).getValue() + value.getValue();
                results.get(key).setValue(sum);
            } else {
                results.put(key, value);
            }
        }

        @Override
        protected List<ITuple<String, DEFDouble>> finalizeReduce() {
            List<ITuple<String, DEFDouble>> rv = new LinkedList<>();
            results.entrySet().forEach(
                entry -> rv.add(new Tuple<>(entry.getKey(), entry.getValue()))
            );
            return rv;
        }
    }


.. client-routine-java::

ClientRoutine
=============

TODO
