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
            // Fetch reduce result
            FactorySimulationResult reducedResult = defClient.extractReducedResult(job, FactorySimulationResult.class);
            System.out.println(reducedResult);
        }
    }
}
