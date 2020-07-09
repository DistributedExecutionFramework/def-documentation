.. _shared-resources:

=================
Shared Resources
=================

It might occur that the input parameters for a task are quite large. If more tasks of a program need the same input parameters this leads to huge data traffic between the client and the DEF components. To counteract this problem it is possible to create a shared resource.

A shared resource is attached to the program and is distributed to all workers directly and only once. When creating a task it can be specified that a shared resource is used as an input parameter. If a shared resource is used the data isn't attached to the task and sent over the network to the DEF but rather fetched from the workers directly which already hold the shared resources.

A shared resource can be created in Java as follows.

.. code-block:: java
    :linenos:

    // Create a shared resource
    DEFDouble sharedResource = new DEFDouble(1e-9);
    String rId = execClient.createSharedResource(
            pId,
            sharedResource.get_id(),
            ByteBuffer.wrap(new TSerializer().serialize(sharedResource))
    ).get();

    // Prepare routine
    RoutineInstanceDTO routine = new RoutineInstanceBuilder("<Routine id>")
            .addParameter("resource", rId)
            .build();