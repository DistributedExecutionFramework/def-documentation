import asyncio
from def_api.client import DEFClient
from def_api.client_helper import RoutineInstanceBuilder, extract_result
from def_api.thrift.communication.ttypes import Protocol
from def_api.thrift.transfer.ttypes import *
from def_api.ttypes import *

# Getting started - PI Calculation

# Setup asyncio for future support.
loop = asyncio.get_event_loop()
# create client
client = DEFClient(host='<replace with manager address>', port=40002, protocol=Protocol.THRIFT_TCP)

# Create program
future_p_id = client.create_program('<replace with clusterId>', '<replace with userId>')
loop.run_until_complete(future_p_id)
p_id = future_p_id.result()

# Define parameters
nr_of_tasks = 100
nr_of_jobs = 1
steps = 1e10
step_size = 1 / steps
batch_size = steps / nr_of_tasks

j = 0
while j < nr_of_jobs:
    j = j + 1
    # Create job
    future_j_id = client.create_job(p_id)
    loop.run_until_complete(future_j_id)
    j_id = future_j_id.result()
    # Attache ReduceRoutine 'DoubleSumReducer'
    client.attach_reduce_routine(p_id, j_id, 'cdc19ccb-047a-38bd-8b68-3a704b48bafd')

    # Create tasks
    t = 0
    start = 0
    end = batch_size
    while t < nr_of_tasks:
        t = t + 1
        # Prepare routine instance and create a task
        builder = RoutineInstanceBuilder('cfec958c-e34f-3240-bcea-cdeebd186cf6')
        builder.add_parameter('stepSize', DEFDouble(value=step_size))
        builder.add_parameter('start', DEFDouble(value=start))
        builder.add_parameter('end', DEFDouble(value=end))
        client.create_task(p_id, j_id, builder.get_routine_instance())
        start = end
        end = end + batch_size

    client.mark_job_as_complete(p_id, j_id)
    state = client.wait_for_job_finished(p_id, j_id)  # Blocking call which waits to job reach the state SUCCESS or FAILED.

    if state == ExecutionState.SUCCESS:
        # Fetch the reduce result from job instead of all task results
        f_job = client.get_job(p_id, j_id)
        loop.run_until_complete(f_job)
        job = f_job.result()
        print('reduced result: {}'.format(job.reducedResults))

    client.delete_job(p_id, j_id)  # Optional
client.mark_program_as_finished(p_id)
client.delete_program(p_id)  # Optional
