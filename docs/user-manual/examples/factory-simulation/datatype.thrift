typedef string Id

struct MachineInfo {
  1: string name,
  2: i32 partsMade,
  3: i32 repairCount
}

struct FactorySimulationResult {
  1: optional Id _id = "",
  2: list<MachineInfo> machines
}