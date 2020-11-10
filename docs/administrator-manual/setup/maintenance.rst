===========
Maintenance
===========

Process monitoring
===================

To check if all components are running properly, the processes can be viewed.
First you need to connect to the respective machine with SSH:

``ssh def@<hostname>``

With ``ps aux | grep java`` all Java process are listed. For each machine the following processes should be listed:

* Manager machine

    * manager-<version>-all.jar
    * manager-webservice-<version>-all.jar
    * library-<version>-all.jar
* Cluster machine

    * cluster-<version>-all.jar
    * scheduler-<version>-all.jar
    * library-<version>-all.jar
* Worker machine

    * worker-node-<version>-all.jar
    * library-<version>-all.jar
* Reducer machine

    * reducer-node-<version>-all.jar
    * library-<version>-all.jar
    * (MemoryStorer)

.. _updating_components:

Deploying updated components
=============================

To deploy updated components the process is the same as described in :ref:`deployment_process`. Depending on the deployed component the following steps must be performed additionally:

* Manager

    * All its Clusters need to be registered with the Manager again. This is described in :ref:`deployment_process`.
* Cluster

    * All its Workers need to be restarted, so they can register themselves with the Cluster. This is described in :ref:`restarting_components`


.. _restarting_components:

Restarting components
======================

The restarting process is very similar to the :ref:`deployment_process`.

First you need to navigate to the component you want to restart. Then gradle can be used for restarting:

``../gradlew restart -Phost=<hostname>``

Like when updating components, depending on the restarted components the steps that are mentioned in :ref:`updating_components` must be performed additionally.

You can also restart a bunch of Workers with a single step. Just adapt the command mentioned in :ref:`deployment_process` to use ``restart`` instead of ``deploy``.


Adding new features to Nodes
================================

New features (f.e. Python packages) can be installed directly on the machines on which they are needed, f.e. by using a package manager like yum:

``sudo yum install python36-setuptools``

When the installation is finished, the new features have to be added to the YAML file of the Node. This can be done by navigating to the node folder on the machine, f.e. ``worker``, and then using a text editor to open the ``worker.yml`` file:

``vi worker.yml``

Under environment the different programming languages are listed with all their features/extensions. The newly added features need to be added to the respective language with the name and the version. F.e. if you installed the Python package ``numpy`` it needs to be added like following:

.. code-block::

    - name: python
      version: 3.6.8
      group: language
      cmd: 'python3.6 {rbs} {args} {pipes}'
      ext:
        - name: numpy
          version: 1.15.0

Afterwards the Nodes need to be restarted as described in :ref:`restarting_components`.

Troubleshooting
=================

The activities of each component are logged in specific files. If some error occurred it might be found in the log files. They can be found on each machine in the folder of the component, f.e. ``manager/manager.log``.