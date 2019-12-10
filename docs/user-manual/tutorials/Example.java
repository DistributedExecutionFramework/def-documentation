import at.enfilo.def.client.api.DEFClientFactory;
import at.enfilo.def.client.api.IDEFClient;
import at.enfilo.def.client.api.RoutineInstanceBuilder;
import at.enfilo.def.communication.dto.Protocol;
import at.enfilo.def.communication.dto.ServiceEndpointDTO;
import at.enfilo.def.transfer.dto.ExecutionState;
import at.enfilo.def.transfer.dto.SortingCriterion;
import at.enfilo.def.transfer.dto.JobDTO;
import at.enfilo.def.transfer.dto.RoutineInstanceDTO;
import at.enfilo.def.transfer.dto.TaskDTO;

import java.util.List;
import java.util.concurrent.Future;

public class Example {

    public static void main(String[] args) throws Exception {
        // ...
        // Create client
        ServiceEndpointDTO managerEndpoint = new ServiceEndpointDTO("10.0.50.53", 40002, Protocol.THRIFT_TCP);
        IDEFClient defClient = DEFClientFactory.createClient(managerEndpoint);

        // Create program
        Future<String> fPId = defClient.createProgram("cluster1", "workshop");
        String pId = fPId.get();

        for (int j = 0; j < 1; j++) { // create only one job
            // Create Job.
            Future<String> fJId = defClient.createJob(pId);
            String jId = fJId.get();
            for (int t = 0; t < 1; t++) { // create only one task
                RoutineInstanceDTO routine = new RoutineInstanceBuilder("<FactorySimulation routine id>")
                        .addParameter("repairTime", new DEFDouble(240.0))
                        .addParameter("weeks", new DEFInteger(52))
                        .addParameter("mttf", new DEFDouble(10080.0))
                        .addParameter("m", new DEFInteger(10))
                        .addParameter("r", new DEFInteger(2))
                        .addParameter("ptSigma", new DEFDouble(60.0))
                        .addParameter("ptMean", new DEFDouble(0.1))
                        .build();
                defClient.createTask(pId, jId, routine);
            }

            defClient.markJobAsComplete(pId, jId);
            JobDTO job = defClient.waitForJob(pId, jId); // Blocking call: waits if job reach the state "SUCCESS" or "FAILED".
            if (job.getState() == ExecutionState.SUCCESS) {
                // Fetch all tasks and results.
                List<String> tIds = defClient.getAllTasksWithState(pId, jId, ExecutionState.SUCCESS, SortingCriterion.NO_SORTING).get();
                for (String tId : tIds) {
                    TaskDTO task = defClient.getTask(pId, jId, tId).get();
                    FactorySimulationResult result = defClient.extractOutParameter(task, FactorySimulationResult.class);
                    // Process task result.
                    System.out.println(result);
                }
            }
            defClient.deleteJob(pId, jId); // Optional
        }

        defClient.markProgramAsFinished(pId);
        defClient.deleteProgram(pId); // Optional: Delete all resources
    }
}
