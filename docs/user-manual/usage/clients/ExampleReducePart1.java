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

public class ExamplePart1 {

    /**
     * Part1: Create jobs & tasks
     */
    public static void main(String[] args) throws Exception {
        // Create client
        ServiceEndpointDTO managerEndpoint = new ServiceEndpointDTO("10.0.50.53", 40002, Protocol.THRIFT_TCP);
        IDEFClient defClient = DEFClientFactory.createClient(managerEndpoint);

        // Create program
        Future<String> fPId = defClient.createProgram("cluster1", "workshop");
        String pId = fPId.get();
        System.out.printf("pId: %s\n", pId);

        for (int j = 0; j < 1; j++) { // create only one job
            // Create Job.
            Future<String> fJId = defClient.createJob(pId);
            String jId = fJId.get();
            System.out.printf("jId: %s\n", jId);

            // Attach reduce routine
            defClient.attachReduceRoutine(pId, jId, "<MeanFactorySimulationResult routine id>");

            RoutineInstanceDTO routine = new RoutineInstanceBuilder("<FactorySimulation routine id>")
                    .addParameter("repairTime", new DEFDouble(240.0))
                    .addParameter("weeks", new DEFInteger(52))
                    .addParameter("mttf", new DEFDouble(10080.0))
                    .addParameter("m", new DEFInteger(10))
                    .addParameter("r", new DEFInteger(2))
                    .addParameter("ptSigma", new DEFDouble(60.0))
                    .addParameter("ptMean", new DEFDouble(0.1))
                    .build();

            for (int t = 0; t < 10000; t++) { // create 1000 tasks
                defClient.createTask(pId, jId, routine);
            }

            defClient.markJobAsComplete(pId, jId);
        }
        defClient.markProgramAsFinished(pId);
    }
}
