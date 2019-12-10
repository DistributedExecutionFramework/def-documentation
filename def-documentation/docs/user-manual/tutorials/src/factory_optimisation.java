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
import java.util.Random;
import java.util.concurrent.Future;

public class Example {

    public static void main(String[] args) throws Exception {
        // Variables used for simulation
        int m = 10;
        int r = 1;

        // Variables used for optimisation
        final double profitPerPart = 18.0;        // 18€ per part
        final double budget = 1000000;            // 1.000.000 € budget
        final double costPerRepairMan = 60000;    // cost per year
        final double costPerMachine = 50000;      // cost per year

        // Variables used for optimisation algorithm
        final int maxGenerations = 20;
        final int replications = 10;
        int currentGeneration = 0;          // generation count
        int[] bestSolution = new int[] {m, r}; // index 0 => m, index 1 => r
        double bestFitness = Double.MIN_VALUE;
        Random rnd = new Random();

        // ...
        // Create client
        ServiceEndpointDTO managerEndpoint = new ServiceEndpointDTO("10.0.50.53", 40002, Protocol.THRIFT_TCP);
        IDEFClient defClient = DEFClientFactory.createClient(managerEndpoint);

        // Create program
        Future<String> fPId = defClient.createProgram("cluster1", "workshop");
        String pId = fPId.get();

        for (currentGeneration = 0; currentGeneration < maxGenerations; currentGeneration++) {
            // Create Job.
            Future<String> fJId = defClient.createJob(pId);
            String jId = fJId.get();

            // Attach reduce routine
            defClient.attachReduceRoutine(pId, jId, "<MeanFactorySimulationResult routine id>");

            // Mutation
            if (rnd.nextDouble() < 0.5) {
                m = bestSolution[0] + rnd.nextInt(4);
            } else {
                m = bestSolution[0] - rnd.nextInt(4);
            }

            if (rnd.nextDouble() < 0.5) {
                r = bestSolution[1] + rnd.nextInt(2);
            } else {
                r = bestSolution[1] - rnd.nextInt(2);
            }

            // Guard
            m = m <= 0 ? 1 : m;
            r = r <= 0 ? 1 : r;

            RoutineInstanceDTO routine = new RoutineInstanceBuilder("<FactorySimulation routine id>")
                    .addParameter("repairTime", new DEFDouble(240.0))
                    .addParameter("weeks", new DEFInteger(52))
                    .addParameter("mttf", new DEFDouble(10080.0))
                    .addParameter("m", new DEFInteger(m))
                    .addParameter("r", new DEFInteger(r))
                    .addParameter("ptSigma", new DEFDouble(60.0))
                    .addParameter("ptMean", new DEFDouble(0.1))
                    .build();

            for (int t = 0; t < 1; t++) { // create only one task
                defClient.createTask(pId, jId, routine);
            }

            defClient.markJobAsComplete(pId, jId);
            JobDTO job = defClient.waitForJob(pId, jId); // Blocking call: waits if job reach the state "SUCCESS" or "FAILED".
            if (job.getState() == ExecutionState.SUCCESS) {
                FactorySimulationResult reducedResult = defClient.extractReducedResult(job, FactorySimulationResult.class);
                double fitness = calculateProfit(reducedResult, profitPerPart, costPerMachine, costPerRepairMan, r, budget);
                if (fitness >= bestFitness) {
                    bestFitness = fitness;
                    bestSolution[0] = m;
                    bestSolution[1] = r;
                    System.out.println("  Found new best solution: profit=" + fitness + " m=" + m + " r=" +r);
                }
            }
            defClient.deleteJob(pId, jId); // Optional
        }

        defClient.markProgramAsFinished(pId);
        defClient.deleteProgram(pId); // Optional: Delete all resources
    }

    private static double calculateProfit(FactorySimulationResult result, double profitPerPart, double costPerMachine, double costPerRepairMan, int repairMen, double budget) {
        double cost = costPerMachine * result.machines.size() + costPerRepairMan * repairMen;
        if (cost > budget) {
            return Double.MIN_VALUE;
        }

        double profit = 0;
        for (MachineInfo machine : result.machines) {
            profit += machine.partsMade * profitPerPart;
        }
        return profit - cost;
    }
}