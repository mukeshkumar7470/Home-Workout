flowchart TB
subgraph presentation [Presentation - MVI]
UI[Compose Screens]
VM[ViewModels]
UI -->|Intent| VM
VM -->|State| UI
VM -->|Effect| UI
end

    subgraph domain [Domain]
        UC[Use Cases]
        REPO_I[WorkoutRepository interface]
        MODEL[Models]
    end

    subgraph data [Data]
        REPO[WorkoutRepositoryImpl]
        JSON[workouts.json]
        PREFS[SharedPreferences progress]
    end

    VM --> UC
    UC --> REPO_I
    REPO --> REPO_I
    REPO --> JSON
    REPO --> PREFS
