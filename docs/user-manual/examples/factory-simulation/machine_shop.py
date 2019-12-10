"""
Machine shop example

Covers:

- Interrupts
- Resources: PreemptiveResource

Scenario:
  A workshop has *n* identical machines. A stream of jobs (enough to
  keep the machines busy) arrives. Each machine breaks down
  periodically. Repairs are carried out by one repairman. The repairman
  has other, less important tasks to perform, too. Broken machines
  preempt theses tasks. The repairman continues them when he is done
  with the machine repair. The workshop works continuously.

"""
import random

import simpy


#RANDOM_SEED = 42
#PT_MEAN = 10.0         # Avg. processing time in minutes
#PT_SIGMA = 2.0         # Sigma of processing time
#MTTF = 300.0           # Mean time to failure in minutes
#BREAK_MEAN = 1 / MTTF  # Param. for expovariate distribution
#REPAIR_TIME = 30.0     # Time it takes to repair a machine in minutes
#JOB_DURATION = 30.0    # Duration of other jobs in minutes
#NUM_MACHINES = 10      # Number of machines in the machine shop
#WEEKS = 4             # Simulation time in weeks
#SIM_TIME = WEEKS * 7 * 24 * 60  # Simulation time in minutes


def time_per_part(pt_mean, pt_sigma):
    """Return actual processing time for a concrete part."""
    return random.normalvariate(pt_mean, pt_sigma)


def time_to_failure(mttf):
    """Return time until next failure for a machine."""
    return random.expovariate(1/mttf)


class Machine(object):
    """A machine produces parts and my get broken every now and then.

    If it breaks, it requests a *repairman* and continues the production
    after the it is repaired.

    A machine has a *name* and a numberof *parts_made* thus far.

    """
    def __init__(self, env, name, repairman, pt_mean, pt_sigma, mttf, repair_time):
        self.env = env
        self.name = name
        self.parts_made = 0
        self.broken = False
        self.pt_mean = pt_mean
        self.pt_sigma = pt_sigma
        self.mttf = mttf
        self.repair_time = repair_time
        self.repair_count = 0

        # Start "working" and "break_machine" processes for this machine.
        self.process = env.process(self.working(repairman))
        env.process(self.break_machine())

    def working(self, repairman):
        """Produce parts as long as the simulation runs.

        While making a part, the machine may break multiple times.
        Request a repairman when this happens.

        """
        while True:
            # Start making a new part
            done_in = time_per_part(self.pt_mean, self.pt_sigma)
            while done_in:
                try:
                    # Working on the part
                    start = self.env.now
                    yield self.env.timeout(done_in)
                    done_in = 0  # Set to 0 to exit while loop.

                except simpy.Interrupt:
                    self.broken = True
                    done_in -= self.env.now - start  # How much time left?

                    # Request a repairman. This will preempt its "other_job".
                    with repairman.request(priority=1) as req:
                        yield req
                        yield self.env.timeout(self.repair_time)

                    self.broken = False

            # Part is done.
            self.parts_made += 1

    def break_machine(self):
        """Break the machine every now and then."""
        while True:
            yield self.env.timeout(time_to_failure(self.mttf))
            if not self.broken:
                # Only break the machine if it is currently working.
                self.process.interrupt()
                self.repair_count = self.repair_count + 1


def other_jobs(env, repairman, job_duration):
    """The repairman's other (unimportant) job."""
    while True:
        # Start a new job
        done_in = job_duration
        while done_in:
            # Retry the job until it is done.
            # It's priority is lower than that of machine repairs.
            with repairman.request(priority=2) as req:
                yield req
                try:
                    start = env.now
                    yield env.timeout(done_in)
                    done_in = 0
                except simpy.Interrupt:
                    done_in -= env.now - start


def run_factory_simulation(m, r, pt_mean, pt_sigma, mttf, repair_time, simulation_weeks, job_duration):
    # Create an environment and start the setup process
    env = simpy.Environment()
    repairman = simpy.PreemptiveResource(env, capacity=r)
    machines = [Machine(env=env, name='Machine %d' % i, repairman=repairman, pt_mean=pt_mean, pt_sigma=pt_sigma, mttf=mttf, repair_time=repair_time)
                for i in range(m)]
    env.process(other_jobs(env, repairman, job_duration))

    # Execute!
    sim_time = simulation_weeks * 7 * 24 * 60
    env.run(until=sim_time)

    return machines
