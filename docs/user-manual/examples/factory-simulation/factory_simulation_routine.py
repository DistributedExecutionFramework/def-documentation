from def_api.routine.base import ObjectiveRoutine
from def_api.ttypes import DEFDouble, DEFInteger

from machine_shop import run_factory_simulation
from ttypes import MachineInfo, FactorySimulationResult


class FactorySimulation(ObjectiveRoutine):
    def __run__(self):
        # Fetch parameters
        pt_mean = self.__get_parameter__('ptMean', DEFDouble()).value
        pt_sigma = self.__get_parameter__('ptSigma', DEFDouble()).value
        mttf = self.__get_parameter__('mttf', DEFDouble()).value
        repair_time = self.__get_parameter__('repairTime', DEFDouble()).value
        m = self.__get_parameter__('m', DEFInteger()).value
        r = self.__get_parameter__('r', DEFInteger()).value
        weeks = self.__get_parameter__('weeks', DEFInteger()).value
        job_duration = 30.0

        # Run simulation
        machines = run_factory_simulation(pt_mean=pt_mean, pt_sigma=pt_sigma, mttf=mttf, repair_time=repair_time, m=m,
                                          r=r, simulation_weeks=weeks, job_duration=job_duration)

        # Convert result
        mis = []
        for machine in machines:
            mi = MachineInfo()
            mi.name = machine.name
            mi.partsMade = machine.parts_made
            mi.repairCount = machine.repair_count
            mis.append(mi)

        return FactorySimulationResult(machines=mis)
