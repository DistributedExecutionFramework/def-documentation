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

public class ExamplePart2 {

    /**
    * Part2: Fetch results
    */
    public static void main(String[] args) throws Exception {
        // Create client
        ServiceEndpointDTO managerEndpoint = new ServiceEndpointDTO("10.0.50.53", 40002, Protocol.THRIFT_TCP);
        IDEFClient defClient = DEFClientFactory.createClient(managerEndpoint);

        String pId = ""; //  take from web-manager or sysout from ExamplePart1
        String jId = ""; //  take from web-manager or sysout from ExamplePart1

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
    }
}
